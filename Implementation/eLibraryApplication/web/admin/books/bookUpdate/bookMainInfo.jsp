<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-17
  Time: 9:59 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="data.CategoryDB" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="utils.dataValidation.DataValidationException" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    try {
        request.setAttribute("categories", CategoryDB.selectAllCategories());
    } catch (SQLException | DataValidationException e) {
        e.printStackTrace();
    }
%>
<html>
<head>
    <title>Title</title>
    <link href="../../../styles/libraryAccounts.css" rel="stylesheet" type="text/css">
    <link href="../../../styles/mainPage.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <h4 class="alert-danger">${errorMessage}</h4>
    <jsp:include page="../../../includes/navigationBar.jsp"/>
    <form action="/admin/bookManagement" method="post" id="mainForm" enctype="multipart/form-data"
          accept-charset="UTF-8">
        <input type="hidden" name="action" value="updateBookMainInfo">
        <fieldset form="mainForm">
            <legend>Main book information</legend>
            <input type="hidden" name="popularity" value="${bookToUpdate.popularity}">
            <label class="control-label"> ISBN-13:
                <c:choose>
                    <c:when test="${bookToUpdate.isbn13 != null}">
                        <input class="form-control" type="text" name="isbn13"
                               value="<c:out value="${bookToUpdate.isbn13}"/>" readonly>
                    </c:when>
                    <c:otherwise>
                        <input class="form-control" type="text" name="isbn13"
                               value="<c:out value="${bookToUpdate.isbn13}"/>">
                    </c:otherwise>
                </c:choose>
            </label><br>
            <label class="control-label">Title:
                <input class="form-control" type="text" name="title" value="<c:out value="${bookToUpdate.title}"/>">
            </label><br>
            <label class="control-label">Year published:
                <input class="form-control" type="text" name="yearPublished"
                       value="<c:out value="${bookToUpdate.yearPublished}"/>">
            </label><br>
            <label class="control-label">Description file:
                <input class="form-control" type="file" name="description">
            </label><br>
            <label class="control-label">Image:
                <input class="form-control" type="file" name="image">
            </label><br>
            <label class="control-label">Thumbnail:
                <input class="form-control" type="file" name="thumbnail">
            </label><br>
        </fieldset>
        <fieldset form="mainForm">
            <legend>Book categories</legend>
            <select class="form-control" name="selectedCategories" multiple="multiple" size="10">
                <c:forEach var="category" items="${categories}">
                    <option value="<c:out value="${category.name}"/>">
                        <c:out value="${category.name}"/>
                    </option>
                </c:forEach>
            </select>
        </fieldset>
        <input id="submit-button" type="submit" value="Update book">
    </form>
</div>
</body>
</html>