package com.matchbridge.chat.mapper;

import com.matchbridge.chat.dto.ChatDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {

    List<ChatDTO> getMessages(@Param("myId") String myId,
                              @Param("otherId") String otherId);

    void saveMessage(ChatDTO dto);

    int getUnreadCount(@Param("myId") String myId);

    List<ChatDTO> getRecentSenders(@Param("myId") String myId);

    int markMessagesAsRead(@Param("myId") String myId,
            @Param("otherId") String otherId);

    
    int countUnreadBySender(@Param("myId") String myId,
            @Param("otherId") String otherId);

}
