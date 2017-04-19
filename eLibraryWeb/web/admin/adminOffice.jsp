<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="../styles/libraryAccounts.css" rel="stylesheet" type="text/css">
    <link href="../styles/mainPage.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <jsp:include page="../includes/navigationBar.jsp"/>
    <h1 id="title">Admin Office</h1>
    <table id="adminTable">
        <tr>
            <th>Book management</th>
            <th>User management</th>
        </tr>
        <tr>
            <td>
                <ul>
                    <li><a href="/admin/bookManagement?action=showLibraryBooks">Show all library books</a></li>
                    <li><a href="/admin/books/bookUpdate/bookMainInfo.jsp">Add new book</a></li>
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
</div>
</body>
</html>



