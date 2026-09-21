<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container">
    <nav class="breadcrumb">
        <a href="${ctx}/home">Home</a> &rsaquo;
        <a href="${ctx}/shop?petType=${fn:escapeXml(product.petType)}"><c:out value="${product.petType}"/></a> &rsaquo;
        <a href="${ctx}/shop?category=${fn:escapeXml(product.category)}"><c:out value="${product.category}"/></a> &rsaquo;
        <span><c:out value="${product.name}"/></span>
    </nav>

    <div class="product-detail">
        <div class="pd-gallery">
            <div class="pd-image">
                <img src="${product.imageUrl}" alt="${fn:escapeXml(product.name)}" id="pdMainImage">
            </div>
            <c:if test="${product.stockQty == 0}">
                <span class="stock-ribbon stock-ribbon-lg">Out of Stock</span>
            </c:if>
        </div>

        <div class="pd-info">
            <div class="card-tags">
                <span class="card-tag">${fn:escapeXml(product.petType)}</span>
                <span class="card-tag card-tag-muted">${fn:escapeXml(product.category)}</span>
            </div>
            <h1 class="pd-name"><c:out value="${product.name}"/></h1>

            <div class="card-rating pd-rating">
                <span class="stars">
                    <c:forEach begin="1" end="5" var="i">
                        <span class="star ${avgRating >= i ? 'on' : (avgRating >= i - 0.5 ? 'half' : '')}">★</span>
                    </c:forEach>
                </span>
                <span class="rating-num"><fmt:formatNumber value="${avgRating}" maxFractionDigits="1"/></span>
                <span class="rating-count">(${reviewCount} reviews)</span>
            </div>

            <div class="pd-price"><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="&#8377;"/></div>

            <div class="pd-meta">
                <div class="pd-meta-row"><span class="muted">Seller:</span> <a href="${ctx}/sellers"><c:out value="${product.sellerName}"/></a></div>
                <div class="pd-meta-row"><span class="muted">Pet:</span> <c:out value="${product.petType}"/></div>
                <div class="pd-meta-row"><span class="muted">Type:</span> <c:out value="${product.category}"/></div>
                <div class="pd-meta-row"><span class="muted">Added:</span> <c:out value="${product.createdAt}"/></div>
                <div class="pd-meta-row ${product.inStock ? 'in-stock' : 'out-stock'}">
                    <span class="muted">Stock:</span>
                    <c:choose>
                        <c:when test="${product.inStock}"><c:out value="${product.stockQty}"/> units available</c:when>
                        <c:otherwise>Out of stock</c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="pd-actions">
                <div class="qty-selector">
                    <button type="button" data-qty="dec" aria-label="Decrease quantity">&minus;</button>
                    <input type="number" id="qtyInput" value="1" min="1" max="${product.stockQty}" readonly aria-label="Quantity">
                    <button type="button" data-qty="inc" aria-label="Increase quantity">+</button>
                </div>
                <button type="button" class="btn btn-primary btn-lg js-add-cart-detail"
                        data-product-id="${product.id}" data-max="${product.stockQty}"
                        ${product.stockQty == 0 ? 'disabled' : ''}>Add to Cart</button>
                <button type="button" class="btn btn-outline btn-lg js-wishlist-toggle"
                        data-product-id="${product.id}" data-active="${inWishlist}">
                    ${inWishlist ? '♥ In Wishlist' : 'Add to Wishlist'}
                </button>
            </div>

            <div class="pd-trust">
                <span>✓ Secure mock payment</span>
                <span>✓ Easy order tracking</span>
                <span>✓ Buyer protection</span>
            </div>
        </div>
    </div>

    <div class="pd-description">
        <h2>Product Description</h2>
        <p><c:out value="${product.description}"/></p>
    </div>

    <section class="reviews-section">
        <div class="section-head">
            <h2>Reviews (${fn:length(reviews)})</h2>
        </div>
        <div class="review-summary">
            <div class="review-score">
                <span class="big-score"><fmt:formatNumber value="${avgRating}" maxFractionDigits="1"/></span>
                <span class="stars">
                    <c:forEach begin="1" end="5" var="i">
                        <span class="star ${avgRating >= i ? 'on' : (avgRating >= i - 0.5 ? 'half' : '')}">★</span>
                    </c:forEach>
                </span>
                <span>based on ${reviewCount} reviews</span>
            </div>
        </div>

        <c:choose>
            <c:when test="${authUser != null and authUser.buyer and canReview}">
                <form id="reviewForm" class="review-form">
                    <h3>Write a review</h3>
                    <input type="hidden" value="${product.id}" id="reviewProductId">
                    <div class="rating-input">
                        <c:forEach begin="1" end="5" var="i">
                            <label class="star-input">
                                <input type="radio" name="rating" value="${i}" ${i == 5 ? 'checked' : ''}>
                                <span>★</span>
                            </label>
                        </c:forEach>
                        <span class="muted" id="ratingHint">Tap stars to rate</span>
                    </div>
                    <textarea id="reviewComment" rows="3" maxlength="1000" placeholder="Share your experience with this product..."></textarea>
                    <div class="form-row-inline">
                        <button type="submit" class="btn btn-primary">Post Review</button>
                        <span class="muted review-status" id="reviewStatus"></span>
                    </div>
                </form>
            </c:when>
            <c:when test="${authUser != null and authUser.buyer}">
                <p class="muted">You can review this product after it is delivered to you.</p>
            </c:when>
            <c:otherwise>
                <p class="muted"><a href="${ctx}/auth/login?next=${ctx}/product?id=${product.id}">Sign in</a> as a buyer to review this product.</p>
            </c:otherwise>
        </c:choose>

        <div class="review-list" id="reviewList">
            <c:forEach items="${reviews}" var="rev">
                <div class="review-card">
                    <span class="rev-avatar"><c:out value="${fn:substring(rev.reviewerName, 0, 1)}"/></span>
                    <div>
                        <div class="rev-head">
                            <b><c:out value="${rev.reviewerName}"/></b>
                            <span class="stars">
                                <c:forEach begin="1" end="5" var="i">
                                    <span class="star ${rev.rating >= i ? 'on' : ''}">★</span>
                                </c:forEach>
                            </span>
                            <span class="muted"><c:out value="${rev.createdAt}"/></span>
                        </div>
                        <p><c:out value="${rev.comment}"/></p>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty reviews}">
                <p class="muted">No reviews yet. Be the first to review this product after your order is delivered.</p>
            </c:if>
        </div>
    </section>

    <section class="container section">
        <div class="section-head">
            <h2>Related Products</h2>
        </div>
        <div class="product-grid">
            <c:forEach items="${related}" var="pc" varStatus="st">
                <%@ include file="/WEB-INF/views/includes/product-card.jsp" %>
            </c:forEach>
        </div>
    </section>
</div>

<script src="${ctx}/js/product.js"></script>
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>