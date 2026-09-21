(function () {
    'use strict';

    var ctx = window.LULU.ctx;

    function buildStars(rating) {
        var html = '';
        for (var i = 1; i <= 5; i++) {
            var cls = 'star';
            if (rating >= i) {
                cls += ' on';
            } else if (rating >= i - 0.5) {
                cls += ' half';
            }
            html += '<span class="' + cls + '">\u2605</span>';
        }
        return html;
    }

    function inr(value) {
        return '\u20B9' + Number(value || 0).toLocaleString('en-IN', { maximumFractionDigits: 2 });
    }

    function cardHtml(p, wishlisted) {
        var esc = window.LULU.escapeHtml;
        var outOfStock = p.stockQty === 0;
        var active = wishlisted.indexOf(p.id) !== -1;
        return '' +
            '<div class="product-card">' +
            '<div class="card-img-wrap">' +
            '<a href="' + ctx + '/product?id=' + p.id + '" class="card-img" aria-label="' + esc(p.name) + '">' +
            '<img src="' + esc(p.imageUrl) + '" alt="' + esc(p.name) + '" loading="lazy"></a>' +
            (outOfStock ? '<span class="stock-ribbon">Out of Stock</span>' : '') +
            '<button type="button" class="wish-btn ' + (active ? 'active' : '') + '" data-wishlist="' + p.id + '" data-active="' + active + '" aria-label="Add to wishlist">' +
            '<svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path d="M12 21s-7.5-4.6-9.7-8.6C.7 9.4 2.2 6 5.5 5.4c2-.3 3.8.6 4.7 2.2.3.6 1.3.6 1.6 0 .9-1.6 2.7-2.5 4.7-2.2 3.3.6 4.8 4 3.2 7C19.5 16.4 12 21 12 21z" fill="none" stroke="currentColor" stroke-width="1.8"/></svg></button>' +
            '</div>' +
            '<div class="card-body">' +
            '<div class="card-tags"><span class="card-tag">' + esc(p.petType) + '</span><span class="card-tag card-tag-muted">' + esc(p.category) + '</span></div>' +
            '<a href="' + ctx + '/product?id=' + p.id + '" class="card-name">' + esc(p.name) + '</a>' +
            '<div class="card-rating"><span class="stars">' + buildStars(p.avgRating) + '</span><span class="rating-num">' + Number(p.avgRating || 0).toFixed(1) + '</span>' +
            (p.reviewCount > 0 ? '<span class="rating-count">(' + p.reviewCount + ')</span>' : '') + '</div>' +
            '<div class="card-price">' + inr(p.price) + '</div>' +
            '<div class="card-seller">by <a href="' + ctx + '/sellers">' + esc(p.sellerName) + '</a></div>' +
            '<div class="card-stock ' + (outOfStock ? 'out-stock' : 'in-stock') + '">' + (outOfStock ? 'Out of Stock' : 'In Stock') + '</div>' +
            '<div class="card-actions">' +
            '<a href="' + ctx + '/product?id=' + p.id + '" class="btn btn-outline btn-block btn-sm">View Details</a>' +
            '<button type="button" class="btn btn-primary btn-block btn-sm js-add-cart" data-product-id="' + p.id + '"' + (outOfStock ? ' disabled' : '') + '>Add to Cart</button>' +
            '</div></div></div>';
    }

    function currentWishlist() {
        var badge = document.getElementById('cartBadge');
        var authed = badge && badge.getAttribute('data-authed') === 'true';
        if (!authed) { return Promise.resolve([]); }
        return window.LULU.api('GET', ctx + '/api/v1/wishlist', null, { silent: true }).then(function (data) {
            var items = (data.data || []);
            return items.map(function (item) { return item.productId; });
        }).catch(function () { return []; });
    }

    function loading(on) {
        var box = document.getElementById('jsLoadingBox');
        var loadingLbl = document.getElementById('resultLoading');
        if (box) { box.style.display = on ? 'block' : 'none'; }
        if (loadingLbl) { loadingLbl.style.display = on ? 'inline' : 'none'; }
    }

    function render(data) {
        return currentWishlist().then(function (wishlisted) {
            var container = document.getElementById('productResults');
            var emptyBox = document.getElementById('jsEmpty');
            var countEl = document.getElementById('resultCount');
            var results = (data && data.results) || [];
            var total = (data && data.count !== undefined) ? data.count : results.length;

            if (container) {
                if (results.length === 0) {
                    container.innerHTML = '';
                } else {
                    container.innerHTML = results.map(function (p) { return cardHtml(p, wishlisted); }).join('');
                }
            }
            if (emptyBox) { emptyBox.style.display = results.length === 0 ? 'block' : 'none'; }
            if (countEl) { countEl.textContent = total + ' product(s)'; }
        });
    }

    var form = document.getElementById('filterForm');
    if (!form) { return; }

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        var fd = new FormData(form);
        var params = new URLSearchParams();
        var allowed = ['q', 'petType', 'category', 'minPrice', 'maxPrice', 'availability', 'sort', 'seller'];
        fd.forEach(function (value, key) {
            if (value && value !== '' && allowed.indexOf(key) !== -1) {
                params.set(key, value);
            }
        });

        var finalQs = params.toString();
        if (history.replaceState) {
            history.replaceState(null, '', ctx + '/shop' + (finalQs ? '?' + finalQs : ''));
        }

        loading(true);
        window.LULU.api('GET', ctx + '/api/v1/products' + (finalQs ? '?' + finalQs : ''), null, { silent: true })
            .then(function (data) {
                if (!data.success) {
                    toastError(data.message);
                    return;
                }
                render(data).then(function () { loading(false); });
            })
            .catch(function () { loading(false); toastError('Search failed, please retry.'); });
    });

    function toastError(msg) {
        if (window.LULU && window.LULU.toast) {
            window.LULU.toast(msg || 'Something went wrong', 'error');
        }
    }
})();