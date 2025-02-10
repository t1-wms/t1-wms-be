<<<<<<< HEAD
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
    outbound_schedule_number VARCHAR(50) ,               -- 출고 예정 번호
    outbound_schedule_date DATE ,                -- 출고 예정 날짜
    production_plan_number VARCHAR(50),          -- 생산 계획 번호
    created_at DATETIME DEFAULT NOW(), -- 현재 날짜 + 시간 자동 입력
    updated_at DATETIME DEFAULT NOW() ON UPDATE NOW() -- 수정될 때 자동 갱신
    );
=======
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    profile_image VARCHAR(255),
    staff_number VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    gender VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE,
    address VARCHAR(255),
    user_role VARCHAR(20) NOT NULL,
    birth_date DATE,
    supplier_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );
>>>>>>> 13ee619c0d24c3744e5803c4579ea65a1c2dba5a
