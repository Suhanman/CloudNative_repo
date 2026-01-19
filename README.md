# 2025 3학년 2학기 클라우드 네이티브 프로젝트

## MatchBridge
**LoveBridge 데이팅 웹 마이그레이션 프로젝트 (1인 프로젝트)**

---

## 1. 프로젝트 개요

3학년 2학기 **Cloud Native** 과목의 기말 프로젝트로 수행되었다.
본 프로젝트는 교수님이 제시한 아래 요구사항을 충족해야 한다.

### 프로젝트 요구사항
1. **MSA(Microservices Architecture)** 로 웹을 배포할 것  
2. **VMware 환경에서 컨테이너 기반**으로 배포할 것  
3. **Redis 기반 세션 공유**를 적용할 것  
4. **Nginx Reverse Proxy**를 통한 라우팅을 구성할 것  
5. **수기 시스템 구성도**를 작성할 것  

### 마이그레이션 배경
기존 멀티클라우드 기반으로 구축된 **LoveBridge**는 레거시 모델인 **JSP 기반** 구조였으며,  
웹 서비스의 컨셉과 규모 대비 **오버엔지니어링된 인프라**로 구축된 문제가 있었다.

이에 따라 LoveBridge를 **Spring Boot 기반 RESTful API 구조의 MSA**로 전환하고,  
**VMware 단일 노드**에서 **컨테이너 기반 배포**가 가능하도록 개선한 **MatchBridge** 프로젝트를 진행하였다.

---

## 2. 사용 기술(Tech Stack)

- **Backend**: Spring Boot (REST API, MSA)
- **Frontend**: HTML / CSS / JavaScript
- **Infra/DevOps**: VMware Workstation, Docker
- **Proxy / Routing**: Nginx Proxy Manager (Reverse Proxy)
- **Session Store**: Redis (Session Shared)
- **Database**: MySQL
- **Object Storage**: AWS S3 (프로필 이미지 업로드)

---

## 3. MSA 설계

### 아키텍처 개요
<img width="1356" height="927" alt="image" src="https://github.com/user-attachments/assets/ff5eeada-db46-4b86-aa92-72171974d856" />

### 컨테이너 구성
인프라 환경에서 구동되는 컨테이너는 총 7개다.

- **Frontend Web**
- **Backend Services**
  - Auth Service
  - Member Service
  - Match Service
  - Chat Service
- **Redis** (세션 저장소)
- **MySQL** (DB)

---

## 4. 서비스 구성(역할 분리)

### Auth Service
**담당: 사용자 인증 및 회원가입 / 로그인**

- 회원가입 / 로그인 / 로그아웃
- Redis 기반 세션 공유
- AWS S3 프로필 이미지 업로드

### Member Service
**담당: 회원 관리 및 정보 수정**

- 기본 회원 정보(인적사항) 조회
- 프로필(MyProfile) / 이상형(IdealProfile) 조회 및 수정
- Match/Chat 서비스에서 회원 정보가 필요할 때 **내부 API 호출**로 제공

### Match Service
**담당: 매칭 점수 계산 및 추천 카드 생성**

- 사용자 간 매칭 점수를 계산하여 추천 목록(매칭 카드) 제공
- 내부 API 통신으로 Member Service에서 정보를 받아 매칭 로직 수행

### Chat Service
**담당: 채팅(메시지/채팅방/읽음 처리)**

- 실시간 메시지 전송 및 채팅방 구성
- 읽지 않은 메시지 상태(read_status) 관리
- 기존 LoveBridge의 “좋아요” 기능을 제거하고 채팅방 기능 추가

---

## 5. REST API 명세

> 모든 API는 기본적으로 **JSON** 형식으로 통신한다.  
> Base URL은 서비스별로 분리되어 있으며 Reverse Proxy(Nginx)에서 라우팅된다.

---

### 5.1 Auth API (REST)
**Auth-Service**  
Base URL: `/api/auth`

