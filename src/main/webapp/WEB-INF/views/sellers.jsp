<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<section class="hero hero-small">
    <div class="container hero-inner">
        <div class="hero-copy">
            <h1>Trusted Sellers</h1>
            <p class="hero-tagline">Hand-picked shops stocking only the best for your pet.</p>
        </div>
    </div>
</section>

<div class="container section">
    <c:choose>
        <c:when test="${empty sellers}">
            <p class="empty-state">No sellers have listed products yet. Check back soon!</p>
        </c:when>
        <c:otherwise>
            <div class="seller-list">
                <c:forEach var="s" items="${sellers}">
                    <div class="seller-card">
                        <div class="seller-avatar"><c:out value="${fn:substring(s.name, 0, 1)}"/></div>
                        <div class="seller-info">
                            <h3 class="seller-name"><c:out value="${s.name}"/></h3>
                            <p class="muted"><c:out value="${s.email}"/></p>
                            <p class="muted">Joined <c:out value="${s.createdAt}"/></p>
                        </div>
                        <div class="seller-meta">
                            <span class="seller-count"><c:out value="${s.productCount}"/> products</span>
                            <a href="${ctx}/shop?seller=${s.id}" class="btn btn-primary btn-sm">View Shop</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>