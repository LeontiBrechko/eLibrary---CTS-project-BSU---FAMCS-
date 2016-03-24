<%@ page import="models.enums.Format" %>
<%@ page import="models.enums.Language" %><%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-22
  Time: 3:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    request.setAttribute("formats", Format.values());
    request.setAttribute("languages", Language.values());
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h3>Current book files:</h3>
    <table>
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
                        <input type="hidden" name="path" value="${file.path}">
                        <input type="submit" value="Delete file">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <form action="/admin/bookManagement" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="addBookFile">
        <label>Format:
            <select name="selectedFormat">
                <c:forEach var="format" items="${formats}">
                    <option value="${format.name()}">${format.name()}</option>
                </c:forEach>
            </select>
        </label>
        <label>Language:
            <select name="selectedLanguage">
                <c:forEach var="language" items="${languages}">
                    <option value="${language.name()}">${language.name()}</option>
                </c:forEach>
            </select>
        </label>
        <label>File:
            <input type="file" name="file">
        </label>
        <input type="submit" value="Add file">
    </form>
    <form action="/admin/bookManagement" method="post">
        <input type="hidden" name="action" value="continue">
        <input type="hidden" name="nextStep" value="review">
        <input type="submit" value="Proceed to next step">
    </form>
</body>
</html>
