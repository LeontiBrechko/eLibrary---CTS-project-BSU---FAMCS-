<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="../../styles/libraryAccounts.css" rel="stylesheet" type="text/css">
    <link href="../../styles/mainPage.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <jsp:include page="../../includes/navigationBar.jsp"/>
    <h1 id="title">Accounts: </h1>
    <table border="1" cellspacing="1">
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
    <div id="linkStyle">
        <p>&nbsp;</p>
        <p><a href="">return to the Main Page</a></p>
    </div>
</div>
</body>
</html>
