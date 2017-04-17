<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>
    <link href="../styles/login.css" rel="stylesheet" type="text/css">
</head>
<body id="bodyStyle">
<div id="wrapper">

    <div id="loginForm">
        <form action="/account/registration" method="post">
            <input type="hidden" value="${referrer}" name="referrer">
            <h4 class="alert-danger">${errorMessage}</h4>
            <label class="control-label">First Name:
                <input class="form-control" type="text" name="firstName" value="<c:out value='${user.firstName}' />">
            </label>
            <br>
            <label class="control-label">Last Name:
                <input class="form-control" type="text" name="lastName" value="<c:out value='${user.lastName}' />">
            </label>
            <br>
            <label class="control-label">Email:
                <input class="form-control" type="email" name="email" value="<c:out value='${user.account.email}' />">
            </label>
            <br>
            <label class="control-label">Nickname:
                <input class="form-control" type="text" name="username" value="<c:out value='${user.account.login}' />">
            </label>
            <br>
            <label class="control-label">Password:
                <input class="form-control" type="password" name="password" value="">
            </label>
            <br>
            <label class="control-label">Confirm Password:
                <input class="form-control" type="password" name="confirmPassword" value="">
            </label>
            <br>
            <input id="submit-button" type="submit" value="Create Account">
        </form>
    </div>
</div>
<p>&nbsp;</p>
</body>
</html>

