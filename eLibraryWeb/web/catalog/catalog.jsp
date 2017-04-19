<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="../styles/mainPage.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <p class="alert-danger">${errorMessage}</p>
    <jsp:include page="../includes/navigationBar.jsp"/>
    <jsp:include page="../includes/search.jsp"/>
    <div class="sidebar">
        <ul>
            <c:forEach var="category" items="${categories}">
                <li><a href="${pageContext.request.contextPath}/catalog?category=${category.name}">${category.name}</a></li>
            </c:forEach>
        </ul>
    </div>
    <div id="content">
        <c:forEach var="book" items="${books}">
            <div class="book">
                <label>${book.title}<br>
                    <img src="<c:url value="${book.thumbnail}" />" alt="${book.title}"><br>
                    <a href="${pageContext.request.contextPath}/catalog/description?action=showDescription&amp;isbn13=${book.isbn13}">
                        Description
                    </a>
                </label>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>




