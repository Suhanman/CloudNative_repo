package com.matchbridge.chat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchbridge.chat.dto.ChatDTO;
import com.matchbridge.chat.service.ChatService;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    // 사용자 ID → WebSocket 세션 매핑
    private static final Map<String, WebSocketSession> sessions = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatService chatService; // ✅ 생성자 주입

    // ✅ ChatService를 생성자로 주입
    public ChatWebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    // 클라이언트 연결 시 실행
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            String query = session.getUri().getQuery(); // 예: ?user=jgjg
            if (query != null && query.startsWith("user=")) {
                String userId = query.split("=")[1];
                session.getAttributes().put("userId", userId);
                sessions.put(userId, session);
                System.out.println("[연결됨] 사용자: " + userId);
            }
        } catch (Exception e) {
            System.err.println("[에러] 연결 처리 중 예외 발생: " + e.getMessage());
        }
    }

    // 메시지 수신 시 실행
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, Object> msgMap = objectMapper.readValue(payload, Map.class);

       
        String from = msgMap.get("sender") != null ? msgMap.get("sender").toString() : null;
        String to = msgMap.get("receiver") != null ? msgMap.get("receiver").toString() : null;
        String msgText = msgMap.get("message") != null ? msgMap.get("message").toString() : "";

        System.out.println("🔥 메시지 수신 from: " + from + " → to: " + to + " / " + msgText);

        // 🔥 DB 저장
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setSender(from);
        chatDTO.setReceiver(to);
        chatDTO.setContent(msgText);
        chatService.saveMessage(chatDTO);

        // 🔥 상대에게 전송
        if (to != null) {
            WebSocketSession receiverSession = sessions.get(to);
            if (receiverSession != null && receiverSession.isOpen()) {
                Map<String, String> sendMsg = new HashMap<>();
                sendMsg.put("from", from);
                sendMsg.put("to", to);
                sendMsg.put("message", msgText);
                receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(sendMsg)));
            }
        }

        // 🔥 자기 자신 Echo
        Map<String, String> selfMsg = new HashMap<>();
        selfMsg.put("from", from);
        selfMsg.put("to", to);
        selfMsg.put("message", msgText);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(selfMsg)));
    }


    // 클라이언트 연결 종료 시 실행
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) {
            sessions.remove(userId);
            System.out.println("[연결 종료] 사용자: " + userId);
        }
    }
}
