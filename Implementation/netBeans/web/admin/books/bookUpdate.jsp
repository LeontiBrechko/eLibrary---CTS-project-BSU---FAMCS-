<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-17
  Time: 9:59 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="/admin/bookManagement" method="post" id="mainForm" enctype="multipart/form-data">
        <input type="hidden" name="action" value="updateBook">
        <fieldset form="mainForm">
            <legend>Main book information</legend>
                <input type="hidden" name="popularity" value="${book.popularity}">
                <label> ISBN-13:
                    <input type="text" name="isbn13" value="${book.isbn13}" readonly>
                </label><br>
                <label>Title:
                    <input type="text" name="title" value="${book.title}">
                </label><br>
                <label>Year published:
                    <input type="text" name="yearPublished" value="${book.yearPublished}">
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
            <legend>Book files</legend>
            <c:forEach var="file" items="${book.files}">
                <label>Language:
                    <input type="text" name="language" value="${file.language}" readonly>
                </label>
                <label>Format:
                    <input type="text" name="format" value="${file.format}" readonly>
                </label>
                <a href="${file.path}" download>Preview file</a>
                <a href="">Delete file</a>
            </c:forEach>
        </fieldset>
        <input type="submit" value="Update book">
    </form>
</body>
</html>
