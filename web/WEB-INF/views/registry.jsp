<%--
  Created by IntelliJ IDEA.
  User: almondshin
  Date: 2023-12-05
  Time: 오후 2:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>가입</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script>

        function submitForm(event) {
            event.preventDefault(); // 기본 폼 제출 동작 방지

            var formData = new FormData(event.target); // 폼 데이터 수집

            $.ajax({
                url: '/registerMerchant',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(
                    {
                        "agency": {
                            agencyId: document.getElementById('agencyId').value,
                            mallId: document.getElementById('mallId').value
                        },
                        "client": {
                            clientId: document.getElementById('mallId').value,
                            companyName: document.getElementById('companyName').value,
                            businessType: document.getElementById('businessType').value,
                            bizNumber: document.getElementById('bizNumber').value,
                            ceoName: document.getElementById('ceoName').value,
                            phoneNumber: document.getElementById('phoneNumber').value,
                            address: document.getElementById('address').value,
                            companySite: document.getElementById('companySite').value,
                            email: document.getElementById('email').value
                        },
                        "settleManager": {
                            settleManagerName: document.getElementById('settleManagerName').value,
                            settleManagerPhoneNumber: document.getElementById('settleManagerPhoneNumber').value,
                            settleManagerEmail: document.getElementById('settleManagerEmail').value
                        }
                    }),
                success: function(response) {
                    // 성공적인 응답 처리
                },
                error: function(xhr, status, error) {
                    alert('Error: ' + xhr.responseText);
                },
                statusCode: {
                    200: function (){
                        alert("성공")
                    },
                    204: function() {
                        alert('No content available');
                    },
                    400: function() {
                        alert('Bad request error');
                    }
                }
            });
        }
    </script>
</head>
<body>
<h1>가입</h1>
<form onsubmit="submitForm(event)">
    제휴사 : <input readonly="readonly" value="${sessionAgencyId}" name="agencyId" id="agencyId"> <br>
    상점Id : <input readonly="readonly" name="mallId" id="mallId" value="${sessionMallId}"><br>
    <input type="hidden" name="clientId" id="clientId">
    회사명 : <input name="companyName" id="companyName"><br>
    사업자 구분 : <input name="businessType" id="businessType"><br>
    사업자 등록번호 또는 기관번호 : <input name="bizNumber" id="bizNumber"><br>
    대표이사명 : <input name="ceoName" id="ceoName"><br>
    이용기관 전화번호 : <input name="phoneNumber" id="phoneNumber"><br>
    이용기관 회사주소 : <input name="address" id="address"><br>
    이용기관 홈페이지 : <input name="companySite" id="companySite"><br>
    이용기관 이메일 : <input name="email" id="email"><br>
    정산담당자 이름 : <input name="settleManagerName" id="settleManagerName"><br>
    정산담당자 전화번호 : <input name="settleManagerPhoneNumber" id="settleManagerPhoneNumber"><br>
    정산담당자 E-Mail : <input name="settleManagerEmail" id="settleManagerEmail"><br>
    <button type="submit">생성</button>
</form>

</body>
</html>
