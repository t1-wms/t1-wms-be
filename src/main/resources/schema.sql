DROP TABLE IF EXISTS notification;

CREATE TABLE notification (
                              notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              content VARCHAR(255) NOT NULL,
                              event INT NOT NULL,
                              user_role VARCHAR(50) NOT NULL,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
