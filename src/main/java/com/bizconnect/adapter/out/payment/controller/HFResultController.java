package com.bizconnect.adapter.out.payment.controller;

import com.bizconnect.adapter.out.payment.model.HFDataModel;
import com.bizconnect.adapter.out.payment.service.ConnectHectoFinancialService;
import com.bizconnect.adapter.out.payment.service.HFResultService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/hectoFinancial/result")
public class HFResultController {


    private final ConnectHectoFinancialService connectHectoFinancialService;
    private final HFResultService hfResultService;

    public HFResultController(ConnectHectoFinancialService connectHectoFinancialService, HFResultService hfResultService) {
        this.connectHectoFinancialService = connectHectoFinancialService;
        this.hfResultService = hfResultService;
    }


    // 결과 페이지 이후 콜백 S2S 안돼서 임시 처리
    @PostMapping("/next")
    public void next(HttpServletRequest request, HttpServletResponse response, @RequestBody HFDataModel hfDataModel) throws IOException {
        System.out.println("next 요청왔어요");
        String method = request.getParameter("method");
        String data = "";
        if (method.equals("card")) {
            data = hfResultService.nextCardData(hfDataModel);
        }
        if (method.equals("vbank")) {
            data = hfResultService.nextVBankData(hfDataModel);
        }
        response.sendRedirect("http://127.0.0.1:9000/end.html?data=" + data);
    }

    // 헥토파이낸셜 서버 요청, 현 서버 수신 - 로컬 사용 불가
    @PostMapping("/noti")
    public void noti(HttpServletRequest request, @RequestBody HFDataModel hfDataModel) {
        System.out.println("noti 요청왔어요");
        /** 응답 파라미터 세팅 */
        String method = request.getParameter("method");
        if (method.equals("CA")) {
            hfResultService.notiCAData(hfDataModel);
        }
        if (method.equals("VA")) {
            hfResultService.notiVAData(hfDataModel);
        }
    }

//    @PostMapping("/decrypt")
//    public String result(HttpServletRequest request, @RequestBody Map<String, String> requestMap) throws
//            IOException {
//        String encodeData = requestMap.get("data");
//        System.out.println(encodeData);
//        String decodeData = new String(Base64.getDecoder().decode(encodeData.getBytes()), StandardCharsets.UTF_8);
//        System.out.println(decodeData);
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, String> resMap = objectMapper.readValue(decodeData, new TypeReference<>() {
//        });
//        System.out.println(resMap);
//
//        /** 응답 파라미터 세팅 */
//        Map<String, String> RES_PARAMS = new LinkedHashMap<>();
//
//        /** 설정 정보 저장 */
//        String aesKey = constant.AES256_KEY;
//
//        if (resMap !=
//                null) {
//            RES_PARAMS = resMap;
//        }
//        else {
//            RES_PARAMS.put("mchtId", StringUtil.isNull(request.getParameter("mchtId")));             //상점아이디
//            RES_PARAMS.put("outStatCd", StringUtil.isNull(request.getParameter("outStatCd")));          //결과코드
//            RES_PARAMS.put("outRsltCd", StringUtil.isNull(request.getParameter("outRsltCd")));          //거절코드
//            RES_PARAMS.put("outRsltMsg", StringUtil.isNull(request.getParameter("outRsltMsg")));         //결과메세지
//            RES_PARAMS.put("method", StringUtil.isNull(request.getParameter("method")));             //결제수단
//            RES_PARAMS.put("mchtTrdNo", StringUtil.isNull(request.getParameter("mchtTrdNo")));          //상점주문번호
//            RES_PARAMS.put("mchtCustId", StringUtil.isNull(request.getParameter("mchtCustId")));         //상점고객아이디
//            RES_PARAMS.put("trdNo", StringUtil.isNull(request.getParameter("trdNo")));              //세틀뱅크 거래번호
//            RES_PARAMS.put("trdAmt", StringUtil.isNull(request.getParameter("trdAmt")));             //거래금액
//            RES_PARAMS.put("mchtParam", StringUtil.isNull(request.getParameter("mchtParam")));          //상점 예약필드
//            RES_PARAMS.put("authDt", StringUtil.isNull(request.getParameter("authDt")));             //승인일시
//            RES_PARAMS.put("authNo", StringUtil.isNull(request.getParameter("authNo")));             //승인번호
//            RES_PARAMS.put("reqIssueDt", StringUtil.isNull(request.getParameter("reqIssueDt")));        //채번요청일시
//            RES_PARAMS.put("intMon", StringUtil.isNull(request.getParameter("intMon")));             //할부개월수
//            RES_PARAMS.put("fnNm", StringUtil.isNull(request.getParameter("fnNm")));               //카드사명
//            RES_PARAMS.put("fnCd", StringUtil.isNull(request.getParameter("fnCd")));               //카드사코드
//            RES_PARAMS.put("pointTrdNo", StringUtil.isNull(request.getParameter("pointTrdNo")));         //포인트거래번호
//            RES_PARAMS.put("pointTrdAmt", StringUtil.isNull(request.getParameter("pointTrdAmt")));        //포인트거래금액
//            RES_PARAMS.put("cardTrdAmt", StringUtil.isNull(request.getParameter("cardTrdAmt")));         //신용카드결제금액
//            RES_PARAMS.put("vtlAcntNo", StringUtil.isNull(request.getParameter("vtlAcntNo")));          //가상계좌번호
//            RES_PARAMS.put("expireDt", StringUtil.isNull(request.getParameter("expireDt")));           //입금기한
//            RES_PARAMS.put("cphoneNo", StringUtil.isNull(request.getParameter("cphoneNo")));           //휴대폰번호
//            RES_PARAMS.put("billKey", StringUtil.isNull(request.getParameter("billKey")));            //자동결제키
//            RES_PARAMS.put("csrcAmt", StringUtil.isNull(request.getParameter("csrcAmt")));            //현금영수증 발급 금액(네이버페이)
//        }
//        //AES256 복호화 필요 파라미터
//        String[] DECRYPT_PARAMS = {"mchtCustId", "trdAmt", "pointTrdAmt", "cardTrdAmt", "vtlAcntNo", "cphoneNo", "csrcAmt"};
//
//        // 파라미터 복호화
//        connectHectoFinancialService.decryptParams(DECRYPT_PARAMS, RES_PARAMS, RES_PARAMS);
//
//        return objectMapper.writeValueAsString(RES_PARAMS);
//    }

}


