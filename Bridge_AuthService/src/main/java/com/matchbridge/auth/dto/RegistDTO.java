package com.matchbridge.auth.dto;

public class RegistDTO {
    private String id;           // 아이디
    private String pw;           // 비밀번호
    private String confirm;      // 비밀번호 확인 (DB 저장 X)
    private String username;     // 이름
    private String mobile;       // 전화번호
    private String gender;       // 성별
    private String age;
    private String imageUrl;     // 이미지 경로
    private String home;
   

	// 나의 프로필
    private String mbti;
    private String smoke;
    private String body;
    private String style;

    // 이상형 프로필
    private String ideal_mbti;
    private String ideal_smoke;
    private String ideal_body;
    private String ideal_style;


    // Getter & Setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getConfirm() {
        return confirm;
    }
    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getHome() {
		return home;
	}
    
	public void setHome(String home) {
		this.home = home;
	}

   
    public String getIdeal_mbti() {
        return ideal_mbti;
    }
    public void setIdeal_mbti(String ideal_mbti) {
        this.ideal_mbti = ideal_mbti;
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
