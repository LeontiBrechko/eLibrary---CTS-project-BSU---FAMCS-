<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-21
  Time: 2:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="data.AuthorDB" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="utils.dataValidation.DataValidationException" %>
<%
    try {
        request.setAttribute("authors", AuthorDB.selectAllAuthors());
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
    <jsp:include page="../../../includes/navigationBar.jsp"/>
    <form action="/admin/bookManagement" method="post">
        <input type="hidden" name="action" value="updateBookAuthors">
        <div>
            <input type="radio" name="updateType" value="selectAuthor" checked>
            <select class="form-control" name="selectedAuthors" multiple="multiple" size="10">
                <c:forEach var="author" items="${authors}">
                    <option value="<c:out value="${author.firstName} ${author.lastName}"/>">
                        <c:out value="${author.firstName} ${author.lastName}"/>
                    </option>
                </c:forEach>
            </select>
            <input id="signin-button" type="submit" value="Add authors">
        </div>

        <br>

        <div>
            <input type="radio" name="updateType" value="addAuthor">
            <label class="control-label">First name:
                <input class="form-control" type="text" name="firstName">
            </label><br>
            <label class="control-label">Second name:
                <input class="form-control" type="text" name="lastName">
            </label>
            <input id="signin-button" type="submit" value="Add author">
        </div>
    </form>

    <br>

    <c:if test="${fn:length(bookToUpdate.authors) gt 0}">
        Selected Authors:
        <table>
            <c:forEach var="author" items="${bookToUpdate.authors}">
                <tr>
                    <td><c:out value="${author.firstName}"/></td>
                    <td><c:out value="${author.lastName}"/></td>
                </tr>
            </c:forEach>
        </table>

        <br>
        <h4 align="center">To delete authors just select proper ones in selection list and press "Add authors"
            button</h4>
        <form action="/admin/bookManagement" method="post">
            <input type="hidden" name="action" value="continue">
            <input type="hidden" name="nextStep" value="publisher">
            <input id="submit-button" type="submit" value="Proceed to next step">
        </form>
    </c:if>
</div>
</body>
</html>
