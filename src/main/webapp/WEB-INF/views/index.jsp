<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<section class="hero">
    <div class="container hero-inner">
        <div class="hero-copy">
            <h1>LULU MART</h1>
            <p class="hero-tagline">Everything Your Pet Loves, Delivered.</p>
            <p class="hero-sub">Shop trusted pet essentials from multiple sellers, all in one place.</p>
            <div class="hero-actions">
                <a href="${ctx}/pets" class="btn btn-secondary btn-lg">SHOP FOR PETS</a>
                <a href="${ctx}/shop" class="btn btn-outline-light btn-lg">EXPLORE PRODUCTS</a>
            </div>
        </div>
        <div class="hero-art" aria-hidden="true">
            <div class="hero-art-card hero-art-a" style="animation-delay:0s">
                <img src="https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=400&q=80&auto=format&fit=crop" alt="">
            </div>
            <div class="hero-art-card hero-art-b" style="animation-delay:.6s">
                <img src="https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=400&q=80&auto=format&fit=crop" alt="">
            </div>
            <div class="hero-art-card hero-art-c" style="animation-delay:1.2s">
                <img src="https://images.unsplash.com/photo-1444464666168-49d633b86797?w=400&q=80&auto=format&fit=crop" alt="">
            </div>
        </div>
    </div>
</section>

<section class="container section">
    <div class="section-head">
        <h2>Shop by Pet</h2>
        <a href="${ctx}/pets" class="link-all">See all pets &rarr;</a>
    </div>
    <div class="pet-grid">
        <a href="${ctx}/shop?petType=Dogs" class="pet-card"><span class="pet-emoji">🐶</span><b>Dogs</b></a>
        <a href="${ctx}/shop?petType=Cats" class="pet-card"><span class="pet-emoji">🐱</span><b>Cats</b></a>
        <a href="${ctx}/shop?petType=Birds" class="pet-card"><span class="pet-emoji">🐦</span><b>Birds</b></a>
        <a href="${ctx}/shop?petType=Rabbits" class="pet-card"><span class="pet-emoji">🐰</span><b>Rabbits</b></a>
        <a href="${ctx}/shop?petType=Hamsters" class="pet-card"><span class="pet-emoji">🐹</span><b>Hamsters</b></a>
        <a href="${ctx}/shop?petType=Fish" class="pet-card"><span class="pet-emoji">🐠</span><b>Fish</b></a>
        <a href="${ctx}/shop?petType=Turtles" class="pet-card"><span class="pet-emoji">🐢</span><b>Turtles</b></a>
        <a href="${ctx}/shop?petType=Guinea%20Pigs" class="pet-card"><span class="pet-emoji">🐹</span><b>Guinea Pigs</b></a>
        <a href="${ctx}/shop?petType=Reptiles" class="pet-card"><span class="pet-emoji">🦎</span><b>Reptiles</b></a>
        <a href="${ctx}/shop?petType=Other" class="pet-card"><span class="pet-emoji">🐾</span><b>Small Pets</b></a>
    </div>
</section>

<section class="section alt-section">
    <div class="container">
        <div class="section-head">
            <h2>Shop by Category</h2>
            <a href="${ctx}/categories" class="link-all">All categories &rarr;</a>
        </div>
        <div class="category-grid">
            <a href="${ctx}/shop?category=Food" class="cat-card"><span>🥣</span><b>Food &amp; Nutrition</b></a>
            <a href="${ctx}/shop?category=Treats" class="cat-card"><span>🍖</span><b>Treats</b></a>
            <a href="${ctx}/shop?category=Toys" class="cat-card"><span>🎾</span><b>Toys</b></a>
            <a href="${ctx}/shop?category=Accessories" class="cat-card"><span>🎒</span><b>Accessories</b></a>
            <a href="${ctx}/shop?category=Grooming" class="cat-card"><span>🧼</span><b>Grooming</b></a>
            <a href="${ctx}/shop?category=Care" class="cat-card"><span>🛁</span><b>Care</b></a>
            <a href="${ctx}/shop?category=Beds%20%26%20Comfort" class="cat-card"><span>🛏️</span><b>Beds &amp; Comfort</b></a>
            <a href="${ctx}/shop?category=Cages%20%2F%20Habitats" class="cat-card"><span>🏠</span><b>Cages &amp; Habitats</b></a>
            <a href="${ctx}/shop?category=Cleaning" class="cat-card"><span>🧹</span><b>Cleaning</b></a>
            <a href="${ctx}/shop?category=Training" class="cat-card"><span>🎓</span><b>Training</b></a>
            <a href="${ctx}/shop?category=Health%20%26%20Wellness" class="cat-card"><span>💊</span><b>Health &amp; Wellness</b></a>
            <a href="${ctx}/shop?category=Clothing%20%2F%20Pet%20Wear" class="cat-card"><span>🧥</span><b>Pet Wear</b></a>
        </div>
    </div>
