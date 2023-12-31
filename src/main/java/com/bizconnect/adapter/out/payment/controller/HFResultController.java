package com.bizconnect.adapter.out.payment.controller;

import com.bizconnect.adapter.out.payment.model.HFDataModel;
import com.bizconnect.adapter.out.payment.model.HFResultDataModel;
import com.bizconnect.adapter.out.payment.service.HFResultService;
import com.bizconnect.application.domain.model.PaymentHistory;
import com.bizconnect.application.port.in.PaymentUseCase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping(value = {"/agency/payment/api/result", "/payment/api/result"})
public class HFResultController {
    private final HFResultService hfResultService;

    @Value("${external.url}")
    private String profileSpecificUrl;

    private final PaymentUseCase paymentUseCase;

    public HFResultController(HFResultService hfResultService, PaymentUseCase paymentUseCase) {
        this.hfResultService = hfResultService;
        this.paymentUseCase = paymentUseCase;
    }

    // 결과 페이지 이후 콜백 S2S 안돼서 임시 처리
    @PostMapping(value = "/next")
    public void next(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getParameter("method");
        HFDataModel card = new HFDataModel(request.getParameter("mchtTrdNo"), request.getParameter("pointTrdNo"), request.getParameter("trdNo"), request.getParameter("vtlAcntNo"), request.getParameter("mchtCustId"), request.getParameter("fnNm"), request.getParameter("method"), request.getParameter("authNo"), request.getParameter("trdAmt"), request.getParameter("pointTrdAmt"), request.getParameter("cardTrdAmt"), request.getParameter("outRsltMsg"), request.getParameter("mchtParam"), request.getParameter("outStatCd"), request.getParameter("outRsltCd"), request.getParameter("intMon"), request.getParameter("authDt"), request.getParameter("mchtId"), request.getParameter("fnCd"));
        HFDataModel vbank = new HFDataModel(request.getParameter("mchtTrdNo"), request.getParameter("trdNo"), request.getParameter("vtlAcntNo"), request.getParameter("mchtCustId"), request.getParameter("fnNm"), request.getParameter("method"), request.getParameter("trdAmt"), request.getParameter("outRsltMsg"), request.getParameter("mchtParam"), request.getParameter("expireDt"), request.getParameter("outStatCd"), request.getParameter("outRsltCd"), request.getParameter("authDt"), request.getParameter("mchtId"), request.getParameter("fnCd"));
        String data = "";

        HFResultDataModel resultDataModel = new HFResultDataModel();
        resultDataModel.setMchtId(request.getParameter("mchtId"));
        resultDataModel.setOutStatCd(request.getParameter("outStatCd"));
        resultDataModel.setOutRsltCd(request.getParameter("outRsltCd"));
        resultDataModel.setOutRsltMsg(request.getParameter("outRsltMsg"));
        resultDataModel.setMethod(request.getParameter("method"));
        resultDataModel.setMchtTrdNo(request.getParameter("mchtTrdNo"));
        resultDataModel.setMchtCustId(request.getParameter("mchtCustId"));
        resultDataModel.setTrdNo(request.getParameter("trdNo"));
        resultDataModel.setTrdAmt(request.getParameter("trdAmt"));
        resultDataModel.setMchtParam(request.getParameter("mchtParam"));
        resultDataModel.setAuthDt(request.getParameter("authDt"));
        resultDataModel.setAuthNo(request.getParameter("authNo"));
        resultDataModel.setReqIssueDt(request.getParameter("reqIssueDt"));
        resultDataModel.setIntMon(request.getParameter("intMon"));
        resultDataModel.setFnNm(request.getParameter("fnNm"));
        resultDataModel.setFnCd(request.getParameter("fnCd"));
        resultDataModel.setPointTrdNo(request.getParameter("pointTrdNo"));
        resultDataModel.setPointTrdAmt(request.getParameter("pointTrdAmt"));
        resultDataModel.setCardTrdAmt(request.getParameter("cardTrdAmt"));
        resultDataModel.setVtlAcntNo(request.getParameter("vtlAcntNo"));
        resultDataModel.setExpireDt(request.getParameter("expireDt"));
        resultDataModel.setCphoneNo(request.getParameter("cphoneNo"));
        resultDataModel.setBillKey(request.getParameter("billKey"));
        resultDataModel.setCsrcAmt(request.getParameter("csrcAmt"));
        if (method.equals("card")) {
            data = hfResultService.nextCardData(card);
        }
        if (method.equals("vbank")) {
            data = hfResultService.nextVBankData(vbank);
        }
        response.sendRedirect(profileSpecificUrl + "/agency/end.html?data=" + data);
    }

