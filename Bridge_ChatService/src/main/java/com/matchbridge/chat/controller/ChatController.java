package com.matchbridge.chat.controller;


import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.matchbridge.chat.dto.ChatDTO;
import com.matchbridge.chat.service.ChatService;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // -----------------------------------------------
    // 1) 특정 유저와의 채팅 내역(HTML에서 사용)
    // -----------------------------------------------
    @GetMapping("/history/{myId}/{otherId}")
    public ResponseEntity<?> getHistory(
            @PathVariable String myId,
            @PathVariable String otherId
    ) {
        return ResponseEntity.ok(chatService.getMessages(myId, otherId));
    }

    // -----------------------------------------------
    // 2) 최근 대화 사용자 목록(HTML에서 사용)
    // -----------------------------------------------
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentChats(HttpSession session) {

        String myId = (String) session.getAttribute("id");
        if (myId == null)
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다."));

        return ResponseEntity.ok(chatService.getRecentSenders(myId));
    }
    
    @GetMapping("/chatroom")
    public List<ChatDTO> getChatRooms(HttpSession session) {
        String myId = (String) session.getAttribute("id");

        System.out.println("💬 chatroom 요청: myId = " + myId);

        if (myId == null) {
            return List.of(); // 로그인 안 됨
        }

        return chatService.getRecentSenders(myId);
    }
    @PostMapping("/read")
    public ResponseEntity<?> markAsRead(
            @RequestParam String otherId,
            HttpSession session) {

        String myId = (String) session.getAttribute("id");

        if (myId == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        int rows = chatService.markMessagesAsRead(myId, otherId);
        System.out.println("📌 read_status 업데이트: " + rows + "개 변경됨");

        return ResponseEntity.ok(Map.of("updated", rows));
    }

}

