package com.matchbridge.member.controller;

import com.matchbridge.member.dto.*;
import com.matchbridge.member.service.MemberService;
import com.matchbridge.member.service.S3service;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired private MemberService service;
    @Autowired private HttpSession session;
    @Autowired private S3service s3service;

   

    // ======================
    // 2. 내 기본 회원 정보 조회
    // ======================
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        String sessionId = (String) session.getAttribute("id");
        if (sessionId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));

        MemberDTO member = service.getMemberById(sessionId);
        return ResponseEntity.ok(member);
    }

    // ======================
    // 3. 특정 회원 조회
    // ======================
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        MemberDTO member = service.getMemberById(id);
        if (member == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "회원 정보를 찾을 수 없습니다."));

        return ResponseEntity.ok(member);
    }
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable String id) {
        MyProfileDTO profile = service.getMyProfile(id);
        if (profile == null)
            return ResponseEntity.status(404).body(Map.of("message", "프로필 없음"));

        return ResponseEntity.ok(profile);
    }
    @GetMapping("/ideal/{id}")
    public ResponseEntity<?> getIdealById(@PathVariable String id) {
        IdealProfileDTO ideal = service.getIdealProfile(id);
        if (ideal == null)
            return ResponseEntity.status(404).body(Map.of("message", "이상형 없음"));

        return ResponseEntity.ok(ideal);
    }



    // ======================
    // 4. 프로필 조회
    // ======================
    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile() {
        String id = (String) session.getAttribute("id");
        if (id == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));

        return ResponseEntity.ok(service.getMyProfile(id));
    }

    @GetMapping("/ideal")
    public ResponseEntity<?> getMyIdeal() {
        String id = (String) session.getAttribute("id");
        if (id == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));

        return ResponseEntity.ok(service.getIdealProfile(id));
    }

    // ======================
    // 5. 프로필 이미지 업로드
    // ======================
    @PostMapping("/editMember/image")
    public ResponseEntity<?> uploadImage(@RequestPart MultipartFile imageFile) {
        try {
            String url = s3service.uploadFile(imageFile);
            return ResponseEntity.ok(Map.of("imageUrl", url));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "업로드 실패"));
        }
    }

    // ======================
    // 6. 회원 정보 수정
    // ======================
    @PutMapping("/editMember")
    public ResponseEntity<?> editMember(@RequestBody MemberDTO member) {
        String sessionId = (String) session.getAttribute("id");
        if (sessionId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));

        member.setId(sessionId);
        MemberDTO existing = service.getMemberById(sessionId);

        if (member.getImageUrl() == null || member.getImageUrl().trim().isEmpty()) {
            member.setImageUrl(existing.getImageUrl());
        }

        if (member.getPw() == null || member.getPw().trim().isEmpty()) {
            member.setPw(existing.getPw());
            member.setConfirm(existing.getPw());
        }
        String msg = service.updateMember(member);

        return ResponseEntity.ok(Map.of("message", "회원 수정이 완료되었습니다!"));

    }


    // ======================
    // 7. 프로필 정보 수정
    // ======================
    @PutMapping("/editprofile")
    public ResponseEntity<?> updateMyProfile(@RequestBody MyProfileDTO my) {
        String id = (String) session.getAttribute("id");
        if (id == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));

        my.setId(id);
        boolean msg = service.updateMyProfile(my);

        return ResponseEntity.ok(Map.of("message", "프로필 수정이 완료되었습니다!"));
    }

    // ======================
    // 8. 이상형 수정
    // ======================
    @PutMapping("/editideal")
    public ResponseEntity<?> updateMyIdeal(@RequestBody IdealProfileDTO ideal) {
        String id = (String) session.getAttribute("id");
        if (id == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));

        ideal.setId(id);
        boolean msg = service.updateIdealProfile(ideal);

        return ResponseEntity.ok(Map.of("message", "이상형 수정이 완료되었습니다!"));
    }
    
 // ======================
 // 9. 반대 성별 회원 조회 (Match-Service 전용)
 // ======================
 @GetMapping("/opposites")
 public ResponseEntity<?> getOppositeGenderMembers(@RequestParam String gender) {
     return ResponseEntity.ok(service.getOppositeGenderMembers(gender));
 }

}
