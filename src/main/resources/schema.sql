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

CREATE TABLE IF NOT EXISTS bin (
                                   bin_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   bin_code VARCHAR(20) NOT NULL UNIQUE,
    zone CHAR(1) NOT NULL,
    aisle INT NOT NULL,
    row_num INT NOT NULL,
    floor INT NOT NULL,
    amount INT NOT NULL DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS product (
                         product_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
                         product_code     VARCHAR(50) NOT NULL UNIQUE,
                         product_name     VARCHAR(100) NOT NULL,
                         purchase_price   INT NOT NULL,
                         sale_price       INT NOT NULL,
                         lot_unit         INT NOT NULL,
                         supplier_id      BIGINT NOT NULL,
                         stock_lot_count  INT NOT NULL DEFAULT 0,
                         category         VARCHAR(50),
                         min_lot_count    INT NOT NULL,
                         lead_time        INT NOT NULL,
                         location_bin_code VARCHAR(50),
                         abc_grade        CHAR(1)

);

CREATE TABLE IF NOT EXISTS lot (
                     lot_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
                     lot_number   VARCHAR(50) NOT NULL,
                     product_id   BIGINT NOT NULL,
                     bin_id       BIGINT NOT NULL,
                     status       ENUM('입고', '출고중', '출고완료') NOT NULL,
                     inbound_id   BIGINT,
                     outbound_id  BIGINT,
                     created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                     updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                     CONSTRAINT FK_Lot_Product FOREIGN KEY (product_id) REFERENCES product (product_id) ON DELETE CASCADE,
                     CONSTRAINT FK_Lot_Bin FOREIGN KEY (bin_id) REFERENCES bin (bin_id) ON DELETE CASCADE
--                      CONSTRAINT FK_Lot_Inbound FOREIGN KEY (inbound_id) REFERENCES inbound (inbound_id) ON DELETE SET NULL,
--                      CONSTRAINT FK_Lot_Outbound FOREIGN KEY (outbound_id) REFERENCES outbound (outbound_id) ON DELETE SET NULL
);

