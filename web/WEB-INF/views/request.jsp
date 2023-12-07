<%--
  Created by IntelliJ IDEA.
  User: almondshin
  Date: 2023-12-05
  Time: 오후 2:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>결제요청</h1>
sessionAgencyId : ${sessionAgencyId}<br>
sessionMallId : ${sessionMallId}

<form action="/paymentRequest" method="POST">
    <table class="info_tbl" summary="충전제 요금 안내표입니다.">
        <caption>월정액 요금 선택</caption>
        <tbody>
        <tr>
            <th width="7%" rowspan="2">선택</th>
            <th width="21%" rowspan="2">상품종류</th>
            <th width="36%" colspan="2">12개월</th>
            <th width="18%" rowspan="2">건당 요금</th>
            <th width="18%" rowspan="2">건당 초과 요금</th>
        </tr>
        <tr>
            <th>요금</th>
            <th>제공건수</th>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel1" value="0"></td>
            <td class="ac">라이트</td>
            <td class="ar">120,000 원</td>
            <td class="ar">2,400 건</td>
            <td class="ar">50 원</td>
            <td class="ar">50 원</td>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel2" value="1"></td>
            <td class="ac">베이직</td>
            <td class="ar">588,000 원</td>
            <td class="ar">12,000 건</td>
            <td class="ar">49 원</td>
            <td class="ar">50 원</td>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel3" value="2"></td>
            <td class="ac">스탠다드</td>
            <td class="ar">1,152,000 원</td>
            <td class="ar">24,000 건</td>
            <td class="ar">48 원</td>
            <td class="ar">50 원</td>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel4" value="3"></td>
            <td class="ac">프리미엄</td>
            <td class="ar">1,728,000 원</td>
            <td class="ar">36,000 건</td>
            <td class="ar">48 원</td>
            <td class="ar">50 원</td>
        </tr>
        </tbody>
    </table>
    <table class="info_tbl" summary="충전제 요금 안내표입니다.">
        <tbody>
        <tr>
            <th width="7%" rowspan="2">선택</th>
            <th width="21%" rowspan="2">상품종류</th>
            <th width="36%" colspan="2">6개월</th>
            <th width="18%" rowspan="2">건당 요금</th>
            <th width="18%" rowspan="2">건당 초과 요금</th>
        </tr>
        <tr>
            <th>요금</th>
            <th>제공건수</th>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel1" value="4"></td>
            <td class="ac">라이트</td>
            <td class="ar">60,000 원</td>
            <td class="ar">1,200 건</td>
            <td class="ar">50 원</td>
            <td class="ar">50 원</td>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel2" value="5"></td>
            <td class="ac">베이직</td>
            <td class="ar">294,000 원</td>
            <td class="ar">6,000 건</td>
            <td class="ar">49 원</td>
            <td class="ar">50 원</td>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel3" value="6"></td>
            <td class="ac">스탠다드</td>
            <td class="ar">576,000 원</td>
            <td class="ar">12,000 건</td>
            <td class="ar">48 원</td>
            <td class="ar">50 원</td>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel4" value="7"></td>
            <td class="ac">프리미엄</td>
            <td class="ar">864,000 원</td>
            <td class="ar">18,000 건</td>
            <td class="ar">48 원</td>
            <td class="ar">50 원</td>
        </tr>
        </tbody>
    </table>


    <table class="info_tbl" summary="월정액 요금 안내표입니다.">
        <caption>월정액 요금 선택</caption>
        <tbody>
        <tr>
            <th width="7%" rowspan="2">선택</th>
            <th width="21%" rowspan="2">상품종류</th>
            <th width="36%" colspan="2">1개월</th>
            <th width="18%" rowspan="2">건당 요금</th>
            <th width="18%" rowspan="2">건당 초과 요금</th>
        </tr>
        <tr>
            <th>요금</th>
            <th>제공건수</th>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel1" value="8"></td>
            <td class="ac">라이트</td>
            <td class="ar">10,000 원</td>
            <td class="ar">200 건</td>
            <td class="ar">50 원</td>
            <td class="ar">50 원</td>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel2" value="9"></td>
            <td class="ac">베이직</td>
            <td class="ar">49,000 원</td>
            <td class="ar">1,000 건</td>
            <td class="ar">49 원</td>
            <td class="ar">50 원</td>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel3" value="10"></td>
            <td class="ac">스탠다드</td>
            <td class="ar">96,000 원</td>
            <td class="ar">2,000 건</td>
            <td class="ar">48 원</td>
            <td class="ar">50 원</td>
        </tr>
        <tr>
            <td class="ac"><input type="radio" name="rate_sel" id="rate_sel4" value="11"></td>
            <td class="ac">프리미엄</td>
            <td class="ar">144,000 원</td>
            <td class="ar">3,000 건</td>
            <td class="ar">48 원</td>
            <td class="ar">50 원</td>
        </tr>
        </tbody>
    </table>


    <table class="info_tbl" summary="결제금액 확인 및 결제수단 선택표입니다.">
        <caption>결제금액 확인 및 결제수단 선택</caption>
        <tbody>
        <c:choose>
            <c:when test="${not empty extra_charge}">
                <tr>
                    <th width="15%" rowspan="2">총 결제금액 (VAT포함)</th>
                    <td width="35%" class="al"><span class="ft_org_02"><b>${salesPrice}
												원</b></span></td>
                </tr>
                <tr>
                    <td width="35%" class="al"><span class="ft_org_02"><b>${extra_charge}(초과사용분)
												+ ${origin_charge}(기본료) + VAT = ${salesPrice}</b></span></td>
                </tr>
            </c:when>
            <c:otherwise>
                <tr>
                    <th width="15%">총 결제금액 (VAT포함)</th>
                    <td width="35%" class="al"><span class="ft_org_02"><b>${salesPrice}
												원</b></span></td>
                </tr>
            </c:otherwise>
        </c:choose>
        <tr>
            <th width="15%">결제수단 선택</th>
            <td width="35%" class="al"><input type="radio"
                                              name="paymentKind" id="vAccount" value="2"
                                              onchange="javascript:cashReceiptChange()"/> <label
                    for="vAccount"><span class="pd_10"></span>무통장 입금</label> <span
                    class="pd_20"></span> <input type="radio" name="paymentKind"
                                                 id="creditCard" value="1"
                                                 onchange="javascript:cashReceiptChange()" checked="checked"/>
                <label for="creditCard"><span class="pd_10"></span>신용카드</label>
            </td>
        </tr>
        </tbody>
    </table>

</form>
</body>
</html>
