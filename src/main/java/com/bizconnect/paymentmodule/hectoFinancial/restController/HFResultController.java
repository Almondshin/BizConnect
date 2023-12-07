package com.bizconnect.paymentmodule.hectoFinancial.restController;

import com.bizconnect.paymentmodule.config.hectofinancial.Constant;
import com.bizconnect.paymentmodule.hectoFinancial.service.ConnectHectoFinancialService;
import com.bizconnect.paymentmodule.utils.EncryptUtil;
import com.bizconnect.paymentmodule.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/hectoFinancial/result")
public class HFResultController {
    Logger logger = LoggerFactory.getLogger("HFResultController");
    private final Constant constant;

    private final ConnectHectoFinancialService connectHectoFinancialService;

    public HFResultController(Constant constant, ConnectHectoFinancialService connectHectoFinancialService) {
        this.constant = constant;
        this.connectHectoFinancialService = connectHectoFinancialService;
    }

    // 결과 페이지 이후 콜백 S2S 안돼서 임시 처리
    @PostMapping("/next")
    public void next(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> RES_PARAMS = new LinkedHashMap<>();

        String method = StringUtil.isNull(request.getParameter("method"));

        if (method.equals("card")) {
            RES_PARAMS.put("mchtTrdNo", StringUtil.isNull(request.getParameter("mchtTrdNo")));
            RES_PARAMS.put("pointTrdNo", StringUtil.isNull(request.getParameter("pointTrdNo")));
            RES_PARAMS.put("trdNo", StringUtil.isNull(request.getParameter("trdNo")));
            RES_PARAMS.put("vtlAcntNo", StringUtil.isNull(request.getParameter("vtlAcntNo")));
            RES_PARAMS.put("mchtCustId", StringUtil.isNull(request.getParameter("mchtCustId")));
            RES_PARAMS.put("fnNm", StringUtil.isNull(request.getParameter("fnNm")));
            RES_PARAMS.put("method", StringUtil.isNull(request.getParameter("method")));
            RES_PARAMS.put("authNo", StringUtil.isNull(request.getParameter("authNo")));
            RES_PARAMS.put("trdAmt", StringUtil.isNull(request.getParameter("trdAmt")));
            RES_PARAMS.put("pointTrdAmt", StringUtil.isNull(request.getParameter("pointTrdAmt")));
            RES_PARAMS.put("cardTrdAmt", StringUtil.isNull(request.getParameter("cardTrdAmt")));
            RES_PARAMS.put("outRsltMsg", StringUtil.isNull(request.getParameter("outRsltMsg")));
            RES_PARAMS.put("mchtParam", StringUtil.isNull(request.getParameter("mchtParam")));
            RES_PARAMS.put("outStatCd", StringUtil.isNull(request.getParameter("outStatCd")));
            RES_PARAMS.put("outRsltCd", StringUtil.isNull(request.getParameter("outRsltCd")));
            RES_PARAMS.put("intMon", StringUtil.isNull(request.getParameter("intMon")));
            RES_PARAMS.put("authDt", StringUtil.isNull(request.getParameter("authDt")));
            RES_PARAMS.put("mchtId", StringUtil.isNull(request.getParameter("mchtId")));
            RES_PARAMS.put("fnCd", StringUtil.isNull(request.getParameter("fnCd")));
        }
        if (method.equals("vbank")) {
            RES_PARAMS.put("mchtTrdNo", StringUtil.isNull(request.getParameter("mchtTrdNo")));
            RES_PARAMS.put("trdNo", StringUtil.isNull(request.getParameter("trdNo")));
            RES_PARAMS.put("vtlAcntNo", StringUtil.isNull(request.getParameter("vtlAcntNo")));
            RES_PARAMS.put("mchtCustId", StringUtil.isNull(request.getParameter("mchtCustId")));
            RES_PARAMS.put("fnNm", StringUtil.isNull(request.getParameter("fnNm")));
            RES_PARAMS.put("method", StringUtil.isNull(request.getParameter("method")));
            RES_PARAMS.put("trdAmt", StringUtil.isNull(request.getParameter("trdAmt")));
            RES_PARAMS.put("outRsltMsg", StringUtil.isNull(request.getParameter("outRsltMsg")));
            RES_PARAMS.put("mchtParam", StringUtil.isNull(request.getParameter("mchtParam")));
            RES_PARAMS.put("expireDt", StringUtil.isNull(request.getParameter("expireDt")));
            RES_PARAMS.put("outStatCd", StringUtil.isNull(request.getParameter("outStatCd")));
            RES_PARAMS.put("outRsltCd", StringUtil.isNull(request.getParameter("outRsltCd")));
            RES_PARAMS.put("authDt", StringUtil.isNull(request.getParameter("authDt")));
            RES_PARAMS.put("mchtId", StringUtil.isNull(request.getParameter("mchtId")));
            RES_PARAMS.put("fnCd", StringUtil.isNull(request.getParameter("fnCd")));
        }

        System.out.println(RES_PARAMS);
        ObjectMapper objectMapper = new ObjectMapper();
        String data = Base64.getEncoder().encodeToString(objectMapper.writeValueAsString(RES_PARAMS).getBytes());
        response.sendRedirect("http://127.0.0.1:9000/end.html?data=" + data);
    }

