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
        function submitForm() {
            $.ajax({
                async: false,
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
                }
            });
        }

        function checkMallId() {
            const agencyId = document.getElementById('agencyId').value;
            const mallId = document.getElementById('mallId').value;

            $.ajax({
                async: false,
                url: '/status',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ agencyId: agencyId, mallId: mallId }),
                success: function(response) {
                    console.log(response);
                },
                error: function(xhr, textStatus, errorThrown) {
                    alert(xhr.responseText);
                    console.log(xhr.responseText);
                }
                // statusCode: {
                //     200: function (){
                //         window.location.href = '/requestPage'; // 성공 시 이동할 페이지 URL
                //     },
                //     204: function() {
                //         window.location.href = '/registryPage'; // noContent일 때 이동할 페이지 URL
                //     },
                //     400: function() {
                //         // 에러에 대한 추가적인 처리가 필요한 경우 여기에 작성
                //     }
                // }
            });
        }
    </script>
</head>
<body>
<h1>가입</h1>
<form >
    <input type="hidden" name="clientId" id="clientId">

<%--    제휴사 : <input readonly="readonly" value="${sessionAgencyId}" name="agencyId" id="agencyId"> <br>--%>
<%--    상점Id : <input readonly="readonly" name="mallId" id="mallId" value="${sessionMallId}"><br>--%>
    <table>
        <tr>
            <th>제휴사</th>
            <td>
                <select name="agencyId" id="agencyId" style="width: 100%;">
                    <option value=''>전 체</option>
                    <option value='SQUARES'>스퀘어스</option>
                    <option value='CAFE24'>카페24</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>상점Id</th>
            <td><input name="mallId" id="mallId"></td>
            <td><button onclick="checkMallId(); return false;">확인</button></td>
        </tr>
        <tr>
            <th>회사명</th>
            <td><input name="companyName" id="companyName"></td>
        </tr>
        <tr>
            <th>사업자 구분</th>
            <td><input name="businessType" id="businessType"></td>
        </tr>
        <tr>
            <th>사업자 등록번호 또는 기관번호</th>
            <td><input name="bizNumber" id="bizNumber"></td>
        </tr>
        <tr>
            <th>대표이사명</th>
            <td><input name="ceoName" id="ceoName"></td>
        </tr>
        <tr>
            <th>이용기관 전화번호</th>
            <td><input name="phoneNumber" id="phoneNumber"></td>
        </tr>
        <tr>
            <th>이용기관 회사주소</th>
            <td><input name="address" id="address"></td>
        </tr>
        <tr>
            <th>이용기관 홈페이지</th>
            <td><input name="companySite" id="companySite"></td>
        </tr>
        <tr>
            <th>이용기관 이메일</th>
            <td><input name="email" id="email"></td>
        </tr>
        <tr>
            <th>정산담당자 이름</th>
            <td><input name="settleManagerName" id="settleManagerName"></td>
        </tr>
        <tr>
            <th>정산담당자 전화번호</th>
            <td><input name="settleManagerPhoneNumber" id="settleManagerPhoneNumber"></td>
        </tr>
        <tr>
            <th>정산담당자 E-Mail</th>
            <td><input name="settleManagerEmail" id="settleManagerEmail"></td>
        </tr>
        <tr>
            <td>
                <button type="submit" onclick="submitForm(); return false;">생성</button>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
