<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="data.BookDB" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="utils.dataValidation.DataValidationException" %>
<%@ page import="utils.dataValidation.InternalDataValidationException" %><%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-02-27
  Time: 9:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    try {
        request.setAttribute("books", BookDB.selectBookList(false, false));
    } catch (SQLException | DataValidationException | InternalDataValidationException e) {
        e.printStackTrace();
    }
%>
<html>
<head>
    <title>$Title$</title>
    <link href="../styles/mainPage.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <p class="alert-danger">${errorMessage}</p>
    <jsp:include page="includes/navigationBar.jsp"/>
    <jsp:include page="includes/search.jsp"/>
    <jsp:include page="includes/sidebar.jsp"/>
    <div class="content">
        <c:forEach var="book" items="${books}">
            <div class="book">
                <label>${book.title}<br>
                    <img src="<c:url value="${book.thumbnail}" />" alt="${book.title}"><br>
                    <a href="/catalog/description?action=showDescription&amp;isbn13=${book.isbn13}">Description</a>
                </label>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>

