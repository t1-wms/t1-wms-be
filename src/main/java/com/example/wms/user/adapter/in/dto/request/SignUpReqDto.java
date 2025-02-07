package com.example.wms.user.adapter.in.dto.request;

import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class SignUpReqDto {

    private String staffNumber; //랜덤생성해줄지 ?
    private String password; //기본 비밀번호 설정
    private String name;
    private String phone;
    private String address;
    private String gender;
    private String birthDate;
    private Long supplierId; //공급업체 여부


    public User dtoToEntity() {
        return User.builder()
                .staffNumber(staffNumber)
                .password(password)
                .name(name)
                .phone(phone)
                .userRole(UserRole.ROLE_USER)
                .gender(gender)
                .birthDate(birthDate)
                .supplierId(supplierId)
                .build();
    }
}
