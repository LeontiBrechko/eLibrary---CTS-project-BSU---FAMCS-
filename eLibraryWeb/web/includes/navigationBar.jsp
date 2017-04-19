<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="navBar">
    <ul>
        <c:choose>
            <c:when test="${sessionScope.account != null}">
                <li>Books in list: ${fn:length(sessionScope.account.books)}</li>
                <li>
                    <a href="${pageContext.request.contextPath}/download/downloadList?action=showDownloadList">
                        Go to list
                    </a>
                </li>
                <li><a href="${pageContext.request.contextPath}/account/login?action=logout">Log out</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="${pageContext.request.contextPath}/account/login.jsp">Login</a></li>
                <li><a href="${pageContext.request.contextPath}/account/register.jsp">Register</a></li>
            </c:otherwise>
        </c:choose>
        <li><a href="${pageContext.request.contextPath}/catalog">Book Catalog</a></li>
        <c:if test="${sessionScope.account != null}">
            <c:forEach var="role" items="${sessionScope.account.roles}">
                <c:if test="${role.role == 'ADMIN'}">
                    <li><a href="${pageContext.request.contextPath}/admin/adminOffice.jsp">Admin office</a></li>
                </c:if>
            </c:forEach>
        </c:if>
    </ul>
</div>
