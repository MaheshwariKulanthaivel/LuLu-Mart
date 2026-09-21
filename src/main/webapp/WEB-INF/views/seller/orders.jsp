<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="container page-title-row">
    <div>
        <h1>Incoming Orders</h1>
        <p class="muted">Orders that contain products from your shop.</p>
    </div>
</div>

<div class="container">
    <c:choose>
        <c:when test="${empty orders}">
            <div class="empty-state">
                <span class="empty-emoji">📦</span>
                <h3>No orders yet</h3>
                <p>Orders containing your products will show up here.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="order-list">
                <c:forEach items="${orders}" var="o">
                    <div class="order-card">
                        <div class="order-card-head">
                            <div>
                                <a href="${ctx}/seller/order?id=${o.id}" class="order-ref"><c:out value="${o.orderRef}"/></a>
                                <span class="muted">placed <c:out value="${o.createdAt}"/> by <c:out value="${o.buyerName}"/></span>
                            </div>
                            <span class="status-badge status-${fn:toLowerCase(o.status)}"><c:out value="${o.status}"/></span>
                        </div>
                        <div class="order-card-body">
                            <span class="order-meta"><c:out value="${o.items.size()}"/> item(s)</span>
                            <span class="order-meta">Payment: <c:out value="${o.paymentMethod}"/> / <c:out value="${o.paymentStatus}"/></span>
                            <span class="order-total">Total: <span>&#8377;<c:out value="${o.totalAmount}"/></span></span>
                            <a href="${ctx}/seller/order?id=${o.id}" class="btn btn-outline btn-sm">View Details</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>