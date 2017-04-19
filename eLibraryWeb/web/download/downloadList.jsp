<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-11
  Time: 4:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="../styles/libraryAccounts.css" rel="stylesheet" type="text/css">
    <link href="../styles/mainPage.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <jsp:include page="../includes/navigationBar.jsp"/>
    <jsp:include page="../includes/search.jsp"/>
    <h1 id="title">List of books to download</h1>
    <table id="downloadTable" border="1" cellspacing="1">
        <c:forEach var="book" items="${account.books}">
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
    <c:if test="${fn:length(account.downloadList) > 0}">
        <a href="/download/downloadList?action=download">Download books</a>
    </c:if>
</div>
</body>
</html>