<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container page-title-row">
    <div>
        <h1>My Products</h1>
        <p class="muted">Everything you sell on Lulu Mart.</p>
    </div>
    <a href="${ctx}/seller/product-form" class="btn btn-primary">+ Add Product</a>
</div>

<div class="container">
    <c:if test="${not empty param.msg}"><div class="alert alert-success"><c:out value="${param.msg}"/></div></c:if>
    <c:if test="${not empty param.error}"><div class="alert alert-error"><c:out value="${param.error}"/></div></c:if>

    <c:choose>
        <c:when test="${empty products}">
            <div class="empty-state">
                <span class="empty-emoji">🗂️</span>
                <h3>No products yet</h3>
                <p>Add your first product to start selling on Lulu Mart.</p>
                <a href="${ctx}/seller/product-form" class="btn btn-primary">Add Product</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Product</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${products}" var="p">
                        <tr>
                            <td class="td-product">
                                <img src="${p.imageUrl}" alt="" class="td-thumb" loading="lazy">
                                <div>
                                    <a href="${ctx}/product?id=${p.id}" class="cart-name"><c:out value="${p.name}"/></a>
                                    <small class="muted">${p.petType} &middot; avg <c:out value="${p.avgRating}"/> (${p.reviewCount})</small>
                                </div>
                            </td>
                            <td><c:out value="${p.category}"/></td>
                            <td><fmt:formatNumber value="${p.price}" type="currency" currencySymbol="&#8377;"/></td>
                            <td>
                                <form method="post" action="${ctx}/seller/stock" class="stock-form">
                                    <input type="hidden" name="id" value="${p.id}">
                                    <input type="number" name="stockQty" value="${p.stockQty}" min="0" max="9999" required>
                                    <button type="submit" class="btn btn-ghost btn-xs">Update</button>
                                </form>
                            </td>
                            <td class="td-actions">
                                <a href="${ctx}/seller/product-form?id=${p.id}" class="btn btn-outline btn-sm">Edit</a>
                                <form method="post" action="${ctx}/seller/delete" onsubmit="return confirm('Delete this product?')">
                                    <input type="hidden" name="id" value="${p.id}">
                                    <button type="submit" class="btn btn-danger-outline btn-sm">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>