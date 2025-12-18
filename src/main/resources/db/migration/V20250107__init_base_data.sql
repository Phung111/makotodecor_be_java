-- =============================================
-- Migration: 20250107_init_base_data
-- Description: Initialize base database schema with tables and foreign keys
-- =============================================

-- Create ENUM types
CREATE TYPE role AS ENUM ('USER', 'STAFF', 'ADMIN');
CREATE TYPE order_status AS ENUM (
  'NEW',
  'PENDING_DEPOSIT',
  'DEPOSITED',
  'PAID',
  'PROCESSING',
  'CANCELLED',
  'COMPLETED'
);
CREATE TYPE product_status AS ENUM ('ACTIVE', 'INACTIVE', 'OUT_OF_STOCK', 'DISCONTINUED');
CREATE TYPE category_status AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE img_type_status AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE user_status AS ENUM ('ACTIVE', 'INACTIVE');

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(255),
    status user_status NOT NULL DEFAULT 'ACTIVE',
    role role NOT NULL DEFAULT 'USER',
    cart_id BIGINT,
    order_id BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);

-- Create access_counts table
CREATE TABLE access_counts (
    id BIGSERIAL PRIMARY KEY,
    count BIGINT NOT NULL
);

-- Create categories table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    status category_status NOT NULL DEFAULT 'ACTIVE',
    img_id BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    updated_by BIGINT
);

-- Create products table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    discount BIGINT NOT NULL,
    sold BIGINT NOT NULL,
    base_sold BIGINT NOT NULL,
    status product_status NOT NULL DEFAULT 'ACTIVE',
    category_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by BIGINT
);

-- Create colors table
CREATE TABLE colors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL,
    img_id BIGINT,
    product_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create sizes table
CREATE TABLE sizes (
    id BIGSERIAL PRIMARY KEY,
    size VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create img_types table
CREATE TABLE img_types (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    status img_type_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    updated_by BIGINT
);

-- Create imgs table
CREATE TABLE imgs (
    id BIGSERIAL PRIMARY KEY,
    priority BIGINT NOT NULL,
    url VARCHAR(255) NOT NULL,
    public_id VARCHAR(255),
    title VARCHAR(255),
    subtitle VARCHAR(255),
    is_default BOOLEAN DEFAULT FALSE,
    product_id BIGINT,
    img_type_id BIGINT,
    order_group_id BIGINT,
    order_item_id BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);

-- Add comments for imgs order relations
COMMENT ON COLUMN imgs.order_group_id IS 'Reference to order_group for product images snapshot';
COMMENT ON COLUMN imgs.order_item_id IS 'Reference to order_item for variant images snapshot';

-- Create carts table
CREATE TABLE carts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL
);

-- Create cart_items table
CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    price BIGINT NOT NULL,
    discount BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    size_id BIGINT,
    color_id BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);

-- Create orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    status order_status NOT NULL DEFAULT 'NEW',
    shipping_full_name VARCHAR(255),
    shipping_phone VARCHAR(50),
    shipping_address TEXT,
    shipping_note TEXT,
    shipping_facebook_link VARCHAR(500),
    payment_proof_url VARCHAR(500),
    payment_proof_public_id VARCHAR(255),
    total_price BIGINT,
    product_count BIGINT,
    total_quantity BIGINT,
    deposit_amount BIGINT,
    remaining_amount BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    updated_by BIGINT
);

-- Create order_items table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    price BIGINT NOT NULL,
    discount BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    color_name VARCHAR(100),
    size_name VARCHAR(50),
    size_price BIGINT,
    order_group_id BIGINT
);

-- Add comments for order_items snapshot fields
COMMENT ON COLUMN order_items.color_name IS 'Color name at time of order (snapshot)';
COMMENT ON COLUMN order_items.size_name IS 'Size name at time of order (snapshot)';
COMMENT ON COLUMN order_items.size_price IS 'Size price at time of order (snapshot)';