    // 헥토파이낸셜 서버 요청, 현 서버 수신 - 로컬 사용 불가
    @PostMapping("/noti")
    public String noti(HttpServletRequest request) {
        /** 응답 파라미터 세팅 */
        Map<String, String> RES_PARAMS = new LinkedHashMap<>();
        String method = StringUtil.isNull(request.getParameter("method"));
        if (method.equals("CA")) {
            RES_PARAMS.put("outStatCd", StringUtil.isNull(request.getParameter("outStatCd")));
            RES_PARAMS.put("trdNo", StringUtil.isNull(request.getParameter("trdNo")));
            RES_PARAMS.put("method", StringUtil.isNull(request.getParameter("method")));
            RES_PARAMS.put("bizType", StringUtil.isNull(request.getParameter("bizType")));
            RES_PARAMS.put("mchtId", StringUtil.isNull(request.getParameter("mchtId")));
            RES_PARAMS.put("mchtTrdNo", StringUtil.isNull(request.getParameter("mchtTrdNo")));
            RES_PARAMS.put("mchtCustNm", StringUtil.isNull(request.getParameter("mchtCustNm")));
            RES_PARAMS.put("mchtName", StringUtil.isNull(request.getParameter("mchtName")));
            RES_PARAMS.put("pmtprdNm", StringUtil.isNull(request.getParameter("pmtprdNm")));
            RES_PARAMS.put("trdDtm", StringUtil.isNull(request.getParameter("trdDtm")));
            RES_PARAMS.put("trdAmt", StringUtil.isNull(request.getParameter("trdAmt")));
            RES_PARAMS.put("billKey", StringUtil.isNull(request.getParameter("billKey")));
            RES_PARAMS.put("billKeyExpireDt", StringUtil.isNull(request.getParameter("billKeyExpireDt")));
            RES_PARAMS.put("bankCd", StringUtil.isNull(request.getParameter("bankCd")));
            RES_PARAMS.put("bankNm", StringUtil.isNull(request.getParameter("bankNm")));
            RES_PARAMS.put("cardCd", StringUtil.isNull(request.getParameter("cardCd")));
            RES_PARAMS.put("cardNm", StringUtil.isNull(request.getParameter("cardNm")));
            RES_PARAMS.put("telecomCd", StringUtil.isNull(request.getParameter("telecomCd")));
            RES_PARAMS.put("telecomNm", StringUtil.isNull(request.getParameter("telecomNm")));
            RES_PARAMS.put("vAcntNo", StringUtil.isNull(request.getParameter("vAcntNo")));
            RES_PARAMS.put("expireDt", StringUtil.isNull(request.getParameter("expireDt")));
            RES_PARAMS.put("AcntPrintNm", StringUtil.isNull(request.getParameter("AcntPrintNm")));
            RES_PARAMS.put("dpstrNm", StringUtil.isNull(request.getParameter("dpstrNm")));
            RES_PARAMS.put("email", StringUtil.isNull(request.getParameter("email")));
            RES_PARAMS.put("mchtCustId", StringUtil.isNull(request.getParameter("mchtCustId")));
            RES_PARAMS.put("cardNo", StringUtil.isNull(request.getParameter("cardNo")));
            RES_PARAMS.put("cardApprNo", StringUtil.isNull(request.getParameter("cardApprNo")));
            RES_PARAMS.put("instmtMon", StringUtil.isNull(request.getParameter("instmtMon")));
            RES_PARAMS.put("instmtType", StringUtil.isNull(request.getParameter("instmtType")));
            RES_PARAMS.put("phoneNoEnc", StringUtil.isNull(request.getParameter("phoneNoEnc")));
            RES_PARAMS.put("orgTrdNo", StringUtil.isNull(request.getParameter("orgTrdNo")));
            RES_PARAMS.put("orgTrdDt", StringUtil.isNull(request.getParameter("orgTrdDt")));
            RES_PARAMS.put("mixTrdNo", StringUtil.isNull(request.getParameter("mixTrdNo")));
            RES_PARAMS.put("mixTrdAmt", StringUtil.isNull(request.getParameter("mixTrdAmt")));
            RES_PARAMS.put("payAmt", StringUtil.isNull(request.getParameter("payAmt")));
            RES_PARAMS.put("csrcIssNo", StringUtil.isNull(request.getParameter("csrcIssNo")));
            RES_PARAMS.put("cnclType", StringUtil.isNull(request.getParameter("cnclType")));
            RES_PARAMS.put("mchtParam", StringUtil.isNull(request.getParameter("mchtParam")));
            RES_PARAMS.put("pktHash", StringUtil.isNull(request.getParameter("pktHash")));
        }
        if (method.equals("VA")) {
            RES_PARAMS.put("outStatCd", StringUtil.isNull(request.getParameter("outStatCd")));
            RES_PARAMS.put("trdNo", StringUtil.isNull(request.getParameter("trdNo")));
            RES_PARAMS.put("method", StringUtil.isNull(request.getParameter("method")));
            RES_PARAMS.put("bizType", StringUtil.isNull(request.getParameter("bizType")));
            RES_PARAMS.put("mchtId", StringUtil.isNull(request.getParameter("mchtId")));
            RES_PARAMS.put("mchtTrdNo", StringUtil.isNull(request.getParameter("mchtTrdNo")));
            RES_PARAMS.put("mchtCustNm", StringUtil.isNull(request.getParameter("mchtCustNm")));
            RES_PARAMS.put("mchtName", StringUtil.isNull(request.getParameter("mchtName")));
            RES_PARAMS.put("pmtprdNm", StringUtil.isNull(request.getParameter("pmtprdNm")));
            RES_PARAMS.put("trdDtm", StringUtil.isNull(request.getParameter("trdDtm")));
            RES_PARAMS.put("trdAmt", StringUtil.isNull(request.getParameter("trdAmt")));
            RES_PARAMS.put("bankCd", StringUtil.isNull(request.getParameter("bankCd")));
            RES_PARAMS.put("bankNm", StringUtil.isNull(request.getParameter("bankNm")));
            RES_PARAMS.put("acntType", StringUtil.isNull(request.getParameter("acntType")));
            RES_PARAMS.put("vAcntNo", StringUtil.isNull(request.getParameter("vAcntNo")));
            RES_PARAMS.put("expireDt", StringUtil.isNull(request.getParameter("expireDt")));
            RES_PARAMS.put("AcntPrintNm", StringUtil.isNull(request.getParameter("AcntPrintNm")));
            RES_PARAMS.put("dpstrNm", StringUtil.isNull(request.getParameter("dpstrNm")));
            RES_PARAMS.put("email", StringUtil.isNull(request.getParameter("email")));
            RES_PARAMS.put("mchtCustId", StringUtil.isNull(request.getParameter("mchtCustId")));
            RES_PARAMS.put("orgTrdNo", StringUtil.isNull(request.getParameter("orgTrdNo")));
            RES_PARAMS.put("orgTrdDt", StringUtil.isNull(request.getParameter("orgTrdDt")));
            RES_PARAMS.put("csrcIssNo", StringUtil.isNull(request.getParameter("csrcIssNo")));
            RES_PARAMS.put("cnclType", StringUtil.isNull(request.getParameter("cnclType")));
            RES_PARAMS.put("mchtParam", StringUtil.isNull(request.getParameter("mchtParam")));
            RES_PARAMS.put("pktHash", StringUtil.isNull(request.getParameter("pktHash")));
        }

        boolean resp = false;
        /** 해쉬 조합 필드
         *  결과코드 + 거래일시 + 상점아이디 + 가맹점거래번호 + 거래금액 + 라이센스키 */
        String hashPlain = String.format("%s%s%s%s%s%s", RES_PARAMS.get("outStatCd"), RES_PARAMS.get("trdDtm"), RES_PARAMS.get("mchtId"), RES_PARAMS.get("mchtTrdNo"), RES_PARAMS.get("trdAmt"), constant.LICENSE_KEY);
        String hashCipher = "";

        /** SHA256 해쉬 처리 */
        try {
            hashCipher = EncryptUtil.digestSHA256(hashPlain);//해쉬 값
        }
        catch (Exception e) {
            logger.error("[" +
                    RES_PARAMS.get("mchtTrdNo") +
                    "][SHA256 HASHING] Hashing Fail! : " +
                    e.toString());
        }
        finally {
            logger.info("[" +
                    RES_PARAMS.get("mchtTrdNo") +
                    "][SHA256 HASHING] Plain Text[" +
                    hashPlain +
                    "] ---> Cipher Text[" +
                    hashCipher +
                    "]");
        }

        /**
         hash데이타값이 맞는 지 확인 하는 루틴은 헥토파이낸셜에서 받은 데이타가 맞는지 확인하는 것이므로 꼭 사용하셔야 합니다
         정상적인 결제 건임에도 불구하고 노티 페이지의 오류나 네트웍 문제 등으로 인한 hash 값의 오류가 발생할 수도 있습니다.
         그러므로 hash 오류건에 대해서는 오류 발생시 원인을 파악하여 즉시 수정 및 대처해 주셔야 합니다.
         그리고 정상적으로 데이터를 처리한 경우에도 헥토파이낸셜에서 응답을 받지 못한 경우는 결제결과가 중복해서 나갈 수 있으므로 관련한 처리도 고려되어야 합니다
         */
        if (hashCipher.equals(RES_PARAMS.get("pktHash"))) {
            logger.info("[" +
                    RES_PARAMS.get("mchtTrdNo") +
                    "][SHA256 Hash Check] hashCipher[" +
                    hashCipher +
                    "] pktHash[" +
                    RES_PARAMS.get("pktHash") +
                    "] equals?[TRUE]");
            if ("0021".equals(RES_PARAMS.get("outStatCd"))) {
                // resp = notiSuccess(noti); // 성공 처리
            }
            else {
                resp = false;
            }
        }
        else {
            logger.info("[" +
                    RES_PARAMS.get("mchtTrdNo") +
                    "][SHA256 Hash Check] hashCipher[" +
                    hashCipher +
                    "] pktHash[" +
                    RES_PARAMS.get("pktHash") +
                    "] equals?[FALSE]");
            // resp = notiHashError(noti); // 실패 처리
        }

        // OK, FAIL문자열은 헥토파이낸셜로 전송되어야 하는 값이므로 변경하거나 삭제하지마십시오.
        if (resp) {
            logger.info("[" +
                    RES_PARAMS.get("mchtTrdNo") +
                    "][Result] OK");
            return "OK";
        }
        else {
            logger.info("[" +
                    RES_PARAMS.get("mchtTrdNo") +
                    "][Result] FAIL");
            return "FAIL";
        }
    }

