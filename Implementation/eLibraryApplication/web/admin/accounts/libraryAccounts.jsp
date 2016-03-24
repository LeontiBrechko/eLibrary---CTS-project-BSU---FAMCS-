<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-21
  Time: 10:05 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Accounts: </h1>
    <table border="1" cellspacing="1">
        <%--<tr>--%>
        <%--<th></th>--%>
        <%--</tr>--%>
        <c:forEach var="account" items="${accounts}">
            <tr>
                <td>${account.username}</td>
                <td>${account.email}</td>
                <td>${account.role}</td>
                <td>${account.creationTime}</td>
                <td>${account.state}</td>
                <td>
                    <c:choose>
                        <c:when test="${account.state == 'FROZEN'}">
                            <a href="/admin/accountManagement?action=unblock&amp;username=${account.username}">
                                Unblock account
                            </a>
                        </c:when>
                        <c:when test="${account.state != 'TEMPORARY'}">
                            <a href="/admin/accountManagement?action=block&amp;username=${account.username}">
                                Block account
                            </a>
                        </c:when>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
