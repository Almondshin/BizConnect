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
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script>
        function submitForm(event) {
            event.preventDefault();

            const agencyId = document.getElementById('agencyId').value;
            const mallId = document.getElementById('mallId').value;

            $.ajax({
                url: '/getAgencySiteStatus',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ agencyId: agencyId, mallId: mallId }),
                success: function(response) {
                },
                error: function(xhr, status, error) {
                    alert('Error: ' + xhr.responseText);
                },
                statusCode: {
                    200: function (){
                        alert("성공")
                        window.location.href = '/requestPage'; // 성공 시 이동할 페이지 URL
                    },
                    204: function() {
                        alert('No content available');
                        window.location.href = '/registryPage'; // noContent일 때 이동할 페이지 URL
                    },
                    400: function() {
                        alert('Bad request error');
                        // 에러에 대한 추가적인 처리가 필요한 경우 여기에 작성
                    }
                }
            });
        }
    </script>

</head>
<body>
<form onsubmit="submitForm(event)">
    <label for="agencyId">Agency ID:</label><br>
    <select name="agencyId" id="agencyId" style="width: 20%;" data-searchable="default">
        <option value=''>전 체</option>
        <c:forEach var="agencyId" items="${enumAgencies}" varStatus="status">
            <option value="${agencyId.code}">${agencyId.code}(${agencyId.value})</option>
        </c:forEach>
    </select><br><br>

    <label for="mallId">Mall ID:</label><br>
    <input type="text" id="mallId" name="mallId"><br><br>

    <input type="submit" value="Submit">
</form>
</body>
</html>

