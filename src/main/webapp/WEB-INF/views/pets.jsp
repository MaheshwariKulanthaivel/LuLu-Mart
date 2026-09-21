<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<section class="hero hero-small">
    <div class="container hero-inner">
        <div class="hero-copy">
            <h1>Shop by Pet</h1>
            <p class="hero-tagline">Whatever pet you call family, we've got them covered.</p>
        </div>
    </div>
</section>

<div class="container section">
    <div class="pet-grid big">
        <a href="${ctx}/shop?petType=Dogs" class="pet-card pet-card-big"><span class="pet-emoji">🐶</span><b>Dogs</b><small>Food, toys, training &amp; more</small></a>
        <a href="${ctx}/shop?petType=Cats" class="pet-card pet-card-big"><span class="pet-emoji">🐱</span><b>Cats</b><small>Food, scratching &amp; comfort</small></a>
        <a href="${ctx}/shop?petType=Birds" class="pet-card pet-card-big"><span class="pet-emoji">🐦</span><b>Birds</b><small>Cages, feed &amp; perches</small></a>
        <a href="${ctx}/shop?petType=Rabbits" class="pet-card pet-card-big"><span class="pet-emoji">🐰</span><b>Rabbits</b><small>Hay, homes &amp; care</small></a>
        <a href="${ctx}/shop?petType=Hamsters" class="pet-card pet-card-big"><span class="pet-emoji">🐹</span><b>Hamsters</b><small>Bedding, wheels &amp; treats</small></a>
        <a href="${ctx}/shop?petType=Guinea%20Pigs" class="pet-card pet-card-big"><span class="pet-emoji">🐾</span><b>Guinea Pigs</b><small>Habitat, food &amp; fun</small></a>
        <a href="${ctx}/shop?petType=Fish" class="pet-card pet-card-big"><span class="pet-emoji">🐠</span><b>Fish</b><small>Tanks, food &amp; decor</small></a>
        <a href="${ctx}/shop?petType=Turtles" class="pet-card pet-card-big"><span class="pet-emoji">🐢</span><b>Turtles</b><small>Basking &amp; habitat care</small></a>
        <a href="${ctx}/shop?petType=Reptiles" class="pet-card pet-card-big"><span class="pet-emoji">🦎</span><b>Reptiles</b><small>Heat, habitat &amp; feeding</small></a>
        <a href="${ctx}/shop?petType=Other" class="pet-card pet-card-big"><span class="pet-emoji">🐾</span><b>Small Pets &amp; Others</b><small>Everything else with paws</small></a>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>