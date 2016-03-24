<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-11
  Time: 4:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <table border="1" cellspacing="1">
        <c:forEach var="book" items="${account.downloadList}">
            <tr>
                <td><img src="<c:url value="${book.thumbnail}" />" /></td>
                <td>${book.title}</td>
                <td>${book.yearPublished}</td>
                <td>
                    <c:forEach var="file" items="${book.files}">
                        ${file.format}
                    </c:forEach>
                </td>
                <td>
                    <a href="/download/downloadList?action=deleteFromDownloadList&amp;isbn13=${book.isbn13}">
                    Delete from list
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
