<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="container section">
    <div class="error-state">
        <span class="error-code">500</span>
        <h1>Something Went Wrong</h1>
        <p>An unexpected error occurred. Our team (of good boys and girls) has been notified.</p>
        <div class="error-actions">
            <a href="${ctx}/home" class="btn btn-primary">Go Home</a>
            <a href="${ctx}/shop" class="btn btn-outline">Browse Shop</a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>