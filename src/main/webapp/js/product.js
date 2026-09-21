(function () {
    'use strict';

    var ctx = window.LULU.ctx;

    var qtyInput = document.getElementById('qtyInput');

    if (qtyInput) {
        var max = parseInt(qtyInput.getAttribute('max'), 10) || 99;
        document.addEventListener('click', function (e) {
            var dec = e.target.closest('[data-qty="dec"]');
            var inc = e.target.closest('[data-qty="inc"]');
            if (!dec && !inc) { return; }
            var current = parseInt(qtyInput.value, 10) || 1;
            var next = dec ? current - 1 : current + 1;
            if (next < 1) { next = 1; }
            if (next > max) { next = max; }
            qtyInput.value = next;
        });
    }

    var ratingInputs = document.querySelectorAll('.rating-input .star-input');
    if (ratingInputs.length) {
        ratingInputs.forEach(function (label) {
            label.addEventListener('click', function () {
                var val = parseInt(label.querySelector('input').value, 10);
                ratingInputs.forEach(function (l) {
                    l.classList.toggle('selected', parseInt(l.querySelector('input').value, 10) <= val);
                });
            });
        });
        var checked = document.querySelector('.rating-input input:checked');
        if (checked) { checked.closest('.star-input').classList.add('selected'); }
    }

    var reviewForm = document.getElementById('reviewForm');
    if (reviewForm) {
        reviewForm.addEventListener('submit', function (e) {
            e.preventDefault();
            var data = {
                productId: document.getElementById('reviewProductId').value,
                rating: (document.querySelector('.rating-input input:checked') || {}).value,
                comment: document.getElementById('reviewComment').value
            };
            window.LULU.api('POST', ctx + '/api/v1/reviews', data, { silent: true }).then(function (resp) {
                var statusEl = document.getElementById('reviewStatus');
                if (!resp.success) {
                    if (statusEl) { statusEl.textContent = resp.message || 'Could not post review'; statusEl.className = 'muted review-status'; }
                    if (window.LULU.toast) { window.LULU.toast(resp.message || 'Could not post review', 'error'); }
                    return;
                }
                if (statusEl) { statusEl.textContent = 'Posted, thank you!'; statusEl.className = 'muted review-status'; }
                if (window.LULU.toast) { window.LULU.toast(ratingMessage(), 'success'); }
                document.getElementById('reviewComment').value = '';
                setTimeout(function () {
                    onReviewPosted();
                }, 800);
            });
        });
    }

    function ratingMessage() {
        var r = parseInt((document.querySelector('.rating-input input:checked') || {}).value, 10) || 0;
        if (r >= 4) { return 'Thank you for your review!'; }
        if (r === 3) { return 'Thanks! Feedback noted.'; }
        if (r <= 2) { return 'We appreciate your honest feedback.'; }
        return 'Review posted';
    }

    function onReviewPosted() {
        var rating = parseInt((document.querySelector('.rating-input input:checked') || {}).value, 10);
        if (!rating) { rating = 5; }
        var comment = document.getElementById('reviewComment').value;
        window.LULU.api('GET', ctx + '/api/v1/reviews?productId=' + document.getElementById('reviewProductId').value, null, { silent: true })
            .then(function (data) {
                if (!data.success) { return; }
                var reviews = (data.data && data.data.reviews) || [];
                var count = (data.data && data.data.count) || 0;
                var avg = (data.data && data.data.avgRating) || 0;

                var list = document.getElementById('reviewList');
                if (list) {
                    if (reviews.length === 0) {
                        list.innerHTML = '<p class="muted">No reviews yet.</p>';
                    } else {
                        list.innerHTML = reviews.map(function (r) {
                            return reviewHtml(r);
                        }).join('');
                    }
                }
                var head = document.querySelector('.section-head h2');
                if (head && head.textContent.indexOf('Reviews') !== -1) {
                    head.textContent = 'Reviews (' + count + ')';
                }
            });
    }

    function reviewHtml(r) {
        var esc = window.LULU.escapeHtml;
        var stars = '';
        for (var i = 1; i <= 5; i++) {
            stars += '<span class="star ' + (r.rating >= i ? 'on' : '') + '">\u2605</span>';
        }
        return '' +
            '<div class="review-card">' +
            '<span class="rev-avatar">' + esc((r.reviewerName || '?').charAt(0)) + '</span>' +
            '<div><div class="rev-head"><b>' + esc(r.reviewerName) + '</b><span class="stars">' + stars + '</span></div>' +
            '<p>' + esc(r.comment) + '</p></div></div>';
    }
})();