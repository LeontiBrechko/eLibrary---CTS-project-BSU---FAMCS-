<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-08
  Time: 10:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link href="../styles/login.css" rel="stylesheet" type="text/css">
</head>
<body id="bodyStyle">
<div id="wrapper">
    <div id="loginForm">
        <h1 id="title">Login Form</h1>
        <p id="title">Please, enter your username and password</p>
        <div class="alert-danger">${errorMessage}</div>
        <form action="/account/login" method="post">
            <input type="hidden" name="action" value="login">
            <label class="control-label">Username:
                <input class = "form-control" type="text" name="emailOrUsername">
            </label><br>
            <label class="control-label">Password:
                <input class = "form-control" type="password" name="password">
            </label><br>
            <input id="submit-button" type="submit" value="Login">
        </form>
        <form action="/account/login" method="post">
            <input  type="hidden" name="action" value="signUp">
            <input id="signin-button" type="submit" value="Sign Up">
        </form>
    </div>
</div>
</body>
</html>
