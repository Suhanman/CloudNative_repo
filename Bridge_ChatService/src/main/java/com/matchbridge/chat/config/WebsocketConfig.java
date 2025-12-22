package com.matchbridge.chat.config;

import com.matchbridge.chat.service.ChatService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    private final ChatService chatService;

 
    public WebsocketConfig(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(new ChatWebSocketHandler(chatService), "/ws/chat")
                .setAllowedOrigins("*");  
       
    }
}
