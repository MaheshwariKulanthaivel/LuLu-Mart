# LULU MART

A complete Java full-stack **multi-seller pet products e-commerce marketplace** (demo) — buyers shop food, toys, beds, cages and more for dogs, cats, birds, rabbits, hamsters, guinea pigs, fish, turtles and reptiles; sellers list and restock products; admins moderate the platform.

Built from scratch with **Java 17, Java Servlets (javax, Tomcat 9), JSP + JSTL, H2, HikariCP, Gson, jQuery-free vanilla JS, JUnit 5 + Mockito**.

---

## Quick Start

Requirements: **JDK 17** and **Maven 3.9+**.

```bash
mvn clean verify     # build + run tests
mvn clean package    # produce target/lulu-mart.war
```

Deploy `target/lulu-mart.war` into a Tomcat 9 `webapps/` folder and open `http://localhost:8080/lulu-mart/`.

Alternatively run standalone:

```bash
mvn org.codehaus.cargo:cargo-maven3-plugin:run -Dcargo.maven.containerId=tomcat9x -Dcargo.servlet.port=8080
```

On first startup an `AppListener` creates the database at `~/.lulumart-data/` (H2 file `lulumart.mv.db`) from `schema.sql` + `seed.sql` and injects real BCrypt hashes for the demo accounts.

### Demo accounts

| Role   | Email                  | Password    |
|--------|------------------------|-------------|
| Buyer  | `buyer@lulumart.com`   | `Buyer@123` |
| Seller | `seller@lulumart.com`  | `Seller@123`|
| Admin  | `admin@lulumart.com`   | `Admin@123` |

All seeded sellers use `Seller@123`, all seeded buyers `Buyer@123`.

> This is a fictitious academic marketplace. No real orders or payments are processed.

---

## Features

### Buyers
- Browse/search/filter by keyword, pet type, category, price range and availability; sort results
- Product detail pages with rating, reviews, related products and a quantity picker
- Cart (add, change quantity, remove), wishlist, checkout with **UPI / Card / COD** (mock)
- Order history + order tracking with status timeline (Pending → Confirmed → Shipped → Delivered / Cancelled)
- Post reviews **only after** the related order item is delivered, once per item

### Sellers
- Dashboard with product / low-stock / pending-order / sales stats
- Create, edit, delete and restock own products (enforced ownership)
- Incoming orders containing own products with order detail view

### Admins
- Platform dashboard (users, buyers, sellers, products, orders, sales)
- User management (view/remove non-admin accounts), product moderation (remove listings), order status updates
- Cannot delete the admin account / admin users are protected

### REST JSON APIs (`/api/v1/*`)
`/auth` (login/logout/me), `/products` (search/list/featured/bestsellers/new-arrivals), `/cart`, `/orders`, `/reviews`, `/wishlist`, `/seller/*`, `/admin/*` — all auth-protected by role, return `{success, message, data}`.

---

## Tech Stack

| Area        | Choice                                        |
|-------------|-----------------------------------------------|
| Language    | Java 17 (`maven.compiler.release=17`)         |
| Web         | javax Servlet API 4.0.1, JSP + JSTL 1.2        |
| Server      | Apache Tomcat 9 (no embedded server)          |
| Database    | H2 2.2.224 (file mode at runtime, memory mode in tests) |
| Pooling     | HikariCP 5.1.0                                |
| JSON        | Gson 2.10.1                                   |
| Password    | jBCrypt 0.4                                   |
| Logging     | SLF4J 2.0.13 + Logback 1.4.14                  |
| Testing     | JUnit 5.10.2 + Mockito 5.11.0                  |
| Frontend    | JSP + vanilla JS/CSS (no frameworks)          |

---

## Project Layout

```
lulu-mart/
├── pom.xml
├── .github/workflows/ci.yml
├── docs/                        # diagrams & docs (Mermaid)
├── src/main/
│   ├── java/com/lulumart/
│   │   ├── controller/          # page + API servlets
│   │   ├── service/             # business logic
│   │   ├── dao/                 # JDBC data access
│   │   ├── dto/                 # views + search requests
│   │   ├── model/               # entities
│   │   ├── exception/           # AppException hierarchy
│   │   ├── filter/              # Encoding + Auth role filters
│   │   ├── listener/            # AppListener (DB bootstrap)
│   │   └── util/                # DbUtil, DatabaseSeeder, WebUtil...
│   ├── resources/               # schema.sql, seed.sql, db.properties, logback.xml
│   └── webapp/
│       ├── css/ js/ images/
│       └── WEB-INF/
│           ├── web.xml
│           └── views/           # all JSP view pages
└── src/test/java/               # unit + integration tests
```

## Docs
- [Architecture](docs/ARCHITECTURE.md)
- [ER Diagram](docs/ER-DIAGRAM.md)
- [Use Cases](docs/USE-CASE.md)
- [Sequence Diagrams](docs/SEQUENCE-DIAGRAM.md)

## Security notes
- BCrypt password hashing, session ID regeneration on login, HttpOnly cookies
- PreparedStatement-only SQL access
- Role checks in `AuthFilter` **and** at service layer (seller ownership, buyer-only reviews)
- XSS escaping via JSTL `c:out` on all user-controlled output
- Friendly error pages for 404 / 403 / 500

Made with love for pets everywhere. 🐾