-- ============================================================
-- LULU MART - H2 Database Schema
-- Multi-seller pet products e-commerce marketplace
-- ============================================================

CREATE TABLE IF NOT EXISTS users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100)  NOT NULL,
    email         VARCHAR(150)  NOT NULL UNIQUE,
    password_hash VARCHAR(100)  NOT NULL,
    role          VARCHAR(20)   NOT NULL,
    created_at    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_users_role CHECK (role IN ('BUYER', 'SELLER', 'ADMIN'))
);

CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

CREATE TABLE IF NOT EXISTS products (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id    BIGINT         NOT NULL,
    pet_type     VARCHAR(50)    NOT NULL,
    name         VARCHAR(150)   NOT NULL,
    description  VARCHAR(2000)  NOT NULL,
    price        DECIMAL(10,2)  NOT NULL,
    stock_qty    INT            NOT NULL,
    category     VARCHAR(50)    NOT NULL,
    image_url    VARCHAR(500),
    created_at   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT  fk_products_seller FOREIGN KEY (seller_id) REFERENCES users(id),
    CONSTRAINT  chk_products_price CHECK (price >= 0),
    CONSTRAINT  chk_products_stock CHECK (stock_qty >= 0)
);

CREATE INDEX IF NOT EXISTS idx_products_pet ON products(pet_type);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_products_seller ON products(seller_id);
CREATE INDEX IF NOT EXISTS idx_products_name ON products(name);

CREATE TABLE IF NOT EXISTS orders (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_ref         VARCHAR(20)   NOT NULL UNIQUE,
    buyer_id          BIGINT        NOT NULL,
    status            VARCHAR(20)   NOT NULL,
    total_amount      DECIMAL(10,2) NOT NULL,
    shipping_address  VARCHAR(500)  NOT NULL,
    payment_method    VARCHAR(20)   NOT NULL,
    payment_status    VARCHAR(20)   NOT NULL,
    created_at        TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT  fk_orders_buyer FOREIGN KEY (buyer_id) REFERENCES users(id),
    CONSTRAINT  chk_orders_status CHECK (status IN ('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED')),
    CONSTRAINT  chk_orders_payment_method CHECK (payment_method IN ('UPI','CARD','COD')),
    CONSTRAINT  chk_orders_payment_status CHECK (payment_status IN ('PENDING','PAID','FAILED','REFUNDED'))
);

CREATE INDEX IF NOT EXISTS idx_orders_buyer ON orders(buyer_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_created ON orders(created_at);

CREATE TABLE IF NOT EXISTS order_items (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT        NOT NULL,
    product_id BIGINT,
    quantity   INT           NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL,
    CONSTRAINT chk_order_items_qty CHECK (quantity > 0)
);

CREATE INDEX IF NOT EXISTS idx_order_items_order ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product ON order_items(product_id);

CREATE TABLE IF NOT EXISTS cart_items (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT        NOT NULL,
    product_id BIGINT        NOT NULL,
    quantity   INT           NOT NULL,
    created_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT uq_cart_user_product UNIQUE (user_id, product_id),
    CONSTRAINT chk_cart_qty CHECK (quantity > 0)
);

CREATE INDEX IF NOT EXISTS idx_cart_user ON cart_items(user_id);

CREATE TABLE IF NOT EXISTS reviews (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id    BIGINT         NOT NULL,
    user_id       BIGINT         NOT NULL,
    order_item_id BIGINT         NOT NULL,
    rating        INT            NOT NULL,
    comment       VARCHAR(1000),
    created_at    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_reviews_order_item FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE,
    CONSTRAINT uq_review UNIQUE (user_id, product_id, order_item_id),
    CONSTRAINT chk_reviews_rating CHECK (rating BETWEEN 1 AND 5)
);

CREATE INDEX IF NOT EXISTS idx_reviews_product ON reviews(product_id);
CREATE INDEX IF NOT EXISTS idx_reviews_user ON reviews(user_id);

CREATE TABLE IF NOT EXISTS wishlist (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT       NOT NULL UNIQUE,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS wishlist_items (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    wishlist_id  BIGINT      NOT NULL,
    product_id   BIGINT      NOT NULL,
    created_at   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wishlist_items_wishlist FOREIGN KEY (wishlist_id) REFERENCES wishlist(id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_items_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT uq_wishlist_item UNIQUE (wishlist_id, product_id)
);

CREATE INDEX IF NOT EXISTS idx_wishlist_items_wishlist ON wishlist_items(wishlist_id);