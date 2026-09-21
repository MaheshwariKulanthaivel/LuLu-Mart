# ER Diagram

```mermaid
erDiagram
    USERS ||--o{ PRODUCTS : sells
    USERS ||--o{ ORDERS : places
    ORDERS ||--|{ ORDER_ITEMS : contains
    PRODUCTS o|--o{ ORDER_ITEMS : "kept (ON DELETE SET NULL)"
    USERS ||--|| WISHLIST : owns
    WISHLIST ||--o{ WISHLIST_ITEMS : contains
    WISHLIST_ITEMS }o--|| PRODUCTS : targets
    USERS ||--o{ CART_ITEMS : has
    PRODUCTS ||--o{ CART_ITEMS : "in (ON DELETE CASCADE)"
    PRODUCTS ||--o{ REVIEWS : rated
    USERS ||--o{ REVIEWS : writes
    REVIEWS }o--|| ORDER_ITEMS : "proves purchase"

    USERS {
        bigint id PK
        varchar name
        varchar email UK
        varchar password_hash
        varchar role "BUYER|SELLER|ADMIN"
        timestamp created_at
    }
    PRODUCTS {
        bigint id PK
        bigint seller_id FK
        varchar pet_type
        varchar name
        varchar description
        decimal price
        int stock_qty
        varchar category
        varchar image_url
        timestamp created_at
    }
    ORDERS {
        bigint id PK
        varchar order_ref UK
        bigint buyer_id FK
        varchar status
        decimal total_amount
        varchar shipping_address
        varchar payment_method
        varchar payment_status
        timestamp created_at
    }
    ORDER_ITEMS {
        bigint id PK
        bigint order_id FK
        bigint product_id FK "nullable"
        int quantity
        decimal unit_price
    }
    CART_ITEMS {
        bigint id PK
        bigint user_id FK
        bigint product_id FK
        int quantity
        timestamp created_at
    }
    REVIEWS {
        bigint id PK
        bigint product_id FK
        bigint user_id FK
        bigint order_item_id FK
        int rating "1..5"
        varchar comment
        timestamp created_at
    }
    WISHLIST_ITEMS {
        bigint id PK
        bigint wishlist_id FK
        bigint product_id FK
        timestamp created_at
    }
```

Key constraints:
- `order_items.product_id` is nullable and `ON DELETE SET NULL` so deleting a product never erases order history.
- `cart_items.product_id` and `wishlist_items.product_id` are `ON DELETE CASCADE`.
- `reviews` carry a unique `(user_id, product_id, order_item_id)` so each purchased line can be reviewed at most once.