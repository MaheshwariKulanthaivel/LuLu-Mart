<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<fmt:setLocale value="en_IN"/>

<div class="container page-title-row">
    <div>
        <h1>Shop Pet Products</h1>
        <p class="muted">Browse food, toys, beds and more for every pet.</p>
    </div>
    <c:if test="${not empty fq}">
        <a href="${ctx}/shop" class="btn btn-ghost btn-sm">Clear search &times;</a>
    </c:if>
</div>

<c:if test="${not empty fseller}">
    <div class="container">
        <div class="filter-chip-row">
            <span class="chip chip-active">Seller shop &middot; <a href="${ctx}/shop" class="chip-clear">&times; clear</a></span>
        </div>
    </div>
</c:if>

<div class="container shop-layout">
    <aside class="filter-side">
        <form id="filterForm" class="filter-form" action="${ctx}/shop" method="get">
            <c:if test="${not empty fseller}">
                <input type="hidden" name="seller" value="${fn:escapeXml(fseller)}">
            </c:if>
            <div class="filter-group">
                <label for="fq">Search</label>
                <input type="search" id="fq" name="q" value="${fn:escapeXml(fq)}" placeholder="dog food, toys...">
            </div>
            <div class="filter-group">
                <label for="fpet">Pet Type</label>
                <select id="fpet" name="petType">
                    <option value="">All pets</option>
                    <c:forEach items="${petTypes}" var="pt">
                        <option value="${pt}" ${pt == fpet ? 'selected' : ''}><c:out value="${pt}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-group">
                <label for="fcat">Category</label>
                <select id="fcat" name="category">
                    <option value="">All categories</option>
                    <c:forEach items="${categories}" var="cat">
                        <option value="${cat}" ${cat == fcat ? 'selected' : ''}><c:out value="${cat}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-group">
                <label>Price Range (&#8377;)</label>
                <div class="price-row">
                    <input type="number" id="fmin" name="minPrice" min="0" step="1" placeholder="Min" value="${fn:escapeXml(fmin)}">
                    <input type="number" id="fmax" name="maxPrice" min="0" step="1" placeholder="Max" value="${fn:escapeXml(fmax)}">
                </div>
            </div>
            <div class="filter-group">
                <label>Availability</label>
                <select id="favail" name="availability">
                    <option value="">Any</option>
                    <option value="IN_STOCK" ${favail == 'IN_STOCK' ? 'selected' : ''}>In Stock</option>
                    <option value="OUT_OF_STOCK" ${favail == 'OUT_OF_STOCK' ? 'selected' : ''}>Out of Stock</option>
                </select>
            </div>
            <div class="filter-group">
                <label>Sort By</label>
                <select id="fsort" name="sort">
                    <option value="NEWEST" ${fsort == 'NEWEST' || empty fsort ? 'selected' : ''}>Newest</option>
                    <option value="PRICE_ASC" ${fsort == 'PRICE_ASC' ? 'selected' : ''}>Price: Low to High</option>
                    <option value="PRICE_DESC" ${fsort == 'PRICE_DESC' ? 'selected' : ''}>Price: High to Low</option>
                    <option value="NAME_ASC" ${fsort == 'NAME_ASC' ? 'selected' : ''}>Name A &rarr; Z</option>
                </select>
            </div>
            <div class="filter-actions">
                <button type="submit" class="btn btn-primary btn-block">Apply Filters</button>
                <a href="${ctx}/shop" class="btn btn-ghost btn-block">Reset</a>
            </div>
        </form>
    </aside>

    <section class="shop-results">
        <div class="results-toolbar">
            <span class="result-count" id="resultCount">${resultCount} product(s)</span>
            <span class="result-loading" id="resultLoading" style="display:none">Searching...</span>
        </div>
        <div id="productResults" class="product-grid">
            <c:choose>
                <c:when test="${empty results}">
                    <div class="empty-state">
                        <span class="empty-emoji">🐾</span>
                        <h3>No products found</h3>
                        <p>Try a different keyword or clear the filters.</p>
                        <a href="${ctx}/shop" class="btn btn-primary">Browse everything</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${results}" var="pc" varStatus="st">
                        <%@ include file="/WEB-INF/views/includes/product-card.jsp" %>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="empty-state" id="jsEmpty" style="display:none">
            <span class="empty-emoji">🐾</span>
            <h3>No products found</h3>
            <p>Try adjusting your filters.</p>
        </div>
        <div id="jsLoadingBox" class="empty-state" style="display:none">
            <span class="empty-emoji">🔍</span>
            <h3>Searching...</h3>
        </div>
    </section>
</div>

<script src="${ctx}/js/shop.js"></script>
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>