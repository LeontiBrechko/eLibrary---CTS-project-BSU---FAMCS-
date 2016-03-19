<%--
  Created by IntelliJ IDEA.
  User: Leonti
  Date: 2016-03-10
  Time: 2:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div>
    <p>Books in list: ${fn:length(account.downloadList)}</p>
    <a href="/download/downloadList?action=showDownloadList">Go to list</a>
</div>
