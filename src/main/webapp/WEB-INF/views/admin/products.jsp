<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container page-title-row">
    <div>
        <h1>Moderate Listings</h1>
        <p class="muted">All products on the marketplace.</p>
    </div>
</div>

<div class="container">
    <c:if test="${not empty param.msg}"><div class="alert alert-success"><c:out value="${param.msg}"/></div></c:if>

    <div class="table-wrap">
        <table class="data-table">
            <thead>
            <tr>
                <th>Product</th>
                <th>Seller</th>
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
                            <small class="muted">avg <c:out value="${p.avgRating}"/> (${p.reviewCount})</small>
                        </div>
                    </td>
                    <td><c:out value="${p.sellerName}"/></td>
                    <td><c:out value="${p.petType}"/> / <c:out value="${p.category}"/></td>
                    <td><fmt:formatNumber value="${p.price}" type="currency" currencySymbol="&#8377;"/></td>
                    <td>${p.stockQty}</td>
                    <td>
                        <form method="post" action="${ctx}/admin/products/delete"
                              onsubmit="return confirm('Remove this listing?')">
                            <input type="hidden" name="id" value="${p.id}">
                            <button type="submit" class="btn btn-danger-outline btn-sm">Remove</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>