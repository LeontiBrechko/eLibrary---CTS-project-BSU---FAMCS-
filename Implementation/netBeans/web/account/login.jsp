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
    <title>Title</title>
</head>
<body>
    <h1>Login Form</h1>
    <p>Please, enter your username and password</p>
    <form action="/account/login" method="post">
        <input type="hidden" name="action" value="login">
        <label>Username:
            <input type="text" name="emailOrUsername">
        </label><br>
        <label>Password:
            <input type="password" name="password">
        </label><br>
        <input type="submit" value="Login">
    </form>
    <form action="/account/login" method="post">
        <input type="hidden" name="action" value="signUp">
        <input type="submit" value="Sign Up">
    </form>
</body>
</html>
