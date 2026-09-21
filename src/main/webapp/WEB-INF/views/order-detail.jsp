<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container page-title-row">
    <div>
        <nav class="breadcrumb">
            <a href="${ctx}/orders">My Orders</a> &rsaquo;
            <span><c:out value="${order.orderRef}"/></span>
        </nav>
        <h1>Order <c:out value="${order.orderRef}"/></h1>
    </div>
    <span class="status-badge status-${fn:toLowerCase(order.status)}"><c:out value="${order.status}"/></span>
</div>

<div class="container">
    <c:if test="${justPlaced}">
        <div class="alert alert-success placed-banner">
            🎉 Order placed successfully! Track it below.
        </div>
    </c:if>

    <div class="order-detail-grid">
        <section class="order-detail-main">
            <h2>Items</h2>
            <div class="checkout-items">
                <c:forEach items="${order.items}" var="it">
                    <div class="checkout-item">
                        <c:choose>
                            <c:when test="${not empty it.productImage}">
                                <img src="${it.productImage}" alt="${fn:escapeXml(it.productName)}" class="cart-thumb" loading="lazy">
                            </c:when>
                            <c:otherwise>
                                <img src="${ctx}/images/pet-placeholder.svg" alt="" class="cart-thumb" loading="lazy">
                            </c:otherwise>
                        </c:choose>
                        <div>
                            <c:choose>
                                <c:when test="${it.productId > 0}">
                                    <a href="${ctx}/product?id=${it.productId}" class="cart-name"><c:out value="${it.productName}"/></a>
                                </c:when>
                                <c:otherwise>
                                    <span class="cart-name"><c:out value="${it.productName}"/></span>
                                </c:otherwise>
                            </c:choose>
                            <span class="muted">Qty <c:out value="${it.quantity}"/> &middot; <fmt:formatNumber value="${it.unitPrice}" type="currency" currencySymbol="&#8377;"/> each</span>
                            <c:if test="${canReview and it.productId > 0}">
                                <a href="${ctx}/product?id=${it.productId}#reviews" class="review-cta">Review this product &rarr;</a>
                            </c:if>
                        </div>
                        <span class="cart-line-total"><fmt:formatNumber value="${it.subtotal}" type="currency" currencySymbol="&#8377;"/></span>
                    </div>
                </c:forEach>
            </div>

            <div class="order-totals">
                <div class="summary-row"><span>Payment Method</span><span><c:out value="${order.paymentMethod}"/> (<c:out value="${order.paymentStatus}"/>)</span></div>
                <div class="summary-row"><span>Delivery</span><span>Free</span></div>
                <div class="summary-row summary-total"><span>Order Total</span><span>&#8377;<c:out value="${order.totalAmount}"/></span></div>
            </div>

            <div class="shipping-box">
                <h3>Shipping Address</h3>
                <p><c:out value="${order.shippingAddress}"/></p>
            </div>
        </section>

        <aside class="order-status-side">
            <h3>Status</h3>
            <ul class="status-timeline">
                <li class="${order.status == 'PENDING' or order.status == 'CONFIRMED' or order.status == 'SHIPPED' or order.status == 'DELIVERED' ? 'done' : ''}">Confirmed</li>
                <li class="${order.status == 'SHIPPED' or order.status == 'DELIVERED' ? 'done' : ''}">Shipped</li>
                <li class="${order.status == 'DELIVERED' ? 'done' : ''}">Delivered</li>
            </ul>
            <c:if test="${order.status == 'CANCELLED'}">
                <div class="alert alert-error">This order was cancelled.</div>
            </c:if>
            <p class="muted">Questions? Contact the seller or Lulu Mart support via the mock channels.</p>
        </aside>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>