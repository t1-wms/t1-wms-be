package com.example.wms.notification.application.port.in;

import com.example.wms.notification.application.domain.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationUseCase {
    SseEmitter connect();
    void send(Notification notification);
}
