<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="utils.CookieUtil" %><%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-15
  Time: 10:35 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        Cookie[] cookies = request.getCookies();
        request.setAttribute("cookies", cookies);
        if (cookies != null) {
            CookieUtil.deleteAllCookies(cookies, response);
    %>
        <p>Cookies are deleted!</p>
    <%  } else {%>
        <p>No cookies found</p>
    <%}%>
    <br><a href="index.jsp">Home</a>
</body>
</html>
