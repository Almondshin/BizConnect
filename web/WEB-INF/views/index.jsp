<%--
  Created by IntelliJ IDEA.
  User: almondshin
  Date: 2023-12-05
  Time: 오후 1:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>첫 화면이올시다</title>
</head>
<body>
<form action="/getAgencySiteStatus" method="POST">
    <input name="agencyId">
    <input name="mallId">
    <button type="submit">확인</button>
</form>

</body>
</html>
