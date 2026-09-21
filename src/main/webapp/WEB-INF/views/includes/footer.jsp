</main>

<footer class="site-footer">
    <div class="container footer-grid">
        <div class="footer-brand">
            <a href="${ctx}/home" class="logo">
                <svg class="logo-paw" viewBox="0 0 24 24" width="26" height="26" aria-hidden="true">
                    <g fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round">
                        <ellipse cx="6.5" cy="9.5" rx="2.4" ry="3.2" transform="rotate(-30 6.5 9.5)"/>
                        <ellipse cx="17.5" cy="9.5" rx="2.4" ry="3.2" transform="rotate(30 17.5 9.5)"/>
                        <ellipse cx="4.2" cy="16" rx="2.7" ry="3.4" transform="rotate(-40 4.2 16)"/>
                        <ellipse cx="19.8" cy="16" rx="2.7" ry="3.4" transform="rotate(40 19.8 16)"/>
                        <ellipse cx="12" cy="17.5" rx="3.2" ry="3.8"/>
                    </g>
                </svg>
                <span class="logo-text">LULU <b>MART</b></span>
            </a>
            <p>Everything your pet loves, delivered. A trusted multi-seller marketplace for pet products across India.</p>
        </div>
        <div class="footer-col">
            <h4>Shop</h4>
            <a href="${ctx}/shop">All Products</a>
            <a href="${ctx}/pets">By Pet</a>
            <a href="${ctx}/categories">By Category</a>
            <a href="${ctx}/sellers">Sellers</a>
        </div>
        <div class="footer-col">
            <h4>Account</h4>
            <c:choose>
                <c:when test="${authUser == null}">
                    <a href="${ctx}/auth/login">Login</a>
                    <a href="${ctx}/auth/register">Create Account</a>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${authUser.buyer}"><a href="${ctx}/orders">My Orders</a></c:when>
                        <c:when test="${authUser.seller}"><a href="${ctx}/seller/dashboard">Seller Dashboard</a></c:when>
                        <c:when test="${authUser.admin}"><a href="${ctx}/admin/dashboard">Admin Dashboard</a></c:when>
                    </c:choose>
                    <form action="${ctx}/auth/logout" method="post"><button type="submit" class="link-btn">Logout</button></form>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="footer-col">
            <h4>Why Lulu Mart</h4>
            <a href="${ctx}/about">About Us</a>
            <a href="${ctx}/about#trust">Trust &amp; Safety</a>
            <a href="${ctx}/about#pets">Our Pets</a>
        </div>
    </div>
    <div class="container footer-bottom">
        <span>&copy; 2026 Lulu Mart. Made with love for pets everywhere.</span>
    </div>
</footer>

<div id="toastContainer" class="toast-container" aria-live="polite"></div>

<script>window.LULU = { ctx: '${ctx}' };</script>
<script src="${ctx}/js/common.js"></script>
</body>
</html>