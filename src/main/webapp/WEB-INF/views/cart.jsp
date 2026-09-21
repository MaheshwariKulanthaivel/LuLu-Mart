<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container page-title-row">
    <div>
        <h1>Your Cart</h1>
        <p class="muted">Review items before checkout.</p>
    </div>
    <a href="${ctx}/shop" class="btn btn-ghost btn-sm">+ Add more items</a>
</div>

<div class="container cart-layout">
    <section class="cart-items" id="cartItems">
        <c:choose>
            <c:when test="${empty cart.items}">
                <div class="empty-state">
                    <span class="empty-emoji">🛒</span>
                    <h3>Your cart is empty</h3>
                    <p>Browse the shop and add some products for your furry friends.</p>
                    <a href="${ctx}/shop" class="btn btn-primary">Go to Shop</a>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach items="${cart.items}" var="item">
                    <div class="cart-item">
                        <img src="${item.imageUrl}" alt="${fn:escapeXml(item.productName)}" class="cart-thumb" loading="lazy">
                        <div class="cart-item-info">
                            <a href="${ctx}/product?id=${item.productId}" class="cart-name"><c:out value="${item.productName}"/></a>
                            <span class="cart-seller">by <c:out value="${item.sellerName}"/></span>
                            <span class="cart-price"><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="&#8377;"/></span>
                            <c:if test="${item.quantity > item.stockQty}">
                                <span class="cart-warn">Only <c:out value="${item.stockQty}"/> left - quantity adjusted</span>
                            </c:if>
                        </div>
                        <div class="cart-qty">
                            <div class="qty-selector">
                                <button type="button" data-cart-dec data-product-id="${item.productId}" aria-label="Decrease">&minus;</button>
                                <input type="number" value="${item.quantity}" min="1" max="${item.stockQty}" readonly data-cart-qty data-product-id="${item.productId}" aria-label="Quantity">
                                <button type="button" data-cart-inc data-product-id="${item.productId}" aria-label="Increase">+</button>
                            </div>
                        </div>
                        <div class="cart-line-total"><fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="&#8377;"/></div>
                        <button type="button" class="cart-remove" data-cart-remove="${item.productId}" aria-label="Remove item">&times;</button>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>

    <aside class="cart-summary">
        <h3>Order Summary</h3>
        <div class="summary-row"><span>Subtotal</span><span id="subtotal">
            <c:set var="sub" value="0"/>
            <c:forEach items="${cart.items}" var="i"><c:set var="sub" value="${sub + i.subtotal}"/></c:forEach>
            <fmt:formatNumber value="${sub}" type="currency" currencySymbol="&#8377;"/>
        </span></div>
        <div class="summary-row"><span>Delivery</span><span>Free</span></div>
        <div class="summary-row summary-total"><span>Total</span><span id="grandTotal"><fmt:formatNumber value="${cart.grandTotal}" type="currency" currencySymbol="&#8377;"/></span></div>
        <a href="${ctx}/checkout" class="btn btn-primary btn-block btn-lg ${empty cart.items ? 'disabled-link' : ''}">Proceed to Checkout</a>
        <p class="muted center">Secure checkout &middot; mock UPI / card / COD</p>
    </aside>
</div>

<script src="${ctx}/js/cart.js"></script>
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>