package com.bizconnect.paymentmodule.hectoFinancial.restController;

import com.bizconnect.paymentmodule.config.hectofinancial.Constant;
import com.bizconnect.paymentmodule.hectoFinancial.service.ConnectHectoFinancialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/hectoFinancial/VBank")
public class VBankController {
    Logger logger = LoggerFactory.getLogger("VBankController");
    private final Constant constant;
    private final ConnectHectoFinancialService connectHectoFinancialService;
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdf3 = new SimpleDateFormat("HHmmss");

    public VBankController(Constant constant, ConnectHectoFinancialService connectHectoFinancialService) {
        this.constant = constant;
        this.connectHectoFinancialService = connectHectoFinancialService;
    }

    @PostMapping(value = "/cancel")
    public String requestVBankCancel(@RequestBody Map<String, String> requestMap) throws Exception {
        Date date = new Date();
        //요청  파라미터(헤더)
        Map<String, String> REQ_HEADER = setREQ_HEADER(requestMap, date);
        //요청  파라미터(헤더)
        Map<String, String> REQ_BODY = setREQ_BODY(requestMap);

        connectHectoFinancialService.hashPkt(REQ_HEADER, REQ_BODY);

        //응답 파라미터(헤더)
        Map<String, String> RES_HEADER = setRES_HEADER();
        //응답 파라미터(바디)
        Map<String, String> RES_BODY = setRES_BODY();

        //요청파라미터 세팅
        //AES256 암호화 필요 파라미터
        String[] ENCRYPT_PARAMS = {"refundAcntNo", "vAcntNo", "cnclAmt", "taxAmt", "vatAmt", "taxFreeAmt"};
        //AES256 복호화 필요 파라미터
        String[] DECRYPT_PARAMS = {"cnclAmt", "blcAmt", "vAcntNo"};

        // 파라미터 암호화
        connectHectoFinancialService.encryptParams(ENCRYPT_PARAMS, REQ_HEADER, REQ_BODY);

        // 환불 요청
        Map<String, String> respParam = connectHectoFinancialService.requestCancelAPI(REQ_HEADER, REQ_BODY, RES_HEADER, RES_BODY);

        // 파라미터 복호화
        connectHectoFinancialService.decryptParams(DECRYPT_PARAMS, REQ_HEADER, respParam);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(respParam);
    }

    @PostMapping(value = "/test")
    public void test(HttpServletRequest request) {
    }

    public Map<String, String> setREQ_HEADER(Map<String, String> requestMap, Date date) {
        Map<String, String> REQ_HEADER = new LinkedHashMap<>();
        // params
        String ver = "0A19";
        String method = "VA";
        String bizType = "C0";
        String encCd = "23";
        String mchtId = requestMap.get("mchtId");
        String mchtTrdNo = "DREAMSEC" + sdf1.format(date);
        String trdDt = sdf2.format(date);
        String trdTm = sdf3.format(date);

        REQ_HEADER.put("mchtId", mchtId);
        REQ_HEADER.put("mchtTrdNo", mchtTrdNo);
        REQ_HEADER.put("ver", ver);
        REQ_HEADER.put("method", method);
        REQ_HEADER.put("bizType", bizType);
        REQ_HEADER.put("encCd", encCd);
        REQ_HEADER.put("trdDt", trdDt);
        REQ_HEADER.put("trdTm", trdTm);

        return REQ_HEADER;
    }

    public Map<String, String> setREQ_BODY(Map<String, String> requestMap) {
        Map<String, String> REQ_BODY = new LinkedHashMap<>();
        // data
        String orgTrdNo = requestMap.get("orgTrdNo");
        String crcCd = "KRW";
        String cnclOrd = "001";
        String cnclAmt = requestMap.get("cnclAmt");
        String refundBankCd = requestMap.get("refundBankCd");
        String refundAcntNo = requestMap.get("refundAcntNo");
        String refundDpstrNm = requestMap.get("refundDpstrNm");

        REQ_BODY.put("orgTrdNo", orgTrdNo);
        REQ_BODY.put("crcCd", crcCd);
        REQ_BODY.put("cnclOrd", cnclOrd);
        REQ_BODY.put("cnclAmt", cnclAmt);
        REQ_BODY.put("refundBankCd", refundBankCd);
        REQ_BODY.put("refundAcntNo", refundAcntNo);
        REQ_BODY.put("refundDpstrNm", refundDpstrNm);

        return REQ_BODY;
    }

    public Map<String, String> setRES_HEADER() {
        Map<String, String> RES_HEADER = new LinkedHashMap<>();
        RES_HEADER.put("mchtId", "");       //상점아이디
        RES_HEADER.put("ver", "");          //버전
        RES_HEADER.put("method", "");       //결제수단
        RES_HEADER.put("bizType", "");      //업무구분
        RES_HEADER.put("encCd", "");        //암호화구분
        RES_HEADER.put("mchtTrdNo", "");    //상점주문번호
        RES_HEADER.put("trdNo", "");        //세틀뱅크거래번호
        RES_HEADER.put("trdDt", "");        //요청일자
        RES_HEADER.put("trdTm", "");        //요청시간
        RES_HEADER.put("outStatCd", "");    //결과코드
        RES_HEADER.put("outRsltCd", "");    //거절코드
        RES_HEADER.put("outRsltMsg", "");   //결과메세지
        return RES_HEADER;
    }

    public Map<String, String> setRES_BODY() {
        Map<String, String> RES_BODY = new LinkedHashMap<>();
        RES_BODY.put("pktHash", "");        //해쉬값
        RES_BODY.put("orgTrdNo", "");       //원거래번호
        RES_BODY.put("cnclAmt", "");        //취소금액
        RES_BODY.put("blcAmt", "");         //취소가능잔액
        return RES_BODY;
    }
}
