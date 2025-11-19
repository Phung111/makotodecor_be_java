-- =====================================================
-- Migration: 20250108_insert_base_data
-- Description: Insert initial base data (img_types, images, categories)
-- =====================================================

-- Insert ImgTypes (DEFAULT, OTHER, DETAIL, COLOR)
INSERT INTO img_types (name, code, status, created_at) VALUES
('Mặc định', 'DEFAULT', 'ACTIVE', CURRENT_TIMESTAMP),
('Khác', 'OTHER', 'ACTIVE', CURRENT_TIMESTAMP),
('Chi tiết', 'DETAIL', 'ACTIVE', CURRENT_TIMESTAMP),
('Màu sắc', 'COLOR', 'ACTIVE', CURRENT_TIMESTAMP);

-- Insert Images for 5 main categories
INSERT INTO imgs (priority, url, public_id, title, subtitle, is_default, img_type_id, created_at) VALUES
(1, 'https://example.com/images/rem-tranh-noren.jpg', 'rem-tranh-noren', 'Rèm – Tranh Noren', 'Category image', TRUE, 1, CURRENT_TIMESTAMP),
(2, 'https://example.com/images/co-nobori-yatai.jpg', 'co-nobori-yatai', 'Cờ – Nobori – Yatai', 'Category image', TRUE, 1, CURRENT_TIMESTAMP),
(3, 'https://example.com/images/ema-the-go-bang-ten.jpg', 'ema-the-go-bang-ten', 'Ema – Thẻ Gỗ – Bảng Tên', 'Category image', TRUE, 1, CURRENT_TIMESTAMP),
(4, 'https://example.com/images/trang-tri-izakaya.jpg', 'trang-tri-izakaya', 'Trang Trí Izakaya', 'Category image', TRUE, 1, CURRENT_TIMESTAMP),
(5, 'https://example.com/images/decor-truyen-thong-nhat.jpg', 'decor-truyen-thong-nhat', 'Decor Truyền Thống Nhật', 'Category image', TRUE, 1, CURRENT_TIMESTAMP);

-- Insert 5 main categories
INSERT INTO categories (code, name, status, img_id, created_at, updated_at) VALUES
('REM', 'Rèm – Tranh Noren', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CO', 'Cờ – Nobori – Yatai', 'ACTIVE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('THE_GO', 'Ema – Thẻ Gỗ – Bảng Tên', 'ACTIVE', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('IZAKAYA', 'Trang Trí Izakaya', 'ACTIVE', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DECOR_TRADITIONAL', 'Decor Truyền Thống Nhật', 'ACTIVE', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
