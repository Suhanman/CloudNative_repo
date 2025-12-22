package com.matchbridge.match.dto;

public class MyProfileDTO {
    private String id;      // 회원 ID (foreign key)
    private String mbti;
    private String smoke;
    private String body;
    private String style;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMbti() {
        return mbti;
    }
    public void setMbti(String mbti) {
        this.mbti = mbti;
    }

    public String getSmoke() {
        return smoke;
    }
    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public String getStyle() {
        return style;
    }
    public void setStyle(String style) {
        this.style = style;
    }
}

