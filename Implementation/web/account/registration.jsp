<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration Page</title>
</head>
<body>

    <form action="/account/registration" method="post">
        <label>First Name:
            <input type="text" name="firstName" value="${account.user.firstName}">
        </label>
        <c:if test="${invalidFirstName}">
            ${message}
        </c:if>
        <br>
        <label>Last Name:
            <input type="text" name="lastName" value="${account.user.lastName}">
        </label>
        <c:if test="${invalidLastName}">
            ${message}
        </c:if>
        <br>
        <label>Email:
            <input type="email" name="email" value="${account.email}">
        </label>
        <c:if test="${invalidEmail}">
            ${message}
        </c:if>
        <br>
        <label>Nickname:
            <input type="text" name="login" value="${account.login}">
        </label>
        <c:if test="${invalidLogin}">
            ${message}
        </c:if>
        <br>
        <label>Password:
            <input type="password" name="password" value="">
        </label>
        <c:if test="${invalidPassword}">
            ${invalidPasswordMessage}
        </c:if>
        <br>
        <label>Confirm Password:
            <input type="password" name="confirmPassword" value="">
        </label>
        <c:if test="${invalidConfirmPassword}">
            ${invalidConfirmPasswordMessage}
        </c:if>
        <br>
        <input type="submit" value="Create Account">
    </form>

</body>
</html>
