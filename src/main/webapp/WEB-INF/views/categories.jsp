<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<section class="hero hero-small">
    <div class="container hero-inner">
        <div class="hero-copy">
            <h1>Shop by Category</h1>
            <p class="hero-tagline">From daily meals to bedtime comfort.</p>
        </div>
    </div>
</section>

<div class="container section">
    <div class="category-grid big">
        <a href="${ctx}/shop?category=Food" class="cat-card cat-card-big"><span>🥣</span><b>Food &amp; Nutrition</b></a>
        <a href="${ctx}/shop?category=Treats" class="cat-card cat-card-big"><span>🍖</span><b>Treats</b></a>
        <a href="${ctx}/shop?category=Toys" class="cat-card cat-card-big"><span>🎾</span><b>Toys</b></a>
        <a href="${ctx}/shop?category=Accessories" class="cat-card cat-card-big"><span>🎒</span><b>Accessories</b></a>
        <a href="${ctx}/shop?category=Grooming" class="cat-card cat-card-big"><span>🧼</span><b>Grooming</b></a>
        <a href="${ctx}/shop?category=Care" class="cat-card cat-card-big"><span>🛁</span><b>Care</b></a>
        <a href="${ctx}/shop?category=Beds%20%26%20Comfort" class="cat-card cat-card-big"><span>🛏️</span><b>Beds &amp; Comfort</b></a>
        <a href="${ctx}/shop?category=Cages%20%2F%20Habitats" class="cat-card cat-card-big"><span>🏠</span><b>Cages &amp; Habitats</b></a>
        <a href="${ctx}/shop?category=Cleaning" class="cat-card cat-card-big"><span>🧹</span><b>Cleaning</b></a>
        <a href="${ctx}/shop?category=Training" class="cat-card cat-card-big"><span>🎓</span><b>Training</b></a>
        <a href="${ctx}/shop?category=Health%20%26%20Wellness" class="cat-card cat-card-big"><span>💊</span><b>Health &amp; Wellness</b></a>
        <a href="${ctx}/shop?category=Clothing%20%2F%20Pet%20Wear" class="cat-card cat-card-big"><span>🧥</span><b>Pet Wear</b></a>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>