| Method | Path | Mapping | 기능 설명 |
|---|---|---|---|
| GET | `/check-id?id={id}` | `@GetMapping("/check-id")` | 아이디 중복 체크 |
| POST | `/profile/image` | `@PostMapping("/profile/image")` | 프로필 이미지 업로드(S3) |
| POST | `/register` | `@PostMapping("/register")` | 회원가입 최종 처리(회원/프로필/이상형 저장, 세션 저장) |
| POST | `/login` | `@PostMapping("/login")` | 로그인(아이디/비밀번호 검증, 세션 저장) |
| POST | `/logout` | `@PostMapping("/logout")` | 로그아웃(세션 무효화) |
| GET | `/check-session` | `@GetMapping("/check-session")` | 세션 확인 |

---

### 5.2 Member API (REST)
Base URL: `/api/member`

| Method | Path | Mapping | 기능 설명 |
|---|---|---|---|
| GET | `/me` | `@GetMapping("/me")` | 내 기본 회원 정보 조회 |
| GET | `/{id}` | `@GetMapping("/{id}")` | 특정 회원 기본 정보 조회 |
| GET | `/profile/{id}` | `@GetMapping("/profile/{id}")` | 특정 회원 MyProfile 조회 |
| GET | `/ideal/{id}` | `@GetMapping("/ideal/{id}")` | 특정 회원 IdealProfile 조회 |
| GET | `/profile` | `@GetMapping("/profile")` | 내 MyProfile 조회 |
| GET | `/ideal` | `@GetMapping("/ideal")` | 내 IdealProfile 조회 |
| POST | `/editMember/image` | `@PostMapping("/editMember/image")` | 프로필 이미지 업로드(S3) |
| PUT | `/editMember` | `@PutMapping("/editMember")` | 내 회원 정보 수정 |
| PUT | `/editprofile` | `@PutMapping("/editprofile")` | 내 MyProfile 수정 |
| PUT | `/editideal` | `@PutMapping("/editideal")` | 내 IdealProfile 수정 |
| GET | `/opposites?gender={gender}` | `@GetMapping("/opposites")` | 반대 성별 회원 리스트 조회(Match 전용) |

---

### 5.3 Match API (REST)
Base URL: `/api/match`

| Method | Path | Mapping | 기능 설명 |
|---|---|---|---|
| GET | `/card` | `@GetMapping("/card")` | 매칭 카드 리스트 조회 |

---

### 5.4 Chat API (REST)
Base URL: `/api/chat`

| Method | Path | Mapping | 기능 설명 |
|---|---|---|---|
| GET | `/history/{myId}/{otherId}` | `@GetMapping("/history/{myId}/{otherId}")` | 채팅 내역 조회 |
| GET | `/recent` | `@GetMapping("/recent")` | 내 최근 대화 상대 목록 조회 |
| GET | `/chatroom` | `@GetMapping("/chatroom")` | 채팅방 목록(최근 대화 상대) 조회 |
| POST | `/read?otherId={otherId}` | `@PostMapping("/read")` | 내가 받은 메시지 읽음 처리 |

---

## 6. DataBase

### Member 테이블 : 회원 기본정보

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| id | VARCHAR(50) | 사용자 고유 ID (PK) |
| pw | VARCHAR(100) | 비밀번호(SHA/BCrypt 적용) |
| username | VARCHAR(50) | 닉네임 |
| mobile | VARCHAR(20) | 전화번호 |
| gender | ENUM('남성','여성') | 성별 |
| age | VARCHAR(10) | 나이 |
| imageUrl | VARCHAR(255) | 프로필 이미지(S3 링크) |
| home | VARCHAR(100) | 지역정보 |

---

### MyProfile 테이블 : 사용자 상세 프로필

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| id | VARCHAR(50) | member.id (FK) |
| mbti | VARCHAR(10) | MBTI |
| smoke | VARCHAR(10) | 흡연 여부 |
| body | VARCHAR(10) | 체형 |
| style | VARCHAR(10) | 스타일 성향 |

