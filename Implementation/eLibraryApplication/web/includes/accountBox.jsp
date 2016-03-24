<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-10
  Time: 2:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <c:choose>
        <c:when test="${sessionScope.account != null}">
            <p>Books in list: ${fn:length(account.downloadList)}</p>
            <a href="/download/downloadList?action=showDownloadList">Go to list</a>
            <a href="/account/login?action=logout">Log out</a>
        </c:when>
        <c:otherwise>
            <a href="/account/login.jsp">Login</a> <br />
            <a href="/account/register.jsp">Register</a>
        </c:otherwise>
    </c:choose>
</div>
