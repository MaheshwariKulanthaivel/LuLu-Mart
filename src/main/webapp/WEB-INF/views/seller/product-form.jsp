<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="container page-title-row">
    <div>
        <h1>${empty product ? 'Add Product' : 'Edit Product'}</h1>
        <p class="muted">${empty product ? 'List a new product on Lulu Mart.' : 'Update the details of your product.'}</p>
    </div>
    <a href="${ctx}/seller/products" class="btn btn-ghost btn-sm">&larr; Back to Products</a>
</div>

<div class="container">
    <div class="form-card auth-card">
        <form method="post" action="${ctx}/seller/product-form" class="auth-form">
            <c:if test="${not empty product}">
                <input type="hidden" name="id" value="${product.id}">
            </c:if>

            <div class="form-grid-2">
                <div class="field">
                    <label for="name">Product Name</label>
                    <input type="text" id="name" name="name" required maxlength="150" placeholder="e.g. Grain-Free Chicken Dog Food 3kg"
                           value="${fn:escapeXml(product.name)}"/>
                </div>
                <div class="field">
                    <label for="petType">Pet Type</label>
                    <select id="petType" name="petType" required>
                        <c:forEach items="${petTypes}" var="pt">
                            <option value="${pt}" ${product != null and pt == product.petType ? 'selected' : ''}><c:out value="${pt}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="field">
                    <label for="category">Category</label>
                    <select id="category" name="category" required>
                        <c:forEach items="${categories}" var="cat">
                            <option value="${cat}" ${product != null and cat == product.category ? 'selected' : ''}><c:out value="${cat}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-grid-2">
                    <div class="field">
                        <label for="price">Price (&#8377;)</label>
                        <input type="number" id="price" name="price" required min="1" max="9999999" step="0.01"
                               value="${product.price}" placeholder="499.00"/>
                    </div>
                    <div class="field">
                        <label for="stockQty">Stock Quantity</label>
                        <input type="number" id="stockQty" name="stockQty" required min="0" max="9999"
                               value="${product.stockQty}" placeholder="100"/>
                    </div>
                </div>
            </div>

            <div class="field">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="5" required maxlength="2000"
                          placeholder="Describe the product, materials, sizes and usage..."><c:out value="${product.description}"/></textarea>
            </div>

            <div class="field">
                <label for="imageUrl">Image URL</label>
                <input type="url" id="imageUrl" name="imageUrl" placeholder="https://images.unsplash.com/photo-...?w=640&q=80"
                       value="${fn:escapeXml(product.imageUrl)}"/>
                <small class="muted">Use an https image link. Leave empty to use a placeholder.</small>
            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg">${empty product ? 'Add Product' : 'Save Changes'}</button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>