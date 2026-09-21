(function () {
    'use strict';

    var ctx = window.LULU.ctx;

    function inr(value) {
        return '\u20B9' + Number(value || 0).toLocaleString('en-IN', { maximumFractionDigits: 2 });
    }

    function badgeCount(data) {
        var badge = document.getElementById('cartBadge');
        if (badge && data && data.count !== undefined) {
            badge.style.display = 'inline-flex';
            badge.textContent = data.count;
        }
    }

    function qtyInput(e) {
        var el = e.target.closest('[data-cart-qty]');
        return el ? parseInt(el.value, 10) : null;
    }

    function refresh(containerWrap) {
        return window.LULU.api('GET', ctx + '/api/v1/cart', null, { silent: true }).then(function (data) {
            if (!data.success) { return; }
            badgeCount(data.data);
            if (containerWrap) {
                renderCart(data.data);
            }
        });
    }

    function cartItemHtml(item) {
        var esc = window.LULU.escapeHtml;
        var warn = item.quantity > item.stockQty
            ? '<span class="cart-warn">Only ' + esc(item.stockQty) + ' left - quantity adjusted</span>' : '';
        var maxQty = Math.max(item.stockQty, 1);
        return '' +
            '<div class="cart-item">' +
            '<img src="' + esc(item.imageUrl) + '" alt="' + esc(item.productName) + '" class="cart-thumb" loading="lazy">' +
            '<div class="cart-item-info">' +
            '<a href="' + ctx + '/product?id=' + item.productId + '" class="cart-name">' + esc(item.productName) + '</a>' +
            '<span class="cart-seller">by ' + esc(item.sellerName) + '</span>' +
            '<span class="cart-price">' + inr(item.unitPrice) + '</span>' +
            warn +
            '</div>' +
            '<div class="cart-qty"><div class="qty-selector">' +
            '<button type="button" data-cart-dec data-product-id="' + item.itemId + '" aria-label="Decrease">&minus;</button>' +
            '<input type="number" value="' + Math.min(item.quantity, item.stockQty) + '" min="1" max="' + maxQty + '" readonly data-cart-qty data-product-id="' + item.itemId + '" aria-label="Quantity">' +
            '<button type="button" data-cart-inc data-product-id="' + item.itemId + '" aria-label="Increase">+</button>' +
            '</div></div>' +
            '<div class="cart-line-total">' + inr(item.subtotal) + '</div>' +
            '<button type="button" class="cart-remove" data-cart-remove="' + item.itemId + '" aria-label="Remove item">&times;</button>' +
            '</div>';
    }

    function renderCart(cart) {
        var wrap = document.getElementById('cartItems');
        var subtotalEl = document.getElementById('subtotal');
        var totalEl = document.getElementById('grandTotal');
        var checkoutBtn = document.querySelector('.cart-summary .btn-primary');

        var items = (cart && cart.items) || [];
        if (wrap) {
            if (items.length === 0) {
                wrap.innerHTML = '' +
                    '<div class="empty-state"><span class="empty-emoji">&#128722;</span>' +
                    '<h3>Your cart is empty</h3><p>Browse the shop and add some products for your furry friends.</p>' +
                    '<a href="' + ctx + '/shop" class="btn btn-primary">Go to Shop</a></div>';
            } else {
                wrap.innerHTML = items.map(cartItemHtml).join('');
            }
        }
        if (subtotalEl) { subtotalEl.textContent = inr(items.reduce(function (s, i) { return s + Number(i.subtotal || 0); }, 0)); }
        if (totalEl) { totalEl.textContent = inr(cart.grandTotal || 0); }
        if (checkoutBtn) { checkoutBtn.classList.toggle('disabled-link', items.length === 0); }
    }

    document.addEventListener('click', function (e) {
        var inc = e.target.closest('[data-cart-inc]');
        var dec = e.target.closest('[data-cart-dec]');
        var remove = e.target.closest('[data-cart-remove]');

        if (inc || dec) {
            var itemId = (inc || dec).getAttribute('data-product-id');
            var input = document.querySelector('[data-cart-qty][data-product-id="' + itemId + '"]');
            var current = input ? parseInt(input.value, 10) : 1;
            var max = input ? parseInt(input.getAttribute('max'), 10) : 9999;
            var target = dec ? current - 1 : current + 1;
            if (target < 1 || target > max) { return; }

            return window.LULU.api('PATCH', ctx + '/api/v1/cart/' + itemId, { quantity: String(target) }, { silent: true })
                .then(function (data) {
                    if (data.success) {
                        badgeCount(data.data);
                        renderCart(data.data);
                    } else {
                        if (window.LULU.toast) { window.LULU.toast(data.message || 'Could not update', 'error'); }
                    }
                });
        }

        if (remove) {
            var removeId = remove.getAttribute('data-cart-remove');
            window.LULU.api('DELETE', ctx + '/api/v1/cart/' + removeId, null, { silent: true }).then(function (data) {
                if (data.success) { refresh(true); }
            });
        }
    });

    window.LULU.refreshCart = function () { refresh(true); };
})();