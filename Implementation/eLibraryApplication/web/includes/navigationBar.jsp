<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-05-18
  Time: 3:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="navBar">
    <ul>
        <c:choose>
            <c:when test="${sessionScope.account != null}">
                <li>Books in list: ${fn:length(account.downloadList)}</li>
                <li><a href="/download/downloadList?action=showDownloadList">Go to list</a></li>
                <li><a href="/account/login?action=logout">Log out</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="/account/login.jsp">Login</a></li>
                <li><a href="/account/register.jsp">Register</a></li>
            </c:otherwise>
        </c:choose>
        <li><a href="/catalog">Book Catalog</a></li>
        <c:if test="${sessionScope.account != null && sessionScope.account.role == 'ADMIN'}">
            <li><a href="/admin/adminOffice.jsp">Admin office</a></li>
        </c:if>
    </ul>
</div>
