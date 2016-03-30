<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration Page</title>
</head>
<body>

    <form action="/account/registration" method="post">
        <input type="hidden" value="${referrer}" name="referrer">
        <h4>${errorMessage}</h4>
        <label>First Name:
            <input type="text" name="firstName" value="<c:out value='${firstName}' />">
        </label><br>
        <label>Last Name:
            <input type="text" name="lastName" value="<c:out value='${lastName}' />">
        </label><br>
        <label>Email:
            <input type="email" name="email" value="<c:out value='${email}' />">
        </label><br>
        <label>Username:
            <input type="text" name="username" value="<c:out value='${username}' />">
        </label><br>
        <label>Password:
            <input type="password" name="password" value="">
        </label><br>
        <label>Confirm Password:
            <input type="password" name="confirmPassword" value="">
        </label><br>
        <input type="submit" value="Create Account">
    </form>

</body>
</html>
