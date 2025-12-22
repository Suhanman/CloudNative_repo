package com.matchbridge.chat.dto;

public class ChatDTO {
    private Long id;           // 메시지 PK (최근대화에서도 그대로 씀)
    private String otherId;   // 최근 대화 목록에서 상대방 ID

    
	private String sender;     // 보낸 사람 ID
    private String receiver;   // 받는 사람 ID
    private String content;    // 메시지 내용 (최근대화 = 마지막 메시지)
    private String timestamp;  // 보낸 시간 (최근대화 = 마지막 메시지 시간)

    
    private String username;   // 상대방 이름
    private Integer age;       // 상대방 나이
    private String home;       // 상대방 거주지
    private String imageUrl;   // 상대방 프로필 이미지
    private String lastMessage;      
    private String lastMessageTime;
    private int unreadCount;


    


	public ChatDTO() {}

    public ChatDTO(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    // ----- Getter & Setter -----
    public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getHome() { return home; }
    public void setHome(String home) { this.home = home; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getLastMessageTime() {
		return lastMessageTime;
	}

	public void setLastMessageTime(String lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}
}
