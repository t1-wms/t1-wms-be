-- notification 테이블 생성
CREATE TABLE IF NOT EXISTS notification (
                                            notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            content VARCHAR(255) NOT NULL,
    event INT NOT NULL,
    user_role VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- outbound_plan 테이블 생성
CREATE TABLE IF NOT EXISTS outbound_plan (
                                             outbound_plan_id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- 출고 계획 ID (자동 증가)
                                             plan_date DATE ,                             -- 창고에서 나가는 날짜
                                             status VARCHAR(50) ,                         -- 상태 (예: 대기, 진행 중, 완료)
    outbound_schedule_number INT ,               -- 출고 예정 번호
    outbound_schedule_date DATE ,                -- 출고 예정 날짜
    production_plan_number VARCHAR(50),          -- 생산 계획 번호
    created_at DATETIME DEFAULT NOW(), -- 현재 날짜 + 시간 자동 입력
    updated_at DATETIME DEFAULT NOW() ON UPDATE NOW() -- 수정될 때 자동 갱신
    );
