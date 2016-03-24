<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-15
  Time: 4:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Books: </h1>
    <table>
        <%--<tr>--%>
            <%--<th></th>--%>
        <%--</tr>--%>
        <c:forEach var="book" items="${books}">
            <tr>
                <td><img src="<c:url value="${book.thumbnail}" />" align="left" /></td>
                <td>${book.title}</td>
                <td>${book.yearPublished}</td>
                <td><a href="/catalog/description?action=showDescription&amp;isbn13=${book.isbn13}">Description'</a></td>
                <td><a href="/admin/bookManagement?action=updateBook&amp;isbn13=${book.isbn13}">Update book</a></td>
                <td><a>Delete book</a></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
