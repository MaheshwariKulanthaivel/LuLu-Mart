(function () {
    'use strict';

    function initPwdToggles() {
        document.querySelectorAll('.pwd-toggle').forEach(function (btn) {
            btn.addEventListener('click', function () {
                var wrap = btn.closest('.pwd-wrap');
                var input = wrap ? wrap.querySelector('input') : null;
                if (!input) return;
                var visible = btn.getAttribute('aria-pressed') === 'true';
                input.type = visible ? 'password' : 'text';
                btn.setAttribute('aria-pressed', String(!visible));
                btn.classList.toggle('is-visible', !visible);
            });
        });
    }

    function initRegisterForm() {
        var form = document.getElementById('registerForm');
        if (!form) return;
        var password = document.getElementById('password');
        var confirmPassword = document.getElementById('confirmPassword');
        var hint = document.getElementById('confirmHint');
        var submitBtn = document.getElementById('submitBtn');

        function checkMatch() {
            var ok = password && confirmPassword && password.value.length > 0
                && password.value === confirmPassword.value;
            if (hint) hint.classList.toggle('hidden', ok);
            if (confirmPassword) confirmPassword.classList.toggle('invalid', !ok);
        }
        if (password) password.addEventListener('input', checkMatch);
        if (confirmPassword) confirmPassword.addEventListener('input', checkMatch);

        form.addEventListener('submit', function (e) {
            if (!password || !confirmPassword || password.value !== confirmPassword.value) {
                e.preventDefault();
                checkMatch();
                if (confirmPassword) confirmPassword.focus();
                return;
            }
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.textContent = 'Creating account...';
            }
        });
    }

    function initLoginForm() {
        var form = document.getElementById('loginForm');
        if (!form) return;
        var submitBtn = form.querySelector('button[type="submit"]');
        form.addEventListener('submit', function () {
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.textContent = 'Logging in...';
            }
        });
    }

    function onReady(fn) {
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', fn);
        } else {
            fn();
        }
    }

    onReady(function () {
        initPwdToggles();
        initRegisterForm();
        initLoginForm();
    });
})();