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

CREATE TABLE IF NOT EXISTS outbound_plan_product (
                                                     outbound_plan_product_id BIGINT PRIMARY KEY AUTO_INCREMENT, -- 출고 계획 제품 ID (자동 증가)
                                                     outbound_plan_id BIGINT NOT NULL, -- 출고 계획 ID (outbound_plan 테이블의 외래키)
                                                     product_id BIGINT NOT NULL, -- 제품 ID
                                                     required_quantity INT NOT NULL, -- 필요 수량
                                                     stock_used_quantity INT NOT NULL, -- 재고에서 사용한 수량
                                                     order_quantity INT NOT NULL, -- 주문된 수량
                                                     status VARCHAR(50) NOT NULL, -- 계획 상태 (예: 대기, 진행 중, 완료)
                                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성 날짜
                                                     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정 날짜
                                                     FOREIGN KEY (outbound_plan_id) REFERENCES outbound_plan(outbound_plan_id), -- 외래키: outbound_plan 테이블 참조
                                                     FOREIGN KEY (product_id) REFERENCES product(product_id) -- 외래키: product 테이블 참조 (product 테이블이 있다고 가정)
);

CREATE TABLE IF NOT EXISTS product (
                                       product_id BIGINT PRIMARY KEY AUTO_INCREMENT, -- 제품 고유 ID (자동 증가)
                                       product_code VARCHAR(50) NOT NULL, -- 제품 코드
                                       product_name VARCHAR(255) NOT NULL, -- 제품 이름
                                       purchase_price INT NOT NULL, -- 구매 가격
                                       sale_price INT NOT NULL, -- 판매 가격
                                       lot_unit INT NOT NULL, -- 로트당 제품 수량
                                       supplier_id BIGINT NOT NULL, -- 공급자 ID
                                       stock_lot_count INT NOT NULL, -- 재고 로트 수량
                                       category VARCHAR(100), -- 카테고리
                                       min_lot_count VARCHAR(50), -- 최소 LOT 수량
                                       lead_time INT, -- 이 품목이 납품업체로부터 납품될 때까지 걸리는 시간
                                       location_bin_code VARCHAR(50), -- 위치 BIN 코드
                                       abc_grade CHAR(1), -- ABC 등급
                                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성 날짜
                                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정 날짜
                                       FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) -- 외래키: supplier 테이블 참조 (supplier 테이블이 있다고 가정)
);

CREATE TABLE IF NOT EXISTS supplier (
                                        supplier_id BIGINT PRIMARY KEY AUTO_INCREMENT, -- 공급자 고유 ID (자동 증가)
                                        supplier_name VARCHAR(255) NOT NULL, -- 공급자 이름
                                        business_number VARCHAR(20) NOT NULL, -- 사업자 등록 번호
                                        representative_name VARCHAR(255), -- 대표자 이름
                                        address VARCHAR(255), -- 공급자 주소
                                        supplier_phone VARCHAR(20), -- 공급자 전화번호
                                        manager_phone VARCHAR(20), -- 공급자 관리자 전화번호
                                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성 날짜
                                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 수정 날짜
);
