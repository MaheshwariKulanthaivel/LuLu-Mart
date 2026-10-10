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

        <form method="post" action="${ctx}/auth/login" class="auth-form" id="loginForm">
            <input type="hidden" name="next" value="${fn:escapeXml(param.next)}">
            <input type="hidden" name="csrfToken" value="${fn:escapeXml(csrfToken)}">
            <div class="field">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required autofocus placeholder="you@example.com"
                       value="${fn:escapeXml(param.email)}"/>
            </div>
            <div class="field">
                <label for="password">Password</label>
                <div class="pwd-wrap">
                    <input type="password" id="password" name="password" required placeholder="Your password"/>
                    <button type="button" class="pwd-toggle" aria-pressed="false"
                            aria-label="Show password" title="Show/hide password">
                        <svg class="eye-on" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 512 512" fill="none" stroke="currentColor" stroke-width="32" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M255.66 112c-77.94 0-157.89 45.11-220.83 132.35a16 16 0 0 0 0 17.4C97.77 380.89 177.72 426 255.66 426s157.89-45.11 220.83-164.65a16 16 0 0 0 0-17.4C413.55 157.11 333.6 112 255.66 112z"/><circle cx="256" cy="256" r="64"/></svg>
                        <svg class="eye-off" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 512 512" fill="none" stroke="currentColor" stroke-width="32" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M255.66 112c-77.94 0-157.89 45.11-220.83 132.35a16 16 0 0 0 0 17.4C97.77 380.89 177.72 426 255.66 426s157.89-45.11 220.83-164.65a16 16 0 0 0 0-17.4C413.55 157.11 333.6 112 255.66 112z"/><circle cx="256" cy="256" r="64"/><line x1="100" y1="100" x2="412" y2="412"/></svg>
                    </button>
                </div>
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

<script src="${ctx}/js/auth.js" defer></script>
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>