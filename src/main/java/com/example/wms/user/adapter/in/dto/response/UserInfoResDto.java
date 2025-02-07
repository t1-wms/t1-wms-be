package com.example.wms.user.adapter.in.dto.response;

import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResDto {

    private Long userId;        // 사용자 고유 ID
    private String name;        // 사용자 이름
    private String profileImage; // 프로필 이미지 URL
    private String staffNumber; // 사번
    private String phone; // 전화번호
    private String gender;      // 성별
    private Boolean isActive;   // 활성화 여부
    private String address;     // 주소
    private String userRole;    // 사용자 유형 (예: 일반 사용자, 관리자)
    private String birthDate;   // 사용자 생년월일

    public static UserInfoResDto entityToResDto(User user) {
        return UserInfoResDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .staffNumber(user.getStaffNumber())
                .phone(user.getPhone())
                .gender(user.getGender())
                .isActive(user.getIsActive())
                .address(user.getAddress())
                .birthDate(user.getBirthDate())
                .userRole(user.getUserRole().getValue())
                .build();
    }
}
