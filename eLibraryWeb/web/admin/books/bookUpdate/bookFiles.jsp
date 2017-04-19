<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link href="../../../styles/libraryAccounts.css" rel="stylesheet" type="text/css">
    <link href="../../../styles/mainPage.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <jsp:include page="../../../includes/navigationBar.jsp"/>
    <h3 id="title">Current book files:</h3>
    <h4 class="alert-danger">${errorMessage}</h4>
    <table>
        <c:forEach var="file" items="${bookToUpdate.bookFiles}">
            <tr>
                <td>${file.format}</td>
                <td>${file.language}</td>
                <td>${file.path}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/bookManagement" method="post">
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
    <form action="${pageContext.request.contextPath}/admin/bookManagement" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="addBookFile">
        <label class="control-label">Format:
            <select class="form-control" name="selectedFormat">
                <c:forEach var="format" items="${formats}">
                    <option value="${format.name}">${format.name}</option>
                </c:forEach>
            </select>
        </label>
        <label class="control-label">Language:
            <select class="form-control" name="selectedLanguage">
                <c:forEach var="language" items="${languages}">
                    <option value="${language.name}">${language.name}</option>
                </c:forEach>
            </select>
        </label>
        <label class="control-label">File:
            <input class="form-control" type="file" name="file">
        </label>
        <input id="signin-button" type="submit" value="Add file">
    </form>
    <form action="${pageContext.request.contextPath}/admin/bookManagement" method="post">
        <input type="hidden" name="action" value="continue">
        <input type="hidden" name="nextStep" value="review">
        <input id="submit-button" type="submit" value="Proceed to next step">
    </form>
</div>
</body>
</html>