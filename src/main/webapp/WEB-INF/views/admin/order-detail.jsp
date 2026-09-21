<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container page-title-row">
    <div>
        <nav class="breadcrumb">
            <a href="${ctx}/admin/orders">All Orders</a> &rsaquo;
            <span><c:out value="${order.orderRef}"/></span>
        </nav>
        <h1>Order <c:out value="${order.orderRef}"/></h1>
    </div>
    <span class="status-badge status-${fn:toLowerCase(order.status)}"><c:out value="${order.status}"/></span>
</div>

<div class="container">
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
                            <span class="cart-name"><c:out value="${it.productName}"/></span>
                            <span class="muted">Qty <c:out value="${it.quantity}"/> &middot; <fmt:formatNumber value="${it.unitPrice}" type="currency" currencySymbol="&#8377;"/> each</span>
                        </div>
                        <span class="cart-line-total"><fmt:formatNumber value="${it.subtotal}" type="currency" currencySymbol="&#8377;"/></span>
                    </div>
                </c:forEach>
            </div>

            <div class="order-totals">
                <div class="summary-row"><span>Buyer</span><span><c:out value="${order.buyerName}"/></span></div>
                <div class="summary-row"><span>Payment</span><span><c:out value="${order.paymentMethod}"/> (<c:out value="${order.paymentStatus}"/>)</span></div>
                <div class="summary-row summary-total"><span>Order Total</span><span>&#8377;<c:out value="${order.totalAmount}"/></span></div>
            </div>

            <div class="shipping-box">
                <h3>Shipping Address</h3>
                <p><c:out value="${order.shippingAddress}"/></p>
            </div>
        </section>

        <aside class="order-status-side">
            <h3>Update Status</h3>
            <form method="post" action="${ctx}/admin/orders/status" class="auth-form">
                <input type="hidden" name="id" value="${order.id}">
                <div class="field">
                    <select name="status">
                        <c:forEach items="${fn:split('PENDING,CONFIRMED,SHIPPED,DELIVERED,CANCELLED', ',')}" var="st">
                            <option value="${st}" ${order.status == st ? 'selected' : ''}><c:out value="${st}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Update Status</button>
            </form>
            <p class="muted">Status updates flow to buyers and sellers automatically on page view.</p>
        </aside>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>