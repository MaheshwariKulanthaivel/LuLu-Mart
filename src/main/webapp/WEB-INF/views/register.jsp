<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="auth-page">
    <div class="auth-card">
        <div class="auth-head">
            <span class="auth-logo">🐾</span>
            <h1>Join Lulu Mart</h1>
            <p class="muted">Shop as a buyer or sell to pet parents across India.</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <c:out value="${error}"/>
                <c:if test="${fn:contains(error, 'already exists')}">
                    <a href="${ctx}/auth/login">Log in instead</a>.
                </c:if>
            </div>
        </c:if>

        <form method="post" action="${ctx}/auth/register" class="auth-form" id="registerForm">
            <input type="hidden" name="csrfToken" value="${fn:escapeXml(csrfToken)}">
            <div class="field">
                <label for="name">Full Name</label>
                <input type="text" id="name" name="name" required minlength="2" maxlength="80"
                       placeholder="Your name" value="${fn:escapeXml(param.name)}"/>
            </div>
            <div class="field">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required placeholder="you@example.com"
                       value="${fn:escapeXml(param.email)}"/>
            </div>
            <div class="field">
                <label for="password">Password (min 8, with letter and number)</label>
                <div class="pwd-wrap">
                    <input type="password" id="password" name="password" required
                           pattern="^(?=.*[A-Za-z])(?=.*\d).{8,}$"
                           placeholder="At least 8 characters" autocomplete="new-password"/>
                    <button type="button" class="pwd-toggle" aria-pressed="false"
                            aria-label="Show password" title="Show/hide password">
                        <svg class="eye-on" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 512 512" fill="none" stroke="currentColor" stroke-width="32" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M255.66 112c-77.94 0-157.89 45.11-220.83 132.35a16 16 0 0 0 0 17.4C97.77 380.89 177.72 426 255.66 426s157.89-45.11 220.83-164.65a16 16 0 0 0 0-17.4C413.55 157.11 333.6 112 255.66 112z"/><circle cx="256" cy="256" r="64"/></svg>
                        <svg class="eye-off" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 512 512" fill="none" stroke="currentColor" stroke-width="32" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M255.66 112c-77.94 0-157.89 45.11-220.83 132.35a16 16 0 0 0 0 17.4C97.77 380.89 177.72 426 255.66 426s157.89-45.11 220.83-164.65a16 16 0 0 0 0-17.4C413.55 157.11 333.6 112 255.66 112z"/><circle cx="256" cy="256" r="64"/><line x1="100" y1="100" x2="412" y2="412"/></svg>
                    </button>
                </div>
                <small class="muted" id="passwordHint">Use at least 8 characters with at least one letter and one number.</small>
            </div>
            <div class="field">
                <label for="confirmPassword">Confirm Password</label>
                <div class="pwd-wrap">
                    <input type="password" id="confirmPassword" name="confirmPassword" required
                           placeholder="Re-enter your password" autocomplete="new-password"/>
                    <button type="button" class="pwd-toggle" aria-pressed="false"
                            aria-label="Show password" title="Show/hide password">
                        <svg class="eye-on" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 512 512" fill="none" stroke="currentColor" stroke-width="32" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M255.66 112c-77.94 0-157.89 45.11-220.83 132.35a16 16 0 0 0 0 17.4C97.77 380.89 177.72 426 255.66 426s157.89-45.11 220.83-164.65a16 16 0 0 0 0-17.4C413.55 157.11 333.6 112 255.66 112z"/><circle cx="256" cy="256" r="64"/></svg>
                        <svg class="eye-off" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 512 512" fill="none" stroke="currentColor" stroke-width="32" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M255.66 112c-77.94 0-157.89 45.11-220.83 132.35a16 16 0 0 0 0 17.4C97.77 380.89 177.72 426 255.66 426s157.89-45.11 220.83-164.65a16 16 0 0 0 0-17.4C413.55 157.11 333.6 112 255.66 112z"/><circle cx="256" cy="256" r="64"/><line x1="100" y1="100" x2="412" y2="412"/></svg>
                    </button>
                </div>
                <small class="field-error hidden" id="confirmHint">Passwords do not match.</small>
            </div>
            <div class="field">
                <label for="role">I want to...</label>
                <select id="role" name="role" required>
                    <option value="BUYER" ${param.role == 'SELLER' ? '' : 'selected'}>Shop for my pets (Buyer)</option>
                    <option value="SELLER" ${param.role == 'SELLER' ? 'selected' : ''}>Sell pet products (Seller)</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg" id="submitBtn">Create Account</button>
        </form>

        <p class="auth-alt">Already have an account? <a href="${ctx}/auth/login">Login</a></p>
    </div>
</div>

<script src="${ctx}/js/auth.js" defer></script>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>