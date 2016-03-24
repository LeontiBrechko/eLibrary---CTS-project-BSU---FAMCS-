<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-24
  Time: 10:34 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Book update review</h1>
    <table>
        <tr>
            <td>ISBN-13: </td>
            <td>${bookToUpdate.isbn13}</td>
        </tr>
        <tr>
            <td>Title: </td>
            <td>${bookToUpdate.title}</td>
        </tr>
        <tr>
            <td>Year: </td>
            <td>${bookToUpdate.yearPublished}</td>
        </tr>
        <tr>
            <td>Description file path:</td>
            <td>${bookToUpdate.description}</td>
        </tr>
        <tr>
            <td>Image: </td>
            <td><img src="<c:url value="${bookToUpdate.image}" />"/></td>
        </tr>
        <tr>
            <td>Thumbnail: </td>
            <td><img src="<c:url value="${bookToUpdate.thumbnail}" />" /></td>
        </tr>
        <tr>
            <td>Book files:</td>
            <td>
                <c:forEach var="file" items="${bookToUpdate.files}">
                    <tr>
                        <td>${file.format}</td>
                        <td>${file.language}</td>
                        <td>${file.path}</td>
                        <td>
                            <form action="/admin/bookManagement" method="post">
                                <input type="hidden" name="action" value="deleteBookFile">
                                <input type="hidden" name="format" value="${file.format}">
                                <input type="hidden" name="language" value="${file.language}">
                                <input type="submit" value="Delete file">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </td>
        </tr>
        <tr>
            <td>Publisher:</td>
            <td>${bookToUpdate.publisher.name}</td>
        </tr>
        <tr>
            <td>Authors: </td>
            <td>
                <c:forEach var="author" items="${bookToUpdate.authors}">
                    ${author.firstName} ${author.lastName}<br>
                </c:forEach>
            </td>
        </tr>
        <tr>
            <td>Categories: </td>
            <td>
                <c:forEach var="category" items="${bookToUpdate.categories}">
                    ${category.name}<br>
                </c:forEach>
            </td>
        </tr>
    </table>
    <form action="/admin/bookManagement" method="post">
        <input type="hidden" name="action" value="updateBook">
        <input type="submit" value="Update book">
    </form>
</body>
</html>
