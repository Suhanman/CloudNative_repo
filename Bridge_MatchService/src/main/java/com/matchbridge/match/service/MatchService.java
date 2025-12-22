package com.matchbridge.match.service;

import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.matchbridge.match.dto.MatchDTO;
import com.matchbridge.match.dto.MyProfileDTO;
import com.matchbridge.match.client.MemberClient;
import com.matchbridge.match.dto.IdealProfileDTO;


@Service
public class MatchService {

    @Autowired private MemberClient memberClient;

    public List<MatchDTO> calculateMatches(String userId) {

        // 1. 내 정보
        MatchDTO me = memberClient.getMember(userId);
        if (me == null) return Collections.emptyList();

        MyProfileDTO myProfile = memberClient.getMyProfile(userId);
        IdealProfileDTO myIdeal = memberClient.getMyIdeal(userId);

        // 2. 반대 성별 후보 리스트
        MatchDTO[] array = memberClient.getOppositeGenderMembers(me.getGender());
        List<MatchDTO> candidates = Arrays.asList(array);


        List<MatchDTO> result = new ArrayList<>();

        // 3. 점수 계산
        for (MatchDTO other : candidates) {

            if (other.getId().equals(userId)) continue;

            MyProfileDTO theirProfile = memberClient.getMyProfile(other.getId());
            IdealProfileDTO theirIdeal = memberClient.getMyIdeal(other.getId());

            int score =
                calculateScore(myProfile, theirIdeal) +
                calculateScore(theirProfile, myIdeal);

            other.setScore(score);
            result.add(other);
        }

        result.sort(Comparator.comparingInt(MatchDTO::getScore).reversed());
        return result;
    }
    private int calculateScore(MyProfileDTO profile, IdealProfileDTO ideal) {
        int score = 0;

        if (safeEquals(profile.getMbti(), ideal.getIdeal_mbti()))  score++;
        if (safeEquals(profile.getSmoke(), ideal.getIdeal_smoke())) score++;
        if (safeEquals(profile.getBody(), ideal.getIdeal_body()))   score++;
        if (safeEquals(profile.getStyle(), ideal.getIdeal_style())) score++;

        return score;
    }

    /**
     * ✅ null-safe equals
     */
    private boolean safeEquals(String a, String b) {
        return a != null && b != null && a.equals(b);
    }
}


