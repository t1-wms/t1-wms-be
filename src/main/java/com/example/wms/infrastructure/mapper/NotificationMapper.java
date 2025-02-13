package com.example.wms.infrastructure.mapper;

import com.example.wms.notification.application.domain.Notification;
import com.example.wms.user.application.domain.enums.UserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NotificationMapper {
    @Insert("""
        INSERT INTO notification
        (content, event,user_role)
        VALUES 
        (#{content},#{event},#{userRole})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "notificationId")
    void insert(Notification notification);

    @Select("""
        SELECT * 
        FROM notification
        
    """)
    Notification getAll(UserRole userRole);
}
