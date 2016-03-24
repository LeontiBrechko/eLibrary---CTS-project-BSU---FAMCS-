<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-17
  Time: 9:59 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="/admin/bookManagement" method="post" id="mainForm" enctype="multipart/form-data" accept-charset="UTF-8">
        <input type="hidden" name="action" value="updateBookMainInfo">
        <fieldset form="mainForm">
            <legend>Main book information</legend>
                <input type="hidden" name="popularity" value="${bookToUpdate.popularity}">
                <label> ISBN-13:
                    <input type="text" name="isbn13" value="${bookToUpdate.isbn13}" readonly>
                </label><br>
                <label>Title:
                    <input type="text" name="title" value="${bookToUpdate.title}">
                </label><br>
                <label>Year published:
                    <input type="text" name="yearPublished" value="${bookToUpdate.yearPublished}">
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
                    <option value="${category.name}">
                        ${category.name}
                    </option>
                </c:forEach>
            </select>
        </fieldset>
        <input type="submit" value="Update book">
    </form>
</body>
</html>