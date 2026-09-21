<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container page-title-row">
    <div>
        <h1>Admin Dashboard</h1>
        <p class="muted">Platform overview and moderation.</p>
    </div>
</div>

<div class="container">
    <c:if test="${not empty param.error}"><div class="alert alert-error"><c:out value="${param.error}"/></div></c:if>

    <div class="stat-grid">
        <div class="stat-card"><span class="stat-icon">👥</span><b>${stats.totalUsers}</b><small>Total Users</small></div>
        <div class="stat-card"><span class="stat-icon">🛍️</span><b>${stats.totalBuyers}</b><small>Buyers</small></div>
        <div class="stat-card"><span class="stat-icon">🏪</span><b>${stats.totalSellers}</b><small>Sellers</small></div>
        <div class="stat-card"><span class="stat-icon">📦</span><b>${stats.totalProducts}</b><small>Products</small></div>
        <div class="stat-card"><span class="stat-icon">🧾</span><b>${stats.totalOrders}</b><small>Orders</small></div>
        <div class="stat-card"><span class="stat-icon">💰</span><b><fmt:formatNumber value="${stats.totalSales}" type="currency" currencySymbol="&#8377;"/></b><small>Total Sales</small></div>
    </div>

    <div class="dash-links">
        <a href="${ctx}/admin/users" class="dash-link-card">
            <span class="dash-link-icon">👥</span>
            <div><b>Manage Users</b><small>View and remove buyer/seller accounts.</small></div>
        </a>
        <a href="${ctx}/admin/products" class="dash-link-card">
            <span class="dash-link-icon">🗂️</span>
            <div><b>Moderate Listings</b><small>Review and remove marketplace products.</small></div>
        </a>
        <a href="${ctx}/admin/orders" class="dash-link-card">
            <span class="dash-link-icon">🧾</span>
            <div><b>All Orders</b><small>Track and update order statuses platform-wide.</small></div>
        </a>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>