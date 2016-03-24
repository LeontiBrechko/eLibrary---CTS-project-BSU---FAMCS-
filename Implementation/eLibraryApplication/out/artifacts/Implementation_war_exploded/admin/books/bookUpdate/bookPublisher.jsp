<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-21
  Time: 2:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="data.PublisherDB" %>
<%@ page import="java.sql.SQLException" %>
<%
    try {
        request.setAttribute("publishers", PublisherDB.selectAllPublishers());
    } catch (SQLException e) {
        e.printStackTrace();
    }
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="/admin/bookManagement" method="post">
        <input type="hidden" name="action" value="updateBookPublisher">
        <div>
            <input type="radio" name="updateType" value="selectPublisher" checked>
            <select name="selectedPublisher" size="10">
                <c:forEach var="publisher" items="${publishers}">
                    <option value="${publisher.name}">
                            ${publisher.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <br>

        <div>
            <input type="radio" name="updateType" value="addPublisher">
            <label>Name:
                <input type="text" name="name">
            </label><br>
            <label>Country:
                <input type="text" name="country">
            </label>
            <label>City:
                <input type="text" name="city">
            </label>
            <label>State:
                <input type="text" name="state">
            </label>
            <label>Street number:
                <input type="text" name="streetNumber">
            </label>
            <label>Street name:
                <input type="text" name="streetName">
            </label>
        </div>
        <input type="submit" value="Add publisher">
    </form>
</body>
</html>
