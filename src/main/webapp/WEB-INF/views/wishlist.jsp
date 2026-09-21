<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container page-title-row">
    <div>
        <h1>My Wishlist</h1>
        <p class="muted">Products you saved for later.</p>
    </div>
    <a href="${ctx}/shop" class="btn btn-ghost btn-sm">+ Discover more</a>
</div>

<div class="container">
    <c:choose>
        <c:when test="${empty items}">
            <div class="empty-state">
                <span class="empty-emoji">💖</span>
                <h3>Your wishlist is empty</h3>
                <p>Tap the heart on any product to save it here.</p>
                <a href="${ctx}/shop" class="btn btn-primary">Browse Products</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="product-grid">
                <c:forEach items="${items}" var="wi">
                    <div class="product-card">
                        <div class="card-img-wrap">
                            <a href="${ctx}/product?id=${wi.productId}" class="card-img" aria-label="${fn:escapeXml(wi.productName)}">
                                <img src="${wi.imageUrl}" alt="${fn:escapeXml(wi.productName)}" loading="lazy">
                            </a>
                            <c:if test="${wi.stockQty == 0}">
                                <span class="stock-ribbon">Out of Stock</span>
                            </c:if>
                            <button type="button" class="wish-btn active" data-wishlist="${wi.productId}" data-active="true" aria-label="Remove from wishlist">
                                <svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path d="M12 21s-7.5-4.6-9.7-8.6C.7 9.4 2.2 6 5.5 5.4c2-.3 3.8.6 4.7 2.2.3.6 1.3.6 1.6 0 .9-1.6 2.7-2.5 4.7-2.2 3.3.6 4.8 4 3.2 7C19.5 16.4 12 21 12 21z" fill="currentColor" stroke="currentColor" stroke-width="1.8"/></svg>
                            </button>
                        </div>
                        <div class="card-body">
                            <div class="card-tags">
                                <span class="card-tag">${fn:escapeXml(wi.petType)}</span>
                                <span class="card-tag card-tag-muted">${fn:escapeXml(wi.category)}</span>
                            </div>
                            <a href="${ctx}/product?id=${wi.productId}" class="card-name"><c:out value="${wi.productName}"/></a>
                            <div class="card-price"><fmt:formatNumber value="${wi.price}" type="currency" currencySymbol="&#8377;"/></div>
                            <div class="card-seller">by <c:out value="${wi.sellerName}"/></div>
                            <div class="card-actions">
                                <button type="button" class="btn btn-primary btn-block js-add-cart"
                                        data-product-id="${wi.productId}"
                                        ${wi.stockQty == 0 ? 'disabled' : ''}>Add to Cart</button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>