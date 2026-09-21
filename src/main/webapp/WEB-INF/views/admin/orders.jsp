<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="container page-title-row">
    <div>
        <h1>All Orders</h1>
        <p class="muted">Every order on the platform.</p>
    </div>
</div>

<div class="container">
    <c:if test="${not empty param.msg}"><div class="alert alert-success"><c:out value="${param.msg}"/></div></c:if>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="empty-state">
                <span class="empty-emoji">🧾</span>
                <h3>No orders yet</h3>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Order</th>
                        <th>Buyer</th>
                        <th>Placed</th>
                        <th>Payment</th>
                        <th>Total</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${orders}" var="o">
                        <tr>
                            <td><a href="${ctx}/admin/order?id=${o.id}" class="order-ref"><c:out value="${o.orderRef}"/></a></td>
                            <td><c:out value="${o.buyerName}"/></td>
                            <td><c:out value="${o.createdAt}"/></td>
                            <td><c:out value="${o.paymentMethod}"/> / <c:out value="${o.paymentStatus}"/></td>
                            <td>&#8377;<c:out value="${o.totalAmount}"/></td>
                            <td>
                                <form method="post" action="${ctx}/admin/orders/status" class="status-select">
                                    <input type="hidden" name="id" value="${o.id}">
                                    <select name="status" onchange="this.form.submit()">
                                        <c:forEach items="${fn:split('PENDING,CONFIRMED,SHIPPED,DELIVERED,CANCELLED', ',')}" var="st">
                                            <option value="${st}" ${o.status == st ? 'selected' : ''}><c:out value="${st}"/></option>
                                        </c:forEach>
                                    </select>
                                </form>
                            </td>
                            <td><a href="${ctx}/admin/order?id=${o.id}" class="btn btn-outline btn-xs">View</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>