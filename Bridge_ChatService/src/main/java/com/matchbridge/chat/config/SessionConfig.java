package com.matchbridge.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();

        serializer.setCookieName("SESSION");  
        serializer.setCookiePath("/");
        serializer.setUseHttpOnlyCookie(true);

        // 🔥 CORS 환경에서 필수
        serializer.setSameSite("lax");

        // 개발 환경: localhost → Secure false
        // HTTPS 배포 시 true
        serializer.setUseSecureCookie(false);

        return serializer;
    }
}
