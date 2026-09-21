<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="container page-title-row">
    <div>
        <h1>My Profile</h1>
        <p class="muted">Your account details.</p>
    </div>
</div>

<div class="container profile-layout">
    <div class="profile-card">
        <span class="profile-avatar-lg"><c:out value="${fn:substring(user.name, 0, 1)}"/></span>
        <h2><c:out value="${user.name}"/></h2>
        <span class="status-badge status-${fn:toLowerCase(user.role)}"><c:out value="${user.role}"/></span>
    </div>
    <div class="profile-details">
        <div class="detail-row"><span class="muted">Email</span><span><c:out value="${user.email}"/></span></div>
        <div class="detail-row"><span class="muted">Member since</span><span><c:out value="${user.createdAt}"/></span></div>
        <div class="profile-actions">
            <a href="${ctx}/orders" class="btn btn-outline">My Orders</a>
            <a href="${ctx}/wishlist" class="btn btn-outline">Wishlist</a>
            <a href="${ctx}/cart" class="btn btn-outline">Cart</a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>