</section>

<section class="container section">
    <div class="section-head">
        <h2>Featured Products</h2>
        <a href="${ctx}/shop" class="link-all">View all &rarr;</a>
    </div>
    <div class="product-grid">
        <c:forEach items="${featured}" var="pc" varStatus="st">
            <%@ include file="/WEB-INF/views/includes/product-card.jsp" %>
        </c:forEach>
    </div>
</section>

<section class="section alt-section">
    <div class="container">
        <div class="section-head">
            <h2>Best Sellers</h2>
            <a href="${ctx}/shop" class="link-all">View all &rarr;</a>
        </div>
        <div class="product-grid">
            <c:forEach items="${bestSellers}" var="pc" varStatus="st">
                <%@ include file="/WEB-INF/views/includes/product-card.jsp" %>
            </c:forEach>
        </div>
    </div>
</section>

<section class="container section">
    <div class="section-head">
        <h2>New Arrivals</h2>
        <a href="${ctx}/shop?sort=NEWEST" class="link-all">View all &rarr;</a>
    </div>
    <div class="product-grid">
        <c:forEach items="${newArrivals}" var="pc" varStatus="st">
            <%@ include file="/WEB-INF/views/includes/product-card.jsp" %>
        </c:forEach>
    </div>
</section>

<section class="section alt-section">
    <div class="container">
        <div class="section-head">
            <h2>Trusted Sellers</h2>
            <a href="${ctx}/sellers" class="link-all">All sellers &rarr;</a>
        </div>
        <div class="seller-strip">
            <c:forEach items="${topSellers}" var="ts">
                <a href="${ctx}/shop?seller=${ts.id}" class="seller-chip">
                    <span class="seller-chip-avatar"><c:out value="${fn:substring(ts.name, 0, 1)}"/></span>
                    <span>
                        <b><c:out value="${ts.name}"/></b>
                        <small>${ts.productCount} products</small>
                    </span>
                </a>
            </c:forEach>
        </div>
    </div>
</section>

<section class="container section">
    <div class="section-head">
        <h2>Why Lulu Mart?</h2>
    </div>
    <div class="why-grid">
        <div class="why-card"><span class="why-icon">🐾</span><h3>Wide range of pet essentials</h3><p>Food, toys, grooming, bedding, habitats and wellness for every pet family member.</p></div>
        <div class="why-card"><span class="why-icon">🤝</span><h3>Multiple trusted sellers</h3><p>Real sellers list real products. Ratings and reviews keep quality high.</p></div>
        <div class="why-card"><span class="why-icon">🛒</span><h3>Easy shopping</h3><p>Search, filter and compare pet products in seconds with a simple cart and checkout.</p></div>
        <div class="why-card"><span class="why-icon">🔒</span><h3>Secure checkout</h3><p>Mock UPI, card and cash-on-delivery payments with protected sessions.</p></div>
        <div class="why-card"><span class="why-icon">📦</span><h3>Simple order tracking</h3><p>Follow every order from pending to delivered, right from your account.</p></div>
    </div>
</section>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>