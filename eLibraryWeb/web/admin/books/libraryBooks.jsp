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
    <h1 id="title">Books: </h1>
    <table>
        <c:forEach var="book" items="${books}">
            <tr>
                <td><img src="<c:url value="${book.thumbnail}" />" align="left"/></td>
                <td>${book.title}</td>
                <td>${book.pubYear}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/catalog/description?action=showDescription&amp;isbn13=${book.isbn13}">
                        Description
                    </a>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/bookManagement?action=updateBook&amp;isbn13=${book.isbn13}">
                        Update book
                    </a>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/bookManagement?action=deleteBook&amp;isbn13=${book.isbn13}">
                        Delete book
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>