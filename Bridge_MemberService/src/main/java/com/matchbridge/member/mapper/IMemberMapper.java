package com.matchbridge.member.mapper;

import java.util.ArrayList;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.matchbridge.member.dto.IdealProfileDTO;
import com.matchbridge.member.dto.MemberDTO;
import com.matchbridge.member.dto.MyProfileDTO;

@Mapper
public interface IMemberMapper {

	
	int totalCount(@Param("select")String select, @Param("search")String search);

	int updateProc(MemberDTO member);

	ArrayList<MemberDTO> memberInfo(@Param("begin")int begin, @Param("end")int end,
			@Param("select")String select, @Param("search")String search);
	

    
    // ✅ 나의 프로필 조회
    MyProfileDTO getMyProfileById(@Param("id") String id);

    // ✅ 이상형 프로필 조회
    IdealProfileDTO getIdealProfileById(@Param("id") String id);

	List<IdealProfileDTO> getAllIdealProfiles();
	
	MemberDTO userInfoById(String id);
	
	List<MemberDTO> getOppositeGenderMembers(@Param("gender") String gender);
	
	int updateMyProfile(MyProfileDTO my);
    int updateIdealProfile(IdealProfileDTO ideal);


}












