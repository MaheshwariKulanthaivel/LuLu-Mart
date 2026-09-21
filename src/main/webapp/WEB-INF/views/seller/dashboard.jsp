<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container page-title-row">
    <div>
        <h1>Seller Dashboard</h1>
        <p class="muted">Manage your shop at a glance.</p>
    </div>
    <a href="${ctx}/seller/product-form" class="btn btn-primary">+ Add Product</a>
</div>

<div class="container">
    <div class="stat-grid">
        <div class="stat-card"><span class="stat-icon">📦</span><b>${stats.totalProducts}</b><small>Products</small></div>
        <div class="stat-card"><span class="stat-icon">⚠️</span><b>${stats.lowStockProducts}</b><small>Low Stock (&le;5)</small></div>
        <div class="stat-card"><span class="stat-icon">🕒</span><b>${stats.incomingOrders}</b><small>Pending Orders</small></div>
        <div class="stat-card"><span class="stat-icon">💰</span><b><fmt:formatNumber value="${stats.totalSales}" type="currency" currencySymbol="&#8377;"/></b><small>Total Sales</small></div>
    </div>

    <div class="dash-links">
        <a href="${ctx}/seller/products" class="dash-link-card">
            <span class="dash-link-icon">🗂️</span>
            <div><b>Manage Listings</b><small>Add, edit, restock or delete your products.</small></div>
        </a>
        <a href="${ctx}/seller/orders" class="dash-link-card">
            <span class="dash-link-icon">📦</span>
            <div><b>Incoming Orders</b><small>Review orders containing your products.</small></div>
        </a>
        <a href="${ctx}/seller/product-form" class="dash-link-card">
            <span class="dash-link-icon">➕</span>
            <div><b>Add New Product</b><small>List a fresh product for pet parents.</small></div>
        </a>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>