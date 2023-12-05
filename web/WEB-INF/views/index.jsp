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
    <script>
        function submitForm(event) {
            event.preventDefault();

            const agencyId = document.getElementById('agencyId').value;
            const mallId = document.getElementById('mallId').value;

            fetch('/getAgencySiteStatus', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ agencyId: agencyId, mallId: mallId })
            })
                .then(response => {
                    if (response.ok) {
                        alert('Registration successful');
                        window.location.href = '/successPage'; // 성공 시 이동할 페이지 URL
                    } else {
                        response.text().then(text => alert('Error: ' + text));
                    }
                })
                .catch(error => alert('Error: ' + error));
        }
    </script>
</head>
<body>
<form onsubmit="submitForm(event)">
    <label for="agencyId">Agency ID:</label>
    <input type="text" id="agencyId" name="agencyId"><br><br>

    <label for="mallId">Mall ID:</label>
    <input type="text" id="mallId" name="mallId"><br><br>

    <input type="submit" value="Submit">
</form>
</body>
</html>

