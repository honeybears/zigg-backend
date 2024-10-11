-- 외래 키 제약 조건 삭제
ALTER TABLE space DROP FOREIGN KEY FKfxv38hmy559tnmdue8j4wrq15;

-- UNIQUE 인덱스 제거
ALTER TABLE space DROP INDEX UKedegj2562xkkkk1ik5qgbjk0w;

-- 외래 키 제약 조건 다시 추가 (선택 사항)
ALTER TABLE space
    ADD CONSTRAINT FKfxv38hmy559tnmdue8j4wrq15
        FOREIGN KEY (image_key_image_id) REFERENCES image (image_id);
