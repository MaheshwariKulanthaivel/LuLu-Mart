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
                <input type="password" id="password" name="password" required
                       pattern="^(?=.*[A-Za-z])(?=.*\d).{8,}$"
                       placeholder="At least 8 characters" autocomplete="new-password"/>
                <small class="muted" id="passwordHint">Use at least 8 characters with at least one letter and one number.</small>
            </div>
            <div class="field">
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required
                       placeholder="Re-enter your password" autocomplete="new-password"/>
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

<script>
(function () {
    var form = document.getElementById('registerForm');
    var password = document.getElementById('password');
    var confirmPassword = document.getElementById('confirmPassword');
    var confirmHint = document.getElementById('confirmHint');
    var submitBtn = document.getElementById('submitBtn');

    function checkMatch() {
        var ok = password.value === confirmPassword.value && password.value.length > 0;
        confirmHint.classList.toggle('hidden', ok);
        confirmPassword.classList.toggle('invalid', !ok);
    }
    confirmPassword.addEventListener('input', checkMatch);
    password.addEventListener('input', checkMatch);

    form.addEventListener('submit', function (e) {
        if (password.value !== confirmPassword.value) {
            e.preventDefault();
            checkMatch();
            confirmPassword.focus();
            return;
        }
        submitBtn.disabled = true;
        submitBtn.textContent = 'Creating account...';
    });
})();
</script>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>