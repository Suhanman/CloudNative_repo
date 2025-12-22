package com.matchbridge.auth.mapper;

import java.util.ArrayList;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import com.matchbridge.auth.dto.RegistDTO;

@Mapper
public interface AuthMemberMapper {
	
	int registProc(RegistDTO member);

	RegistDTO login(String id);


	 int insertMyProfile(RegistDTO profile);
	 
	 int insertIdealProfile(RegistDTO ideal);

	    // ✅ 이상형 프로필 등록 (idealprofile 테이블)

}
