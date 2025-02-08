package com.example.wms.notification.application.domain;


import com.example.wms.user.application.domain.enums.UserRole;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification {
    private Long notificationId;

    private String content;

    private Integer event;

    private UserRole userRole;
}
