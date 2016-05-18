<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="data.CategoryDB" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="utils.dataValidation.DataValidationException" %><%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-05-18
  Time: 3:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    try {
        request.setAttribute("categories", CategoryDB.selectAllCategories());
    } catch (SQLException | DataValidationException e) {
        e.printStackTrace();
    }
%>
<div class="sidebar">
    <ul>
        <c:forEach var="category" items="${categories}">
            <li><a href="/catalog?category=${category.name}">${category.name}</a></li>
        </c:forEach>
    </ul>
</div>
