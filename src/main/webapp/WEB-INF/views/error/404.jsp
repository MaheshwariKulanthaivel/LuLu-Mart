<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="container section">
    <div class="error-state">
        <span class="error-code">404</span>
        <h1>Page Not Found</h1>
        <p>The page you are looking for doesn't exist, or a pet ran off with it.</p>
        <div class="error-actions">
            <a href="${ctx}/home" class="btn btn-primary">Go Home</a>
            <a href="${ctx}/shop" class="btn btn-outline">Browse Shop</a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>