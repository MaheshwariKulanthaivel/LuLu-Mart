<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="auth-page">
    <div class="auth-card">
        <div class="auth-head">
            <span class="auth-logo">🐾</span>
            <h1>Welcome back to Lulu Mart</h1>
            <p class="muted">Log in to shop, track orders and more.</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${ctx}/auth/login" class="auth-form">
            <input type="hidden" name="next" value="${fn:escapeXml(param.next)}">
            <div class="field">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required autofocus placeholder="you@example.com"/>
            </div>
            <div class="field">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required placeholder="Your password"/>
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg">Login</button>
        </form>

        <p class="auth-alt">New to Lulu Mart? <a href="${ctx}/auth/register">Create an account</a></p>

        <div class="demo-box">
            <h4>Demo Accounts</h4>
            <code>Buyer&nbsp;&nbsp;buyer@lulumart.com&nbsp;&nbsp;/&nbsp;&nbsp;Buyer@123</code>
            <code>Seller&nbsp;seller@lulumart.com&nbsp;/&nbsp;Seller@123</code>
            <code>Admin&nbsp;&nbsp;admin@lulumart.com&nbsp;&nbsp;/&nbsp;Admin@123</code>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>