-- Create order_groups table to group variants by product within an order
CREATE TABLE order_groups (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    total_quantity BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- Add Foreign Key Constraints
-- =============================================

-- Products to Categories
ALTER TABLE products 
ADD CONSTRAINT fk_products_category 
FOREIGN KEY (category_id) REFERENCES categories(id);

-- Colors to Products
ALTER TABLE colors 
ADD CONSTRAINT fk_colors_product 
FOREIGN KEY (product_id) REFERENCES products(id);

-- Colors to Imgs
ALTER TABLE colors 
ADD CONSTRAINT fk_colors_img 
FOREIGN KEY (img_id) REFERENCES imgs(id);

-- Categories to Imgs
ALTER TABLE categories
ADD CONSTRAINT fk_categories_img
FOREIGN KEY (img_id) REFERENCES imgs(id);

-- Sizes to Products
ALTER TABLE sizes 
ADD CONSTRAINT fk_sizes_product 
FOREIGN KEY (product_id) REFERENCES products(id);

-- Imgs to Products
ALTER TABLE imgs 
ADD CONSTRAINT fk_imgs_product 
FOREIGN KEY (product_id) REFERENCES products(id);

-- Imgs to ImgTypes
ALTER TABLE imgs 
ADD CONSTRAINT fk_imgs_img_type 
FOREIGN KEY (img_type_id) REFERENCES img_types(id);

-- Imgs to OrderGroups
ALTER TABLE imgs
ADD CONSTRAINT fk_imgs_order_group
FOREIGN KEY (order_group_id) REFERENCES order_groups(id) ON DELETE CASCADE;

-- Imgs to OrderItems
ALTER TABLE imgs
ADD CONSTRAINT fk_imgs_order_item
FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE;

-- Carts to Users
ALTER TABLE carts 
ADD CONSTRAINT fk_carts_user 
FOREIGN KEY (user_id) REFERENCES users(id);

-- Users to Carts (bidirectional)
ALTER TABLE users 
ADD CONSTRAINT fk_users_cart 
FOREIGN KEY (cart_id) REFERENCES carts(id);

-- Cart Items to Carts
ALTER TABLE cart_items 
ADD CONSTRAINT fk_cart_items_cart 
FOREIGN KEY (cart_id) REFERENCES carts(id);

-- Cart Items to Products
ALTER TABLE cart_items 
ADD CONSTRAINT fk_cart_items_product 
FOREIGN KEY (product_id) REFERENCES products(id);

-- Cart Items to Sizes
ALTER TABLE cart_items 
ADD CONSTRAINT fk_cart_items_size 
FOREIGN KEY (size_id) REFERENCES sizes(id);

-- Cart Items to Colors
ALTER TABLE cart_items 
ADD CONSTRAINT fk_cart_items_color 
FOREIGN KEY (color_id) REFERENCES colors(id);

-- Orders to Users
ALTER TABLE orders 
ADD CONSTRAINT fk_orders_user 
FOREIGN KEY (user_id) REFERENCES users(id);

-- Users to Orders (bidirectional)
ALTER TABLE users 
ADD CONSTRAINT fk_users_order 
FOREIGN KEY (order_id) REFERENCES orders(id);

-- Order Items to Orders
ALTER TABLE order_items 
ADD CONSTRAINT fk_order_items_order 
FOREIGN KEY (order_id) REFERENCES orders(id);

-- Order Items to Products
ALTER TABLE order_items 
ADD CONSTRAINT fk_order_items_product 
FOREIGN KEY (product_id) REFERENCES products(id);

-- Order Groups to Orders
ALTER TABLE order_groups
ADD CONSTRAINT fk_order_groups_order
FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;

-- Order Groups to Products
ALTER TABLE order_groups
ADD CONSTRAINT fk_order_groups_product
FOREIGN KEY (product_id) REFERENCES products(id);

-- Order Items to Order Groups
ALTER TABLE order_items
ADD CONSTRAINT fk_order_items_order_group
FOREIGN KEY (order_group_id) REFERENCES order_groups(id) ON DELETE CASCADE;

CREATE INDEX idx_imgs_img_type_id ON imgs(img_type_id);

CREATE INDEX idx_imgs_product_id ON imgs(product_id);

CREATE INDEX idx_imgs_img_type_product ON imgs(img_type_id, product_id);
