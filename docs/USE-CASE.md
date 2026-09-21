# Use Cases

```mermaid
flowchart TB
    subgraph Guests
        G1[Browse / search products]
        G2[View product details]
        G3[Register]
        G4[Login]
    end

    subgraph Buyers
        B1[Manage cart] --> B2[Checkout UPI / Card / COD]
        B2 --> B3[Track order status]
        B3 --> B4[Review purchased item after delivery]
        B5[Manage wishlist]
    end

    subgraph Sellers
        S1[List new product]
        S2[Edit / delete own product]
        S3[Update stock]
        S4[View incoming orders]
    end

    subgraph Admin
        A1[View platform dashboard]
        A2[Manage users - remove non-admin]
        A3[Moderate listings - remove products]
        A4[Set order status PENDING->CONFIRMED->SHIPPED->DELIVERED/CANCELLED]
    end

    G1 --> B1
    G3 --> B1
    B1 --> B5
```

## Key business rules
- Only authenticated **buyers** can cart, checkout, wishlist, or review.
- A buyer may review a product **only via a delivered (SHIPPED/DELIVERED) order item they haven't reviewed yet**.
- Sellers may only create/edit/delete/restock **their own** products.
- Admin users are protected from deletion; only non-admin accounts can be removed.
- Cart quantity cannot exceed available stock; checkout decreases stock atomically and clears the cart.