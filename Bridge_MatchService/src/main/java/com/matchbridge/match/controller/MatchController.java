package com.matchbridge.match.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchbridge.match.dto.MatchDTO;
import com.matchbridge.match.service.MatchService;

import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    private MatchService matchService;


   
    @GetMapping("/card")
    public Object getMatchList(HttpSession session) {

        // ✔ 세션 ID 체크
        String myId = (String) session.getAttribute("id");
        if (myId == null) {
            return new ApiResponse(false, "NOT_LOGGED_IN", null);
        }

     
        List<MatchDTO> matches = matchService.calculateMatches(myId);

        if (matches.isEmpty()) {
        	return new ApiResponse(true, "OK", Collections.emptyList());

        }

      
        return new ApiResponse(true, "OK", matches);
    }
    public class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }

}