### idealProfile 테이블 : 이상형 프로필

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| id | VARCHAR(50) | member.id (FK) |
| ideal_mbti | VARCHAR(10) | 이상형 MBTI |
| ideal_smoke | VARCHAR(10) | 이상형 흡연 여부 |
| ideal_body | VARCHAR(10) | 이상형 체형 |
| ideal_style | VARCHAR(10) | 이상형 스타일 성향 |

---

### chat_message 테이블 : 실시간 채팅 메시지 테이블

| 컬럼명 | 타입 | 설명 |
|---|---|---|
| id | INT | 메시지 고유 PK |
| sender | VARCHAR(100) | 보낸 사용자 ID |
| reciver | VARCHAR(100) | 받은 사용자 ID |
| message | TEXT | 메시지 내용 |
| timestamp | DATETIME | 송신 시각 |
| read_status | TINYINT | 읽음 여부(0=안읽음, 1=읽음) |


## 7. 배포

```
version: "3.9"

services:

  # ------------------------
  # Nginx Proxy Manager
  # ------------------------
  myproxy:
    image: jc21/nginx-proxy-manager
    container_name: matchbridgeproxy
    restart: unless-stopped
    networks:
      - matchbridge-net
    ports:
      - "80:80"
      - "81:81"
      - "443:443"
    volumes:
      - ~/nginx-proxy-manager/data:/data
      - ~/nginx-proxy-manager/letsencrypt:/etc/letsencrypt

  # ------------------------
  # Redis
  # ------------------------
  redis:
    image: redis:latest
    container_name: matchbridgeredis
    restart: unless-stopped
    ports:
      - "6379:6379"
    command: ["redis-server", "--appendonly", "yes"]
    networks:
      - matchbridge-net

  # ------------------------
  # Frontend
  # ------------------------
  frontend:
    image: nugulsuhan/cloudnative:frontend6
    container_name: frontend
    ports:
      - "5500:80"
    networks:
      - matchbridge-net

  # ------------------------
  # Auth-Service
  # ------------------------
  auth-service:
    image: nugulsuhan/cloudnative:auth-service
    container_name: auth-service
    ports:
      - "8080:80"
    environment:
      VMWARE_IP: "192.168.108.129"
    networks:
      - matchbridge-net

  # ------------------------
  # Member-Service
  # ------------------------
  member-service:
    image: nugulsuhan/cloudnative:member-service2
    container_name: member-service
    ports:
      - "8081:80"
    environment:
      VMWARE_IP: "192.168.108.129"
    networks:
      - matchbridge-net

  # ------------------------
  # Match-Service
  # ------------------------
  match-service:
    image: nugulsuhan/cloudnative:match-service8
    container_name: match-service
    ports:
      - "8082:80"
    environment:
      VMWARE_IP: "192.168.108.129"
    networks:
      - matchbridge-net

  # ------------------------
  # Chat-Service
  # ------------------------
  chat-service:
    image: nugulsuhan/cloudnative:chat-service
    container_name: chat-service
    ports:
      - "8083:80"
    environment:
      VMWARE_IP: "192.168.108.129"
    networks:
      - matchbridge-net

  # ------------------------
  # mysql
  # ------------------------
  mysql:
    image: mysql:8.0
    container_name: matchbridgeDB
    restart: unless-stopped
    networks:
      - matchbridge-net
    ports:
      - "33307:3306"
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: matchbridge
    command:
      [
        "--default-authentication-plugin=mysql_native_password",
        "--character-set-server=utf8mb4",
        "--collation-server=utf8mb4_unicode_ci"
      ]
    volumes:
      - mysql-data:/var/lib/mysql

# ------------------------
# Networks
# ------------------------
networks:
  matchbridge-net:
    driver: bridge

# ------------------------
# Volumes
# ------------------------
volumes:
  mysql-data:
```




