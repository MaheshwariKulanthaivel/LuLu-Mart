<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="en_IN"/>
<div class="product-card">
    <div class="card-img-wrap">
        <a href="${ctx}/product?id=${pc.id}" class="card-img" aria-label="${fn:escapeXml(pc.name)}">
            <img src="${pc.imageUrl}" alt="${fn:escapeXml(pc.name)}" loading="lazy">
        </a>
        <c:if test="${pc.stockQty == 0}">
            <span class="stock-ribbon">Out of Stock</span>
        </c:if>
        <button type="button"
                class="wish-btn ${wishlisted.contains(pc.id) ? 'active' : ''}"
                data-wishlist="${pc.id}"
                data-active="${wishlisted.contains(pc.id)}"
                aria-label="Add to wishlist">
            <svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path d="M12 21s-7.5-4.6-9.7-8.6C.7 9.4 2.2 6 5.5 5.4c2-.3 3.8.6 4.7 2.2.3.6 1.3.6 1.6 0 .9-1.6 2.7-2.5 4.7-2.2 3.3.6 4.8 4 3.2 7C19.5 16.4 12 21 12 21z" fill="none" stroke="currentColor" stroke-width="1.8"/></svg>
        </button>
    </div>
    <div class="card-body">
        <div class="card-tags">
            <span class="card-tag">${fn:escapeXml(pc.petType)}</span>
            <span class="card-tag card-tag-muted">${fn:escapeXml(pc.category)}</span>
        </div>
        <a href="${ctx}/product?id=${pc.id}" class="card-name"><c:out value="${pc.name}"/></a>
        <div class="card-rating">
            <span class="stars" aria-label="${pc.avgRating} out of 5">
                <c:forEach begin="1" end="5" var="i">
                    <span class="star ${pc.avgRating >= i ? 'on' : (pc.avgRating >= i - 0.5 ? 'half' : '')}">★</span>
                </c:forEach>
            </span>
            <span class="rating-num"><fmt:formatNumber value="${pc.avgRating}" maxFractionDigits="1"/></span>
            <c:if test="${pc.reviewCount > 0}">
                <span class="rating-count">(${pc.reviewCount})</span>
            </c:if>
        </div>
        <div class="card-price"><fmt:formatNumber value="${pc.price}" type="currency" currencySymbol="&#8377;"/></div>
        <div class="card-seller">by <a href="${ctx}/sellers"><c:out value="${pc.sellerName}"/></a></div>
        <div class="card-stock ${pc.inStock ? 'in-stock' : 'out-stock'}">
            ${pc.inStock ? 'In Stock' : 'Out of Stock'}
        </div>
        <div class="card-actions">
            <a href="${ctx}/product?id=${pc.id}" class="btn btn-outline btn-block btn-sm">View Details</a>
            <button type="button" class="btn btn-primary btn-block btn-sm js-add-cart"
                    data-product-id="${pc.id}"
                    ${pc.stockQty == 0 ? 'disabled' : ''}>Add to Cart</button>
        </div>
    </div>
</div>