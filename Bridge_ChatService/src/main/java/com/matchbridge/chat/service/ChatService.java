package com.matchbridge.chat.service;

import com.matchbridge.chat.dto.ChatDTO;
import com.matchbridge.chat.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMapper mapper;


    // ---------------------------------------------------------
    // 1) 특정 사용자 간 채팅 기록 조회 (정렬 포함)
    // ---------------------------------------------------------
    public List<ChatDTO> getMessages(String myId, String otherId) {
        return mapper.getMessages(myId, otherId);
    }


    // ---------------------------------------------------------
    // 2) 메시지 저장 (WebSocket에서 호출)
    // ---------------------------------------------------------
    public void saveMessage(ChatDTO dto) {
        mapper.saveMessage(dto);
    }


    // ---------------------------------------------------------
    // 3) 내가 아직 읽지 않은 메시지 개수 조회
    // ---------------------------------------------------------
    public int getUnreadCount(String myId) {
        return mapper.getUnreadCount(myId);
    }


    // ---------------------------------------------------------
    // 4) 내가 최근에 메시지를 주고받은 사용자 목록
    // ---------------------------------------------------------
    public List<ChatDTO> getRecentSenders(String myId) {

        List<ChatDTO> list = mapper.getRecentSenders(myId);

        for (ChatDTO chat : list) {
            int unread = mapper.countUnreadBySender(myId, chat.getOtherId());
            chat.setUnreadCount(unread);
        }

        return list;
    }



    // ---------------------------------------------------------
    // 5) 읽음 처리 (상대방→나로 온 메시지)
    // ---------------------------------------------------------
    public int markMessagesAsRead(String myId, String otherId) {
        return mapper.markMessagesAsRead(myId, otherId);
    }

}
