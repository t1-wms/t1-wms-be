package com.example.wms.user.application.port.in;

import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;

public interface UserUseCase {

    void deleteUser();

    UserInfoResDto findUser();
}
