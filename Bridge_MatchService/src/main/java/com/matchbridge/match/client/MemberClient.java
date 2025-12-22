package com.matchbridge.match.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.matchbridge.match.dto.MatchDTO;
import com.matchbridge.match.dto.MyProfileDTO;
import com.matchbridge.match.dto.IdealProfileDTO;

@Component
public class MemberClient {

    private final RestTemplate rest = new RestTemplate();

    private String baseUrl() {
        return "http://member-service:8081";   // ← 변경 (도커 네트워크 DNS)
    }

    public MatchDTO getMember(String id) {
        return rest.getForObject(baseUrl() + "/api/member/" + id, MatchDTO.class);
    }

    public MyProfileDTO getMyProfile(String id) {
        return rest.getForObject(baseUrl() + "/api/member/profile/" + id, MyProfileDTO.class);
    }

    public IdealProfileDTO getMyIdeal(String id) {
        return rest.getForObject(baseUrl() + "/api/member/ideal/" + id, IdealProfileDTO.class);
    }

    public MatchDTO[] getOppositeGenderMembers(String gender) {
        return rest.getForObject(
                baseUrl() + "/api/member/opposites?gender=" + gender,
                MatchDTO[].class
        );
    }
}

