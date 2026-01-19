# 2025 3학년 2학기 클라우드 네이티브 프로젝트

MatchBridge

데이팅웹 LoveBrideg 마이그레이션 프로젝트 (1인 프로젝트)

프로젝트 개요 
3학년 2학기 CloudNative의 기말 프로젝트로 제출된 본 프로젝트는 교수가 제시한 아래의 조건을 따라야한다.

1. MSA로 웹을 배포할 것.
2. Vmware 에서 컨테이너 기반으로 배포할 것.
3. Redis를 통한 세션을 공유할 것.
4. Nginx를 통한 Reverse Proxy를 이용한 라우팅할 것.
5. 수기로 된 시스템 구성도를 작성할 것.

기존의 멀티클라우드 기반의 데이팅 웹으로 구축되었던 LoveBridge는 레거시 모델인 JSP 방식으로 코딩되어 있었고,
웹의 컨셉과 규모에 비해 지나치게 오버엔지니어링된 인프라가 구축된 문제가 있었다.
따라서 프로젝트 LoveBridge를 Spring Boot 기반의 Restful API 구조의 MSA로 전환 및 서비스 개선, Vmware 단일 노드에서 컨테이너 기반으로 배포하는
MatchBridge 프로젝트를 진행하였다.


사용 기술 

Springboot, HTML, CSS, JS, Vmware Workstation, Docker, Redis, Nginx Proxy Manager, AWS s3


1. MSA 설계

<img width="1356" height="927" alt="image" src="https://github.com/user-attachments/assets/ff5eeada-db46-4b86-aa92-72171974d856" />

시스템 구성도

인프라환경에서 돌아가게 되는 컨테이너는 총 7개로 다음과 같다.

프론트웹 / 백엔드 서버 (auth / member / chat / match) / Redis(세션저장소) / Mysql(DB)

Auth-service

담당 : 사용자 인증 및 회원가입 / 로그인

Auth-Service는 본 프로젝트의 전체 시스템의 인증과 회원가입, 로그인, 로그아웃, S3로의 프로필 이미지 업로드를 담당한다.

Member-Service

담당 : 회원 관리 및 정보 수정

Member-Service는 기본 회원 정보(인적사항, 프로필, 이상형)을 조회하고 회원 정보 업데이트를 담당하는 서비스이다.
MatchService 혹은 ChatService에서 회원정보가 필요할 시에는 MemberService에서 API 요청을 통하여 정보를 받아온다.

Match-Service 

담당 : 매칭 점수계산 및 매칭

사용자에게 보여줄 매칭카드 목록을 생성하는 서비스로, 사용자 간의 매칭 점수 계산을 통하여 추천을 제공한다.
Client를 통한 내부 API 통신을 통하여 MemberService에서 받아와 매칭 카드를 띄우게 된다.

Chat-Service

담당: Web-Socket 기반 채팅

실시간 메시지 전송, 채팅방, 읽지 않은 메시지 관리 등등을 담당한다.
기존 LoveBridge에서 좋아요 기능을 제거하고 채팅방 기능을 추가하였다. 

3. REST API 명세

Json 형태로 받아온다.

## Auth API 명세 (REST)

**Auth-Service**

Base URL: `/api/auth`

| Method | Path | Mapping | 기능 설명 |
|---|---|---|---|
| GET | `/check-id?id={id}` | `@GetMapping("/check-id")` | 아이디 중복 체크  |
| POST | `/profile/image` | `@PostMapping("/profile/image")` | 프로필 이미지 업로드(S3) |
| POST | `/register` | `@PostMapping("/register")` | 회원가입 최종 처리 — 회원/내프로필/이상형프로필 저장 후 성공 시 세션 저장 및 201 응답 |
| POST | `/login` | `@PostMapping("/login")` | 로그인 — 아이디/비밀번호 검증 후 성공 시 세션 저장 |
| POST | `/logout` | `@PostMapping("/logout")` | 로그아웃 — 세션 무효화 |
| GET | `/check-session` | `@GetMapping("/check-session")` | 세션 확인  |



## Member API 명세 (REST)

Base URL: `/api/member`

| Method | Path | Mapping | 기능 설명 |
|---|---|---|---|
| GET | `/me` | `@GetMapping("/me")` | 내 기본 회원 정보 조회 |
| GET | `/{id}` | `@GetMapping("/{id}")` | 특정 회원 기본 정보 조회  |
| GET | `/profile/{id}` | `@GetMapping("/profile/{id}")` | 특정 회원의 **MyProfile** 조회  |
| GET | `/ideal/{id}` | `@GetMapping("/ideal/{id}")` | 특정 회원의 **IdealProfile** 조회  |
| GET | `/profile` | `@GetMapping("/profile")` | 내 **MyProfile** 조회  |
| GET | `/ideal` | `@GetMapping("/ideal")` | 내 **IdealProfile** 조회  |
| POST | `/editMember/image` | `@PostMapping("/editMember/image")` | 프로필 이미지 업로드(S3) |
| PUT | `/editMember` | `@PutMapping("/editMember")` | 내 회원 정보 수정  |
| PUT | `/editprofile` | `@PutMapping("/editprofile")` | 내 **MyProfile** 수정  |
| PUT | `/editideal` | `@PutMapping("/editideal")` | 내 **IdealProfile** 수정  |
| GET | `/opposites?gender={gender}` | `@GetMapping("/opposites")` | 반대 성별 회원 리스트 조회 |


## Match API 명세 (REST)

Base URL: `/api/match`

| Method | Path | Mapping | 기능 설명 |
|---|---|---|---|
| GET | `/card` | `@GetMapping("/card")` | 매칭 카드 리스트 조회 |

## Chat API 명세 (REST)

Base URL: `/api/chat`

| Method | Path | Mapping | 기능 설명 |
|---|---|---|---|
| GET | `/history/{myId}/{otherId}` | `@GetMapping("/history/{myId}/{otherId}")` | **채팅 내역 조회**  |
| GET | `/recent` | `@GetMapping("/recent")` | 내 **최근 대화 상대 목록 조회**  |
| GET | `/chatroom` | `@GetMapping("/chatroom")` | 채팅방 목록(최근 대화 상대) 조회 |
| POST | `/read?otherId={otherId}` | `@PostMapping("/read")` |  **내가 받은 메시지 읽음 처리**  |



5. ERD
6. 배포

