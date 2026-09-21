<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="container section">
    <div class="error-state">
        <span class="error-code">403</span>
        <h1>Access Denied</h1>
        <p>You don't have permission to view this page. Log in with the right account to continue.</p>
        <div class="error-actions">
            <a href="${ctx}/auth/login" class="btn btn-primary">Login</a>
            <a href="${ctx}/auth/register" class="btn btn-outline">Create Account</a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>