    // 헥토파이낸셜 서버 요청, 현 서버 수신 - 로컬 사용 불가
    @PostMapping(value = "/noti")
    public String noti(HttpServletRequest request) {
        HFDataModel notiCA = new HFDataModel(request.getParameter("outStatCd"), request.getParameter("trdNo"), request.getParameter("method"), request.getParameter("bizType"), request.getParameter("mchtId"), request.getParameter("mchtTrdNo"), request.getParameter("mchtCustNm"), request.getParameter("mchtName"), request.getParameter("pmtprdNm"), request.getParameter("trdDtm"), request.getParameter("trdAmt"), request.getParameter("billKey"), request.getParameter("billKeyExpireDt"), request.getParameter("bankCd"), request.getParameter("bankNm"), request.getParameter("cardCd"), request.getParameter("cardNm"), request.getParameter("telecomCd"), request.getParameter("telecomNm"), request.getParameter("vAcntNo"), request.getParameter("expireDt"), request.getParameter("AcntPrintNm"), request.getParameter("dpstrNm"), request.getParameter("email"), request.getParameter("mchtCustId"), request.getParameter("cardNo"), request.getParameter("cardApprNo"), request.getParameter("instmtMon"), request.getParameter("instmtType"), request.getParameter("phoneNoEnc"), request.getParameter("orgTrdNo"), request.getParameter("orgTrdDt"), request.getParameter("mixTrdNo"), request.getParameter("mixTrdAmt"), request.getParameter("payAmt"), request.getParameter("csrcIssNo"), request.getParameter("cnclType"), request.getParameter("mchtParam"), request.getParameter("pktHash"));
        HFDataModel notiVA = new HFDataModel(request.getParameter("outStatCd"), request.getParameter("trdNo"), request.getParameter("method"), request.getParameter("bizType"), request.getParameter("mchtId"), request.getParameter("mchtTrdNo"), request.getParameter("mchtCustNm"), request.getParameter("mchtName"), request.getParameter("pmtprdNm"), request.getParameter("trdDtm"), request.getParameter("trdAmt"), request.getParameter("bankCd"), request.getParameter("bankNm"), request.getParameter("acntType"), request.getParameter("vAcntNo"), request.getParameter("expireDt"), request.getParameter("AcntPrintNm"), request.getParameter("dpstrNm"), request.getParameter("email"), request.getParameter("mchtCustId"), request.getParameter("orgTrdNo"), request.getParameter("orgTrdDt"), request.getParameter("csrcIssNo"), request.getParameter("cnclType"), request.getParameter("mchtParam"), request.getParameter("pktHash"));
        /** 응답 파라미터 세팅 */
        String method = request.getParameter("method");
        if (method.equals("CA")) {
            System.out.println("hfResultService.notiCAData(notiCA) : " + hfResultService.notiCAData(notiCA));
            return hfResultService.notiCAData(notiCA);
        }
        if (method.equals("VA")) {
            System.out.println("hfResultService.notiVAData(notiVA) : " + hfResultService.notiVAData(notiVA));
            return hfResultService.notiVAData(notiVA);
        }
        return "FAIL";
    }

    @PostMapping(value = "/decrypt")
    public String result(HttpServletRequest request, @RequestBody Map<String, String> requestMap) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String encodeData = requestMap.get("data");
        String decodeData = new String(Base64.getDecoder().decode(encodeData.getBytes()), StandardCharsets.UTF_8);

        Map<String, String> resMap = objectMapper.readValue(decodeData, new TypeReference<>() {
        });

        if (resMap == null) {
            HFResultDataModel resultDataModel = new HFResultDataModel();
            resultDataModel.setMchtId(request.getParameter("mchtId"));
            resultDataModel.setOutStatCd(request.getParameter("outStatCd"));
            resultDataModel.setOutRsltCd(request.getParameter("outRsltCd"));
            resultDataModel.setOutRsltMsg(request.getParameter("outRsltMsg"));
            resultDataModel.setMethod(request.getParameter("method"));
            resultDataModel.setMchtTrdNo(request.getParameter("mchtTrdNo"));
            resultDataModel.setMchtCustId(request.getParameter("mchtCustId"));
            resultDataModel.setTrdNo(request.getParameter("trdNo"));
            resultDataModel.setTrdAmt(request.getParameter("trdAmt"));
            resultDataModel.setMchtParam(request.getParameter("mchtParam"));
            resultDataModel.setAuthDt(request.getParameter("authDt"));
            resultDataModel.setAuthNo(request.getParameter("authNo"));
            resultDataModel.setReqIssueDt(request.getParameter("reqIssueDt"));
            resultDataModel.setIntMon(request.getParameter("intMon"));
            resultDataModel.setFnNm(request.getParameter("fnNm"));
            resultDataModel.setFnCd(request.getParameter("fnCd"));
            resultDataModel.setPointTrdNo(request.getParameter("pointTrdNo"));
            resultDataModel.setPointTrdAmt(request.getParameter("pointTrdAmt"));
            resultDataModel.setCardTrdAmt(request.getParameter("cardTrdAmt"));
            resultDataModel.setVtlAcntNo(request.getParameter("vtlAcntNo"));
            resultDataModel.setExpireDt(request.getParameter("expireDt"));
            resultDataModel.setCphoneNo(request.getParameter("cphoneNo"));
            resultDataModel.setBillKey(request.getParameter("billKey"));
            resultDataModel.setCsrcAmt(request.getParameter("csrcAmt"));
            resMap = hfResultService.decryptData(resultDataModel);
        }
        return objectMapper.writeValueAsString(resMap);
    }

}


