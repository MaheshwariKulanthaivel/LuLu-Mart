<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="authUser" value="${sessionScope.authUser}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${empty pageTitle ? 'Lulu Mart - Everything Your Pet Loves, Delivered.' : pageTitle}"/> | Lulu Mart</title>
    <meta name="description" content="Lulu Mart - shop trusted pet essentials from multiple sellers. Food, toys, grooming, beds and more for dogs, cats, birds and small pets.">
    <link rel="icon" href="${ctx}/images/favicon.svg" type="image/svg+xml">
    <link rel="stylesheet" href="${ctx}/css/lulumart.css">
</head>
<body>
<c:if test="${not empty errorMessage}">
    <div class="toast toast-error" id="toastError">
        <span><c:out value="${errorMessage}"/></span>
        <button class="toast-close" onclick="this.parentElement.classList.remove('show')">&times;</button>
    </div>
</c:if>
<c:if test="${not empty successMessage}">
    <div class="toast toast-success" id="toastSuccess">
        <span><c:out value="${successMessage}"/></span>
        <button class="toast-close" onclick="this.parentElement.classList.remove('show')">&times;</button>
    </div>
</c:if>

<header class="site-header">
    <div class="topbar">
        <div class="container topbar-inner">
            <span>🐾 <c:out value="${fn:escapeXml('Everything Your Pet Loves, Delivered.')}"/></span>
            <span class="topbar-demo">Demo: buyer@lulumart.com &middot; seller@lulumart.com &middot; admin@lulumart.com</span>
        </div>
    </div>
    <nav class="navbar" aria-label="Main navigation">
        <div class="container nav-inner">
            <a href="${ctx}/home" class="logo" aria-label="Lulu Mart home">
                <svg class="logo-paw" viewBox="0 0 24 24" width="28" height="28" aria-hidden="true">
                    <g fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round">
                        <ellipse cx="6.5" cy="9.5" rx="2.4" ry="3.2" transform="rotate(-30 6.5 9.5)"/>
                        <ellipse cx="17.5" cy="9.5" rx="2.4" ry="3.2" transform="rotate(30 17.5 9.5)"/>
                        <ellipse cx="4.2" cy="16" rx="2.7" ry="3.4" transform="rotate(-40 4.2 16)"/>
                        <ellipse cx="19.8" cy="16" rx="2.7" ry="3.4" transform="rotate(40 19.8 16)"/>
                        <ellipse cx="12" cy="17.5" rx="3.2" ry="3.8"/>
                    </g>
                </svg>
                <span class="logo-text">LULU <b>MART</b></span>
            </a>

            <button class="nav-toggle" id="navToggle" aria-label="Toggle menu">
                <span></span><span></span><span></span>
            </button>

            <ul class="nav-links" id="navLinks">
                <li><a href="${ctx}/home" class="${pageActive == 'home' ? 'active' : ''}">Home</a></li>
                <li><a href="${ctx}/shop" class="${pageActive == 'shop' ? 'active' : ''}">Shop</a></li>
                <li><a href="${ctx}/pets" class="${pageActive == 'pets' ? 'active' : ''}">Pets</a></li>
                <li><a href="${ctx}/categories" class="${pageActive == 'categories' ? 'active' : ''}">Categories</a></li>
                <li><a href="${ctx}/sellers" class="${pageActive == 'sellers' ? 'active' : ''}">Sellers</a></li>
                <li><a href="${ctx}/about" class="${pageActive == 'about' ? 'active' : ''}">About</a></li>
            </ul>

            <form class="nav-search" action="${ctx}/shop" method="get" role="search">
                <svg viewBox="0 0 24 24" width="17" height="17" aria-hidden="true"><circle cx="11" cy="11" r="7" fill="none" stroke="currentColor" stroke-width="2"/><line x1="16.5" y1="16.5" x2="21" y2="21" stroke="currentColor" stroke-width="2" stroke-linecap="round"/></svg>
                <input type="search" name="q" placeholder="Search dog food, toys, cages..." value="${fn:escapeXml(fq)}" aria-label="Search products">
            </form>

            <div class="nav-actions">
                <a href="${ctx}/wishlist" class="icon-link" title="Wishlist" aria-label="Wishlist">
                    <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true"><path d="M12 21s-7.5-4.6-9.7-8.6C.7 9.4 2.2 6 5.5 5.4c2-.3 3.8.6 4.7 2.2.3.6 1.3.6 1.6 0 .9-1.6 2.7-2.5 4.7-2.2 3.3.6 4.8 4 3.2 7C19.5 16.4 12 21 12 21z" fill="none" stroke="currentColor" stroke-width="1.8"/></svg>
                </a>
                <a href="${ctx}/cart" class="icon-link" title="Cart" aria-label="Cart">
                    <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true"><circle cx="9" cy="20" r="1.6" fill="currentColor"/><circle cx="17" cy="20" r="1.6" fill="currentColor"/><path d="M3 4h2l2.2 11.2a1.5 1.5 0 001.5 1.3h8.6a1.5 1.5 0 001.5-1.2L20.5 8H6" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/></svg>
                    <span class="badge" id="cartBadge" data-authed="${authUser != null and authUser.buyer}" style="display:none">0</span>
                </a>

                <c:choose>
                    <c:when test="${authUser == null}">
                        <a href="${ctx}/auth/login" class="btn btn-ghost btn-sm">Login</a>
                        <a href="${ctx}/auth/register" class="btn btn-primary btn-sm">Sign Up</a>
                    </c:when>
                    <c:otherwise>
                        <div class="user-menu">
                            <button class="user-chip" type="button" aria-expanded="false">
                                <span class="user-avatar"><c:out value="${fn:substring(authUser.name, 0, 1)}"/></span>
                                <span class="user-name"><c:out value="${authUser.name}"/></span>
                            </button>
                            <div class="user-dropdown">
                                <c:choose>
                                    <c:when test="${authUser.buyer}">
                                        <a href="${ctx}/orders">My Orders</a>
                                        <a href="${ctx}/wishlist">Wishlist</a>
                                        <a href="${ctx}/cart">Cart</a>
                                        <a href="${ctx}/profile">Profile</a>
                                    </c:when>
                                    <c:when test="${authUser.seller}">
                                        <a href="${ctx}/seller/dashboard">Seller Dashboard</a>
                                        <a href="${ctx}/seller/products">My Products</a>
                                        <a href="${ctx}/seller/orders">Orders</a>
                                    </c:when>
                                    <c:when test="${authUser.admin}">
                                        <a href="${ctx}/admin/dashboard">Admin Dashboard</a>
                                        <a href="${ctx}/admin/users">Users</a>
                                        <a href="${ctx}/admin/products">Moderation</a>
                                    </c:when>
                                </c:choose>
                                <form action="${ctx}/auth/logout" method="post">
                                    <button type="submit" class="dropdown-logout">Logout</button>
                                </form>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>
</header>

<main class="page-wrap">