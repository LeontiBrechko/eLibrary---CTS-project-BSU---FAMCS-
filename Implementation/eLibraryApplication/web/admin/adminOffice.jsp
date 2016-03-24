<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-15
  Time: 3:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <table>
        <tr>
            <th>Book management</th>
            <th>User management</th>
        </tr>
        <tr>
            <td>
                <ul>
                    <li><a href="/admin/bookManagement?action=showLibraryBooks">Show all library books</a></li>
                </ul>
            </td>
            <td>
                <ul>
                    <li><a href="/admin/accountManagement?action=showAccounts">Show all library accounts</a></li>
                    <li><a href="/admin/accounts/adminRegistration.jsp">Register new admin</a></li>
                </ul>
            </td>
        </tr>
    </table>
</body>
</html>
