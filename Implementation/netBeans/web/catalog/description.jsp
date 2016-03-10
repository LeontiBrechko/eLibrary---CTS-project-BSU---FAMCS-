<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-03
  Time: 12:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="models.BookFile" %>
<%@ page import="models.Book" %>
<%@ page import="models.enums.Format" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
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
        <p>${book.description}</p>
    </div>
    <a href="/catalog/description?action=openBook&amp;isbn13=${book.isbn13}">Open book online</a>
    <a href="/index.jsp">Home</a>
</body>
</html>
