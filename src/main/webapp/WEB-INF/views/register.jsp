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
            <div class="alert alert-error"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${ctx}/auth/register" class="auth-form">
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
                <label for="phone">Phone (10 digits)</label>
                <input type="tel" id="phone" name="phone" required pattern="[6-9][0-9]{9}"
                       placeholder="10-digit mobile number" value="${fn:escapeXml(param.phone)}"/>
            </div>
            <div class="field">
                <label for="password">Password (min 8, with letter and number)</label>
                <input type="password" id="password" name="password" required
                       pattern="^(?=.*[A-Za-z])(?=.*\d).{8,}$"
                       placeholder="At least 8 characters"/>
            </div>
            <div class="field">
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required
                       placeholder="Re-enter your password"/>
            </div>
            <div class="field">
                <label for="role">I want to...</label>
                <select id="role" name="role" required>
                    <option value="BUYER" ${param.role == 'SELLER' ? '' : 'selected'}>Shop for my pets (Buyer)</option>
                    <option value="SELLER" ${param.role == 'SELLER' ? 'selected' : ''}>Sell pet products (Seller)</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg">Create Account</button>
        </form>

        <p class="auth-alt">Already have an account? <a href="${ctx}/auth/login">Login</a></p>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>