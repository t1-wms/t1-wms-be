package com.example.wms.infrastructure.mapper;

import com.example.wms.user.application.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {

    // 로그인 시 사용자 정보 조회
    User findUserForLogin(@Param("staffNumber") String staffNumber, @Param("password") String password);

}
