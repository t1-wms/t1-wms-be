package com.example.wms.user.application.port.out;

import com.example.wms.user.application.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserQueryPort {

    // 사번으로 사용자가 존재하는지 확인
    boolean existsByStaffNumber(String staffNumber);

    // 사번으로 사용자 정보 조회
    Optional<User> findByStaffNumber(String staffNumber);

    // 사용자 ID로 사용자 정보 조회
    Optional<User> findById(Long userId);

    // 사용자 ID로 사용자가 존재하는지 확인
    boolean existsById(Long userId);

    // 모든 사용자 정보 조회
    List<User> findAllUsers();
}
