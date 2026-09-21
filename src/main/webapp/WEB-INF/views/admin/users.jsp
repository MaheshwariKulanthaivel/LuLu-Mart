<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="container page-title-row">
    <div>
        <h1>Manage Users</h1>
        <p class="muted">Everyone registered on Lulu Mart.</p>
    </div>
</div>

<div class="container">
    <c:if test="${not empty param.msg}"><div class="alert alert-success"><c:out value="${param.msg}"/></div></c:if>
    <c:if test="${not empty param.error}"><div class="alert alert-error"><c:out value="${param.error}"/></div></c:if>

    <div class="filter-tabs">
        <a href="${ctx}/admin/users" class="${empty roleFilter ? 'active' : ''}">All</a>
        <a href="${ctx}/admin/users?role=BUYER" class="${roleFilter == 'BUYER' ? 'active' : ''}">Buyers</a>
        <a href="${ctx}/admin/users?role=SELLER" class="${roleFilter == 'SELLER' ? 'active' : ''}">Sellers</a>
        <a href="${ctx}/admin/users?role=ADMIN" class="${roleFilter == 'ADMIN' ? 'active' : ''}">Admins</a>
    </div>

    <div class="table-wrap">
        <table class="data-table">
            <thead>
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Joined</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="u">
                <tr>
                    <td><c:out value="${u.name}"/></td>
                    <td><c:out value="${u.email}"/></td>
                    <td><span class="status-badge status-${fn:toLowerCase(u.role)}"><c:out value="${u.role}"/></span></td>
                    <td><c:out value="${u.createdAt}"/></td>
                    <td>
                        <c:if test="${not u.admin}">
                            <form method="post" action="${ctx}/admin/users/delete"
                                  onsubmit="return confirm('Remove ${fn:escapeXml(u.name)}?')">
                                <input type="hidden" name="id" value="${u.id}">
                                <button type="submit" class="btn btn-danger-outline btn-sm">Remove</button>
                            </form>
                        </c:if>
                        <c:if test="${u.admin}">
                            <span class="muted">Protected</span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>