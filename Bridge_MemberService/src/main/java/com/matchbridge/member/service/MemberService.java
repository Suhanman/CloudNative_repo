package com.matchbridge.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.matchbridge.member.dto.IdealProfileDTO;
import com.matchbridge.member.dto.MemberDTO;
import com.matchbridge.member.dto.MyProfileDTO;
import com.matchbridge.member.mapper.IMemberMapper;

@Service
public class MemberService {

    @Autowired private IMemberMapper mapper;

    // ----------- SELECT -----------
    public MemberDTO getMemberById(String id) {
        return mapper.userInfoById(id);
    }

    public MyProfileDTO getMyProfile(String id) {
        return mapper.getMyProfileById(id);
    }

    public IdealProfileDTO getIdealProfile(String id) {
        return mapper.getIdealProfileById(id);
    }

    // ----------- UPDATE -----------
    public String updateMember(MemberDTO member) {
        MemberDTO existing = mapper.userInfoById(member.getId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (member.getPw() == null || member.getPw().trim().isEmpty()) {
            member.setPw(existing.getPw());
        } else {
            if (!member.getPw().equals(member.getConfirm())) {
                return "비밀번호 불일치";
            }
            if (!member.getPw().startsWith("$2a$")) {
                member.setPw(encoder.encode(member.getPw()));
            }
        }

        int result = mapper.updateProc(member);
        return result == 1 ? "OK" : "FAIL";
    }

    public boolean updateMyProfile(MyProfileDTO dto) {
        return mapper.updateMyProfile(dto) == 1;
    }

    public boolean updateIdealProfile(IdealProfileDTO dto) {
        return mapper.updateIdealProfile(dto) == 1;
    }
    
    public List<MemberDTO> getOppositeGenderMembers(String gender) {
        return mapper.getOppositeGenderMembers(gender);
    }

}
