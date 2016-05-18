<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-07
  Time: 9:45 AM
  To change this template use File | Settings | File Templates.
--%>
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
    <h1 id="title">Books: </h1>
    <c:forEach var="book" items="${books}">
        <div>
            <img src="<c:url value="${book.thumbnail}" />" align="left"/>
            <p>${book.title}</p>
            <p>${book.yearPublished}</p>
            <p><a href="catalog/description?action=showDescription&amp;isbn13=${book.isbn13}">Description</a></p>
        </div>
    </c:forEach>
</div>
</body>
</html>




