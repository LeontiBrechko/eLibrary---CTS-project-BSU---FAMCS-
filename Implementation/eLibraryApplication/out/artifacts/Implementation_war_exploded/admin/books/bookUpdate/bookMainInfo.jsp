<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-17
  Time: 9:59 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="data.CategoryDB" %>
<%@ page import="java.sql.SQLException" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    try {
        request.setAttribute("categories", CategoryDB.selectAllCategories());
    } catch (SQLException e) {
        e.printStackTrace();
    }
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h4>${errorMessage}</h4>
    <form action="/admin/bookManagement" method="post" id="mainForm" enctype="multipart/form-data" accept-charset="UTF-8">
        <input type="hidden" name="action" value="updateBookMainInfo">
        <fieldset form="mainForm">
            <legend>Main book information</legend>
                <input type="hidden" name="popularity" value="${bookToUpdate.popularity}">
                <label> ISBN-13:
                    <c:choose>
                        <c:when test="${bookToUpdate.isbn13 != null}">
                            <input type="text" name="isbn13" value="<c:out value="${bookToUpdate.isbn13}"/>" readonly>
                        </c:when>
                        <c:otherwise>
                            <input type="text" name="isbn13" value="<c:out value="${bookToUpdate.isbn13}"/>">
                        </c:otherwise>
                    </c:choose>
                </label><br>
                <label>Title:
                    <input type="text" name="title" value="<c:out value="${bookToUpdate.title}"/>">
                </label><br>
                <label>Year published:
                    <input type="text" name="yearPublished" value="<c:out value="${bookToUpdate.yearPublished}"/>">
                </label><br>
                <label>Description file:
                    <input type="file" name="description">
                </label><br>
                <label>Image:
                    <input type="file" name="image">
                </label><br>
                <label>Thumbnail:
                    <input type="file" name="thumbnail">
                </label><br>
        </fieldset>
        <fieldset form="mainForm">
            <legend>Book categories</legend>
            <select name="selectedCategories" multiple="multiple" size="10">
                <c:forEach var="category" items="${categories}">
                    <option value="<c:out value="${category.name}"/>">
                        <c:out value="${category.name}"/>
                    </option>
                </c:forEach>
            </select>
        </fieldset>
        <input type="submit" value="Update book">
    </form>
</body>
</html>
