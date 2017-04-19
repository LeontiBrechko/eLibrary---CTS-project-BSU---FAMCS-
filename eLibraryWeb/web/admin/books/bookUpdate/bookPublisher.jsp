<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="../../../styles/libraryAccounts.css" rel="stylesheet" type="text/css">
    <link href="../../../styles/mainPage.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <jsp:include page="../../../includes/navigationBar.jsp"/>
    <h4 class="alert-danger">${errorMessage}</h4>
    <form action="${pageContext.request.contextPath}/admin/bookManagement" method="post">
        <input type="hidden" name="action" value="updateBookPublisher">
        <div>
            <input type="radio" name="updateType" value="selectPublisher" checked>
            <select class="form-control" name="selectedPublisher" size="10">
                <c:forEach var="publisher" items="${publishers}">
                    <option value="<c:out value="${publisher.name}"/>">
                        <c:out value="${publisher.name}"/>
                    </option>
                </c:forEach>
            </select>
        </div>

        <br>

        <div>
            <input type="radio" name="updateType" value="addPublisher">
            <label class="control-label">Name:
                <input class="form-control" type="text" name="name">
            </label><br>
            <label class="control-label">Country:
                <input class="form-control" type="text" name="country">
            </label>
            <label class="control-label">City:
                <input class="form-control" type="text" name="city">
            </label>
            <label class="control-label">State:
                <input class="form-control" type="text" name="state">
            </label>
            <label class="control-label">Street number:
                <input class="form-control" type="text" name="streetNumber">
            </label>
            <label class="control-label">Street name:
                <input class="form-control" type="text" name="streetName">
            </label>
        </div>
        <input id="submit-button" type="submit" value="Add publisher">
    </form>
</div>
</body>
</html>