    @PostMapping("/decrypt")
    public String result(HttpServletRequest request, @RequestBody Map<String, String> requestMap) throws
            IOException {
        String encodeData = requestMap.get("data");
        System.out.println(encodeData);
        String decodeData = new String(Base64.getDecoder().decode(encodeData.getBytes()), StandardCharsets.UTF_8);
        System.out.println(decodeData);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> resMap = objectMapper.readValue(decodeData, new TypeReference<>() {
        });
        System.out.println(resMap);

        /** 응답 파라미터 세팅 */
        Map<String, String> RES_PARAMS = new LinkedHashMap<>();

        /** 설정 정보 저장 */
        String aesKey = constant.AES256_KEY;

        if (resMap !=
                null) {
            RES_PARAMS = resMap;
        }
        else {
            RES_PARAMS.put("mchtId", StringUtil.isNull(request.getParameter("mchtId")));             //상점아이디
            RES_PARAMS.put("outStatCd", StringUtil.isNull(request.getParameter("outStatCd")));          //결과코드
            RES_PARAMS.put("outRsltCd", StringUtil.isNull(request.getParameter("outRsltCd")));          //거절코드
            RES_PARAMS.put("outRsltMsg", StringUtil.isNull(request.getParameter("outRsltMsg")));         //결과메세지
            RES_PARAMS.put("method", StringUtil.isNull(request.getParameter("method")));             //결제수단
            RES_PARAMS.put("mchtTrdNo", StringUtil.isNull(request.getParameter("mchtTrdNo")));          //상점주문번호
            RES_PARAMS.put("mchtCustId", StringUtil.isNull(request.getParameter("mchtCustId")));         //상점고객아이디
            RES_PARAMS.put("trdNo", StringUtil.isNull(request.getParameter("trdNo")));              //세틀뱅크 거래번호
            RES_PARAMS.put("trdAmt", StringUtil.isNull(request.getParameter("trdAmt")));             //거래금액
            RES_PARAMS.put("mchtParam", StringUtil.isNull(request.getParameter("mchtParam")));          //상점 예약필드
            RES_PARAMS.put("authDt", StringUtil.isNull(request.getParameter("authDt")));             //승인일시
            RES_PARAMS.put("authNo", StringUtil.isNull(request.getParameter("authNo")));             //승인번호
            RES_PARAMS.put("reqIssueDt", StringUtil.isNull(request.getParameter("reqIssueDt")));        //채번요청일시
            RES_PARAMS.put("intMon", StringUtil.isNull(request.getParameter("intMon")));             //할부개월수
            RES_PARAMS.put("fnNm", StringUtil.isNull(request.getParameter("fnNm")));               //카드사명
            RES_PARAMS.put("fnCd", StringUtil.isNull(request.getParameter("fnCd")));               //카드사코드
            RES_PARAMS.put("pointTrdNo", StringUtil.isNull(request.getParameter("pointTrdNo")));         //포인트거래번호
            RES_PARAMS.put("pointTrdAmt", StringUtil.isNull(request.getParameter("pointTrdAmt")));        //포인트거래금액
            RES_PARAMS.put("cardTrdAmt", StringUtil.isNull(request.getParameter("cardTrdAmt")));         //신용카드결제금액
            RES_PARAMS.put("vtlAcntNo", StringUtil.isNull(request.getParameter("vtlAcntNo")));          //가상계좌번호
            RES_PARAMS.put("expireDt", StringUtil.isNull(request.getParameter("expireDt")));           //입금기한
            RES_PARAMS.put("cphoneNo", StringUtil.isNull(request.getParameter("cphoneNo")));           //휴대폰번호
            RES_PARAMS.put("billKey", StringUtil.isNull(request.getParameter("billKey")));            //자동결제키
            RES_PARAMS.put("csrcAmt", StringUtil.isNull(request.getParameter("csrcAmt")));            //현금영수증 발급 금액(네이버페이)
        }
        //AES256 복호화 필요 파라미터
        String[] DECRYPT_PARAMS = {"mchtCustId", "trdAmt", "pointTrdAmt", "cardTrdAmt", "vtlAcntNo", "cphoneNo", "csrcAmt"};

        // 파라미터 복호화
        connectHectoFinancialService.decryptParams(DECRYPT_PARAMS, RES_PARAMS, RES_PARAMS);

        return objectMapper.writeValueAsString(RES_PARAMS);
    }


}
