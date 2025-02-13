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
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) -- 외래키: supplier 테이블 참조
    );