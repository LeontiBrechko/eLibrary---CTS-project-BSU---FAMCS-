<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-03
  Time: 12:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link href="../styles/mainPage.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <jsp:include page="../includes/navigationBar.jsp"/>
    <jsp:include page="../includes/search.jsp"/>
    <jsp:include page="../includes/sidebar.jsp"/>
    <div id="description">
        <img src="<c:url value="${book.image}" />" align="left" />
        <h3>${book.title}</h3>
        <ul>
            <li>
                Author: <c:forEach var="author" items="${book.authors}">${author.firstName} ${author.lastName} </c:forEach>
            </li>
            <li>
                Publisher: ${book.publisher.name} (${book.yearPublished})
            </li>
            <li>
                Category: <c:forEach var="cat" items="${book.categories}">${cat.name} </c:forEach>
            </li>
            <li>
                ISBN-13: ${book.isbn13}
            </li>
            <li>
                Language: <c:forEach var="file" items="${book.files}">${file.language} </c:forEach>
            </li>
            <li>
                Format: <c:forEach var="file" items="${book.files}">${file.format} </c:forEach>
            </li>
            <li>
                Popularity: ${book.popularity}
            </li>
        </ul>
        <p>${bookDescription}</p>
    </div>
    <c:if test="${isOpenable}">
        <a href="/catalog/description?action=openBook&amp;isbn13=${book.isbn13}">Open book online</a>
    </c:if>
    <c:choose>
        <c:when test="${isAddable}">
            <a href="/download/downloadList?action=addToDownloadList&amp;isbn13=${book.isbn13}">Add to download list</a>
        </c:when>
        <c:otherwise>
            <p id="success-message">Book has already been added to the download list.</p>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>