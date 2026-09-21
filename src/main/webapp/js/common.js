(function () {
    'use strict';

    var ctx = (window.LULU && window.LULU.ctx) || '';

    function escapeHtml(value) {
        return String(value == null ? '' : value)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }

    function api(method, url, body, opts) {
        opts = opts || {};
        var headers = { 'X-Requested-With': 'XMLHttpRequest' };
        var options = {
            method: method || 'GET',
            headers: headers,
            credentials: 'same-origin'
        };
        if (body !== undefined && body !== null) {
            headers['Content-Type'] = 'application/json';
            options.body = JSON.stringify(body);
        }
        return fetch(url, options).then(function (resp) {
            return resp.json().catch(function () {
                return { success: false, message: 'Unexpected server response' };
            }).then(function (data) {
                data.status = resp.status;
                if (resp.status === 401) {
                    if (!opts.silent) {
                        window.location.href = ctx + '/auth/login';
                    }
                    return data;
                }
                return data;
            });
        });
    }

    function toast(message, type) {
        var container = document.getElementById('toastContainer');
        if (!container) { return; }
        var el = document.createElement('div');
        el.className = 'toast ' + (type === 'error' ? 'toast-error' : 'toast-success');
        el.innerHTML = '<span>' + escapeHtml(message) + '</span>';
        container.appendChild(el);
        setTimeout(function () {
            el.classList.add('show');
        }, 10);
        setTimeout(function () {
            el.classList.remove('show');
            setTimeout(function () { el.remove(); }, 300);
        }, 3200);
    }

    window.LULU = window.LULU || {};
    window.LULU.ctx = ctx;
    window.LULU.api = api;
    window.LULU.escapeHtml = escapeHtml;
    window.LULU.toast = toast;

    function redirectToLogin() {
        var next = window.encodeURIComponent(window.location.pathname + window.location.search);
        window.location.href = ctx + '/auth/login?next=' + next;
    }

    function updateCartBadge() {
        var badge = document.getElementById('cartBadge');
        if (!badge) { return; }
        if (badge.getAttribute('data-authed') !== 'true') { return; }
        api('GET', ctx + '/api/v1/cart', null, { silent: true }).then(function (data) {
            if (data.success && data.data) {
                badge.style.display = 'inline-flex';
                badge.textContent = data.data.count || 0;
            }
        });
    }

    function doWishlist(productId, setActive) {
        api('POST', ctx + '/api/v1/wishlist/toggle', { productId: String(productId) }, { silent: true }).then(function (data) {
            if (data.status === 401) { return; }
            if (data.status === 403) {
                redirectToLogin();
                return;
            }
            if (!data.success) {
                toast(data.message || 'Something went wrong', 'error');
                return;
            }
            var added = data.data ? data.data.added : false;
            if (setActive !== undefined) {
                added = setActive;
            }
            toast(data.message || (added ? 'Added to wishlist' : 'Removed from wishlist'), 'success');
            setWishlistButtons(String(productId), added);
        }).catch(function () {
            toast('Request failed', 'error');
        });
    }

    function setWishlistButtons(productId, active) {
        var btns = document.querySelectorAll('[data-wishlist="' + productId + '"]');
        btns.forEach(function (btn) {
            btn.classList.toggle('active', active);
            btn.setAttribute('data-active', active ? 'true' : 'false');
        });
    }

    function doAddToCart(productId, qty) {
        api('POST', ctx + '/api/v1/cart', { productId: String(productId), quantity: String(qty || 1) }, { silent: true }).then(function (data) {
            if (data.status === 401) { return; }
            if (data.status === 403) {
                redirectToLogin();
                return;
            }
            if (!data.success) {
                toast(data.message || 'Something went wrong', 'error');
                return;
            }
            toast(data.message || 'Added to cart', 'success');
            if (data.data && data.data.cartCount !== undefined) {
                var badge = document.getElementById('cartBadge');
                if (badge) { badge.setAttribute('data-authed', 'true'); badge.style.display = 'inline-flex'; badge.textContent = data.data.cartCount; }
            }
        }).catch(function () {
            toast('Request failed', 'error');
        });
    }

    document.addEventListener('click', function (e) {
        var addBtn = e.target.closest('.js-add-cart');
        if (addBtn) {
            if (addBtn.disabled) { return; }
            doAddToCart(addBtn.getAttribute('data-product-id'), 1);
            return;
        }

        var cartDetail = e.target.closest('.js-add-cart-detail');
        if (cartDetail) {
            if (cartDetail.disabled) { return; }
            var input = document.getElementById('qtyInput');
            var qty = input ? parseInt(input.value, 10) : 1;
            if (!qty || qty < 1) { qty = 1; }
            doAddToCart(cartDetail.getAttribute('data-product-id'), qty);
            return;
        }

        var wishBtn = e.target.closest('[data-wishlist]');
        if (wishBtn) {
            var pid = wishBtn.getAttribute('data-wishlist');
            var active = wishBtn.getAttribute('data-active') === 'true';
            doWishlist(pid, !active);
            return;
        }

        var wishToggle = e.target.closest('.js-wishlist-toggle');
        if (wishToggle) {
            var wpid = wishToggle.getAttribute('data-product-id');
            var wactive = wishToggle.getAttribute('data-active') === 'true';
            doWishlist(wpid, !wactive);
            return;
        }

        if (e.target.closest('.user-chip')) {
            document.querySelector('.user-menu').classList.toggle('open');
            var menu = document.querySelector('.user-menu');
            if (menu) {
                setTimeout(function () {
                    var outside = function (ev) {
                        if (!menu.contains(ev.target)) {
                            menu.classList.remove('open');
                            document.removeEventListener('click', outside);
                        }
                    };
                    document.addEventListener('click', outside);
                }, 0);
            }
        }

        if (e.target.closest('.toast-close')) {
            var t = e.target.closest('.toast');
            if (t) { t.remove(); }
        }
    });

    document.addEventListener('DOMContentLoaded', function () {
        var toggle = document.getElementById('navToggle');
        var links = document.getElementById('navLinks');
        if (toggle && links) {
            toggle.addEventListener('click', function () {
                links.classList.toggle('open');
            });
        }

        var images = document.querySelectorAll('img');
        var fallback = ctx + '/images/pet-placeholder.svg';
        images.forEach(function (img) {
            if (img.getAttribute('data-no-fallback') === 'true') { return; }
            img.addEventListener('error', function handler() {
                img.removeEventListener('error', handler);
                if (img.src.indexOf('pet-placeholder') === -1) {
                    img.src = fallback;
                }
            });
        });

        updateCartBadge();
    });
})();