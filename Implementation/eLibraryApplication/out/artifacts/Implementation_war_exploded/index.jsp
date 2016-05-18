<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-02-27
  Time: 9:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
    <jsp:include page="includes/accountBox.jsp" />
    <p>${errorMessage}</p>
    <a href="/account/register.jsp">Registration</a>
    <a href="/catalog/description?action=showDescription&amp;isbn13=9781593272203">Description of 'The Linux Programming Environment'</a>
    <a href="/catalog">Book Catalog</a>
    <a href="/deleteCookies.jsp">Delete cookies (Test only)</a>
    <a href="/download/downloadList?action=download">Download books</a>
    <a href="/admin/adminOffice.jsp">Admin office</a>
  </body>
</html>
