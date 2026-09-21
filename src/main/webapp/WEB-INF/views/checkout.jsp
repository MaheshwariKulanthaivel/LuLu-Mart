<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container page-title-row">
    <div>
        <h1>Checkout</h1>
        <p class="muted">Almost there - confirm your order details.</p>
    </div>
</div>

<c:if test="${not empty param.error}">
    <div class="container"><div class="alert alert-error"><c:out value="${param.error}"/></div></div>
</c:if>

<div class="container checkout-layout">
    <section class="checkout-main">
        <h2>Order Items</h2>
        <div class="checkout-items">
            <c:forEach items="${cart.items}" var="item">
                <div class="checkout-item">
                    <img src="${item.imageUrl}" alt="${fn:escapeXml(item.productName)}" class="cart-thumb" loading="lazy">
                    <div>
                        <a href="${ctx}/product?id=${item.productId}" class="cart-name"><c:out value="${item.productName}"/></a>
                        <span class="muted"><c:out value="${item.sellerName}"/> &middot; Qty <c:out value="${item.quantity}"/></span>
                    </div>
                    <span class="cart-line-total"><fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="&#8377;"/></span>
                </div>
            </c:forEach>
        </div>

        <h2>Delivery Address</h2>
        <form id="checkoutForm" method="post" action="${ctx}/checkout" class="auth-form">
            <div class="field">
                <label for="shippingAddress">Shipping Address</label>
                <textarea id="shippingAddress" name="shippingAddress" rows="4" required
                          maxlength="500" placeholder="House/Flat, Street, Area, City, PIN"></textarea>
                <small class="muted">This address will be used for delivery.</small>
            </div>

            <h2>Payment Method</h2>
            <div class="pay-grid">
                <label class="pay-option">
                    <input type="radio" name="paymentMethod" value="UPI" checked>
                    <span class="pay-box"><b>UPI</b><small>Instant payment</small></span>
                </label>
                <label class="pay-option">
                    <input type="radio" name="paymentMethod" value="CARD">
                    <span class="pay-box"><b>Card</b><small>Credit / Debit</small></span>
                </label>
                <label class="pay-option">
                    <input type="radio" name="paymentMethod" value="COD">
                    <span class="pay-box"><b>Cash on Delivery</b><small>Pay at your door</small></span>
                </label>
            </div>
            <p class="muted pay-note">Demo store - no real money is charged. UPI/Card are treated as paid, COD is pay-on-delivery.</p>
        </form>
    </section>

    <aside class="cart-summary">
        <h3>Order Summary</h3>
        <div class="summary-row"><span>Items</span><span>${cart.totalQuantity}</span></div>
        <div class="summary-row"><span>Delivery</span><span>Free</span></div>
        <div class="summary-row summary-total"><span>Total</span><span><fmt:formatNumber value="${cart.grandTotal}" type="currency" currencySymbol="&#8377;"/></span></div>
        <button type="submit" form="checkoutForm" class="btn btn-primary btn-block btn-lg">Place Order</button>
        <a href="${ctx}/cart" class="btn btn-ghost btn-block">Back to Cart</a>
        <p class="muted center">By placing this order you agree to Lulu Mart's mock terms.</p>
    </aside>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>