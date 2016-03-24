<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-21
  Time: 2:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="data.AuthorDB" %>
<%@ page import="java.sql.SQLException" %>
<%
    try {
        request.setAttribute("authors", AuthorDB.selectAllAuthors());
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
        <input type="hidden" name="action" value="updateBookAuthors">
        <div>
            <input type="radio" name="updateType" value="selectAuthor" checked>
            <select name="selectedAuthors" multiple="multiple" size="10">
                <c:forEach var="author" items="${authors}">
                    <option value="${author.firstName} ${author.lastName}">
                        ${author.firstName} ${author.lastName}
                    </option>
                </c:forEach>
            </select>
            <input type="submit" value="Add authors">
        </div>

        <br>

        <div>
            <input type="radio" name="updateType" value="addAuthor">
            <label>First name:
                <input type="text" name="firstName">
            </label><br>
            <label>Second name:
                <input type="text" name="lastName">
            </label>
            <input type="submit" value="Add author">
        </div>
    </form>

    <br>

    <c:if test="${bookToUpdate.authors != null}">
        Selected Authors:
        <table>
            <c:forEach var="author" items="${bookToUpdate.authors}">
                <tr>
                    <td>${author.firstName}</td>
                    <td>${author.lastName}</td>
                </tr>
            </c:forEach>
        </table>

        <br>
        <h4>To delete authors just select proper ones in selection list and press "Add authors" button</h4>
        <form action="/admin/bookManagement" method="post">
            <input type="hidden" name="action" value="continue">
            <input type="hidden" name="nextStep" value="publisher">
            <input type="submit" value="Proceed to next step">
        </form>
    </c:if>
</body>
</html>
