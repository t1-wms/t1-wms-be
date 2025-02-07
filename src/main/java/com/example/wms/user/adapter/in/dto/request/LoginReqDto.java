package com.example.wms.user.adapter.in.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginReqDto {
    private String staffNumber;
    private String password;
}
