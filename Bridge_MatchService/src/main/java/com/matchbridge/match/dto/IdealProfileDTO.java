package com.matchbridge.match.dto;

public class IdealProfileDTO {
    private String id;               // 회원 ID
    private String ideal_mbti;       // 이상형 MBTI
    private String ideal_smoke;      // 이상형 흡연 여부
    private String ideal_body;       // 이상형 체형
    private String ideal_style;      // 이상형 스타일

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getIdeal_mbti() {
        return ideal_mbti;
    }
    public void setIdeal_mbti(String ideal_mbti) {
        this.ideal_mbti = ideal_mbti;
    }

    public String getIdeal_smoke() {
        return ideal_smoke;
    }
    public void setIdeal_smoke(String ideal_smoke) {
        this.ideal_smoke = ideal_smoke;
    }

    public String getIdeal_body() {
        return ideal_body;
    }
    public void setIdeal_body(String ideal_body) {
        this.ideal_body = ideal_body;
    }

    public String getIdeal_style() {
        return ideal_style;
    }
    public void setIdeal_style(String ideal_style) {
        this.ideal_style = ideal_style;
    }
}
