package com.matchbridge.auth.controller;

import com.matchbridge.auth.dto.RegistDTO;
import com.matchbridge.auth.service.AuthService;
import com.matchbridge.auth.service.S3service;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {   

    @Autowired private AuthService service;
    @Autowired private HttpSession session;   // 세션은 Redis로 공유 (과제 요건)
    @Autowired private S3service s3service;

    // =====================================================================
    // 1. (변경) 아이디 중복 체크
    // =====================================================================
    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Object>> checkId(@RequestParam String id) {
        RegistDTO member = service.userInfoById(id);
        boolean duplicate = (member != null);

      
        return ResponseEntity.ok(
                Map.of(
                        "duplicate", duplicate,
                        "message", duplicate ? "duplicate" : "available"
                )
        );
    }

    // =====================================================================
    // 2. (변경) 프로필 이미지 업로드 전용 REST API

    // =====================================================================
    @PostMapping("/profile/image")
    public ResponseEntity<?> uploadProfileImage(@RequestPart("imageFile") MultipartFile file) {
        try {
            String imageUrl = s3service.uploadFile(file);  // S3 업로드는 기존 로직 재사용

        
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "이미지 업로드 실패: " + e.getMessage()));
        }
    }

    // =====================================================================
    // 3. (핵심 변경) 회원가입 최종 처리

    // =====================================================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistDTO member) {

     
        String msg = service.registProc(member);

        if (!"회원 등록 완료".equals(msg)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", msg));
        }

        // 2) 가입 성공 → 세션 저장
        session.setAttribute("id", member.getId());
        session.setAttribute("username", member.getUsername());

        // 3) 결과 응답
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", msg,
                        "id", member.getId(),
                        "username", member.getUsername()
                ));
    }
    // =====================================================================
    // 4. (변경) 로그인 

    // =====================================================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String id = request.getId();
        String pw = request.getPw();

        String msg = service.loginProc(id, pw);

        if (!"로그인 성공".equals(msg)) {
       
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", msg));
        }

        
        RegistDTO member = service.userInfoById(id);
        if (member == null) {
          
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "회원 정보를 찾을 수 없습니다."));
        }

       
       
      

        return ResponseEntity.ok(
                Map.of(
                        "message", msg,
                        "id", member.getId(),
                        "username", member.getUsername()
                )
        );
    }

 
    public static class LoginRequest {
        private String id;
        private String pw;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getPw() { return pw; }
        public void setPw(String pw) { this.pw = pw; }
    }

    // =====================================================================
    // 5. (추가) 로그아웃 REST API (선택)
    // =====================================================================
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        session.invalidate();  
        return ResponseEntity.ok(Map.of("message", "로그아웃 완료"));
    }
    // =====================================================================
    // 5. (추가) 로그아웃 REST API (선택)
    // =====================================================================
    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        String id = (String) session.getAttribute("id");

        if (id == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "loggedIn", false,
                    "message", "세션 없음"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "loggedIn", true,
                "id", id
        ));
    }

}
