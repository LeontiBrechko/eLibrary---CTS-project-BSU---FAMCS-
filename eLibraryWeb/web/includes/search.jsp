<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="input">
    <form method="get" action="${pageContext.request.contextPath}/catalog">
        <div><input type="text" name="search" class="form-control" placeholder="Enter name of a book..."></div>
        <div><input type="submit" class="search-button" value="Search"></div>
    </form>
</div>
