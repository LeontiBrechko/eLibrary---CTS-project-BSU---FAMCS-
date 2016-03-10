<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-08
  Time: 4:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <table>
    <c:forEach items="${headerValues}" var="entry">
        <tr>
            <td>
                ${entry.key}}
            </td>
            <td>
                <c:forEach items="${entry.value}" var="headerValue">
                    <p>${headerValue}</p>
                </c:forEach>
            </td>
        </tr>
    </c:forEach>
    </table>
</body>
</html>
