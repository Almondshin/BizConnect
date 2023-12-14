package com.bizconnect.adapter.out.payment.service;

import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.model.HFDataModel;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class HFResultService {
    private final Constant constant;
    Logger logger = LoggerFactory.getLogger("HFResultController");

    public HFResultService(Constant constant) {
        this.constant = constant;
    }

    public String nextCardData(HFDataModel hfDataModel) throws IOException {
        Map<String, String> RES_PARAMS = new LinkedHashMap<>();
        RES_PARAMS.put("mchtTrdNo", hfDataModel.getMchtTrdNo());
        RES_PARAMS.put("pointTrdNo", hfDataModel.getPointTrdNo());
        RES_PARAMS.put("trdNo", hfDataModel.getTrdNo());
        RES_PARAMS.put("vtlAcntNo", hfDataModel.getVtlAcntNo());
        RES_PARAMS.put("mchtCustId", hfDataModel.getMchtCustId());
        RES_PARAMS.put("fnNm", hfDataModel.getFnNm());
        RES_PARAMS.put("method", hfDataModel.getMethod());
        RES_PARAMS.put("authNo", hfDataModel.getAuthNo());
        RES_PARAMS.put("trdAmt", hfDataModel.getTrdAmt());
        RES_PARAMS.put("pointTrdAmt", hfDataModel.getPointTrdAmt());
        RES_PARAMS.put("cardTrdAmt", hfDataModel.getCardTrdAmt());
        RES_PARAMS.put("outRsltMsg", hfDataModel.getOutRsltMsg());
        RES_PARAMS.put("mchtParam", hfDataModel.getMchtParam());
        RES_PARAMS.put("outStatCd", hfDataModel.getOutStatCd());
        RES_PARAMS.put("outRsltCd", hfDataModel.getOutRsltCd());
        RES_PARAMS.put("intMon", hfDataModel.getIntMon());
        RES_PARAMS.put("authDt", hfDataModel.getAuthDt());
        RES_PARAMS.put("mchtId", hfDataModel.getMchtId());
        RES_PARAMS.put("fnCd", hfDataModel.getFnCd());
        ObjectMapper objectMapper = new ObjectMapper();
        return Base64.getEncoder().encodeToString(objectMapper.writeValueAsString(RES_PARAMS).getBytes());
    }

    public String nextVBankData(HFDataModel hfDataModel) throws IOException {
        Map<String, String> RES_PARAMS = new LinkedHashMap<>();
        RES_PARAMS.put("mchtTrdNo", hfDataModel.getMchtTrdNo());
        RES_PARAMS.put("trdNo", hfDataModel.getTrdNo());
        RES_PARAMS.put("vtlAcntNo", hfDataModel.getVtlAcntNo());
        RES_PARAMS.put("mchtCustId", hfDataModel.getMchtCustId());
        RES_PARAMS.put("fnNm", hfDataModel.getFnNm());
        RES_PARAMS.put("method", hfDataModel.getMethod());
        RES_PARAMS.put("trdAmt", hfDataModel.getTrdAmt());
        RES_PARAMS.put("outRsltMsg", hfDataModel.getOutRsltMsg());
        RES_PARAMS.put("mchtParam", hfDataModel.getMchtParam());
        RES_PARAMS.put("expireDt", hfDataModel.getExpireDt());
        RES_PARAMS.put("outStatCd", hfDataModel.getOutStatCd());
        RES_PARAMS.put("outRsltCd", hfDataModel.getOutRsltCd());
        RES_PARAMS.put("authDt", hfDataModel.getAuthDt());
        RES_PARAMS.put("mchtId", hfDataModel.getMchtId());
        RES_PARAMS.put("fnCd", hfDataModel.getFnCd());
        ObjectMapper objectMapper = new ObjectMapper();
        return Base64.getEncoder().encodeToString(objectMapper.writeValueAsString(RES_PARAMS).getBytes());
    }

    public String notiCAData(HFDataModel hfDataModel) {
        Map<String, String> RES_PARAMS = new LinkedHashMap<>();
        RES_PARAMS.put("outStatCd", hfDataModel.getOutStatCd());
        RES_PARAMS.put("trdNo", hfDataModel.getTrdNo());
        RES_PARAMS.put("method", hfDataModel.getMethod());
        RES_PARAMS.put("bizType", hfDataModel.getBizType());
        RES_PARAMS.put("mchtId", hfDataModel.getMchtId());
        RES_PARAMS.put("mchtTrdNo", hfDataModel.getMchtTrdNo());
        RES_PARAMS.put("mchtCustNm", hfDataModel.getMchtCustNm());
        RES_PARAMS.put("mchtName", hfDataModel.getMchtName());
        RES_PARAMS.put("pmtprdNm", hfDataModel.getPmtprdNm());
        RES_PARAMS.put("trdDtm", hfDataModel.getTrdDtm());
        RES_PARAMS.put("trdAmt", hfDataModel.getTrdAmt());
        RES_PARAMS.put("billKey", hfDataModel.getBillKey());
        RES_PARAMS.put("billKeyExpireDt", hfDataModel.getBillKeyExpireDt());
        RES_PARAMS.put("bankCd", hfDataModel.getBankCd());
        RES_PARAMS.put("bankNm", hfDataModel.getBankNm());
        RES_PARAMS.put("cardCd", hfDataModel.getCardCd());
        RES_PARAMS.put("cardNm", hfDataModel.getCardNm());
        RES_PARAMS.put("telecomCd", hfDataModel.getTelecomCd());
        RES_PARAMS.put("telecomNm", hfDataModel.getTelecomNm());
        RES_PARAMS.put("vAcntNo", hfDataModel.getVAcntNo());
        RES_PARAMS.put("expireDt", hfDataModel.getExpireDt());
        RES_PARAMS.put("AcntPrintNm", hfDataModel.getAcntPrintNm());
        RES_PARAMS.put("dpstrNm", hfDataModel.getDpstrNm());
        RES_PARAMS.put("email", hfDataModel.getEmail());
        RES_PARAMS.put("mchtCustId", hfDataModel.getMchtCustId());
        RES_PARAMS.put("cardNo", hfDataModel.getCardNo());
        RES_PARAMS.put("cardApprNo", hfDataModel.getCardApprNo());
        RES_PARAMS.put("instmtMon", hfDataModel.getInstmtMon());
        RES_PARAMS.put("instmtType", hfDataModel.getInstmtType());
        RES_PARAMS.put("phoneNoEnc", hfDataModel.getPhoneNoEnc());
        RES_PARAMS.put("orgTrdNo", hfDataModel.getOrgTrdNo());
        RES_PARAMS.put("orgTrdDt", hfDataModel.getOrgTrdDt());
        RES_PARAMS.put("mixTrdNo", hfDataModel.getMixTrdNo());
        RES_PARAMS.put("mixTrdAmt", hfDataModel.getMixTrdAmt());
        RES_PARAMS.put("payAmt", hfDataModel.getPayAmt());
        RES_PARAMS.put("csrcIssNo", hfDataModel.getCsrcIssNo());
        RES_PARAMS.put("cnclType", hfDataModel.getCnclType());
        RES_PARAMS.put("mchtParam", hfDataModel.getMchtParam());
        RES_PARAMS.put("pktHash", hfDataModel.getPktHash());

        boolean resp = false;
        /** 해쉬 조합 필드
         *  결과코드 + 거래일시 + 상점아이디 + 가맹점거래번호 + 거래금액 + 라이센스키 */
        String hashPlain = new HFDataModel(
                hfDataModel.getMchtTrdNo(),
                hfDataModel.getTrdAmt(),
                hfDataModel.getOutStatCd(),
                hfDataModel.getMchtId(),
                hfDataModel.getTrdDtm()
        ).getHashPlain() + constant.LICENSE_KEY;
        String hashCipher = "";

        /** SHA256 해쉬 처리 */
        try {
            hashCipher = EncryptUtil.digestSHA256(hashPlain);//해쉬 값
        } catch (Exception e) {
            logger.error("[" + RES_PARAMS.get("mchtTrdNo") + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
        } finally {
            logger.info("[" + RES_PARAMS.get("mchtTrdNo") + "][SHA256 HASHING] Plain Text[" + hashPlain + "] ---> Cipher Text[" + hashCipher + "]");
        }

        return responseMessage(hashCipher,RES_PARAMS.get("mchtTrdNo"), RES_PARAMS.get("pktHash"),RES_PARAMS.get("outStatCd"));
    }


    public String notiVAData(HFDataModel hfDataModel) {
        Map<String, String> RES_PARAMS = new LinkedHashMap<>();
        RES_PARAMS.put("outStatCd", hfDataModel.getOutStatCd());
        RES_PARAMS.put("trdNo", hfDataModel.getTrdNo());
        RES_PARAMS.put("method", hfDataModel.getMethod());
        RES_PARAMS.put("bizType", hfDataModel.getBizType());
        RES_PARAMS.put("mchtId", hfDataModel.getMchtId());
        RES_PARAMS.put("mchtTrdNo", hfDataModel.getMchtTrdNo());
        RES_PARAMS.put("mchtCustNm", hfDataModel.getMchtCustNm());
        RES_PARAMS.put("mchtName", hfDataModel.getMchtName());
        RES_PARAMS.put("pmtprdNm", hfDataModel.getPmtprdNm());
        RES_PARAMS.put("trdDtm", hfDataModel.getTrdDtm());
        RES_PARAMS.put("trdAmt", hfDataModel.getTrdAmt());
        RES_PARAMS.put("bankCd", hfDataModel.getBankCd());
        RES_PARAMS.put("bankNm", hfDataModel.getBankNm());
        RES_PARAMS.put("acntType", hfDataModel.getAcntType());
        RES_PARAMS.put("vAcntNo", hfDataModel.getVAcntNo());
        RES_PARAMS.put("expireDt", hfDataModel.getExpireDt());
        RES_PARAMS.put("AcntPrintNm", hfDataModel.getAcntPrintNm());
        RES_PARAMS.put("dpstrNm", hfDataModel.getDpstrNm());
        RES_PARAMS.put("email", hfDataModel.getEmail());
        RES_PARAMS.put("mchtCustId", hfDataModel.getMchtCustId());
        RES_PARAMS.put("orgTrdNo", hfDataModel.getOrgTrdNo());
        RES_PARAMS.put("orgTrdDt", hfDataModel.getOrgTrdDt());
        RES_PARAMS.put("csrcIssNo", hfDataModel.getCsrcIssNo());
        RES_PARAMS.put("cnclType", hfDataModel.getCnclType());
        RES_PARAMS.put("mchtParam", hfDataModel.getMchtParam());
        RES_PARAMS.put("pktHash", hfDataModel.getPktHash());

        /** 해쉬 조합 필드
         *  결과코드 + 거래일시 + 상점아이디 + 가맹점거래번호 + 거래금액 + 라이센스키 */
        String hashPlain = new HFDataModel(
                hfDataModel.getMchtTrdNo(),
                hfDataModel.getTrdAmt(),
                hfDataModel.getOutStatCd(),
                hfDataModel.getMchtId(),
                hfDataModel.getTrdDtm()
        ).getHashPlain() + constant.LICENSE_KEY;
        String hashCipher = "";

        /** SHA256 해쉬 처리 */
        try {
            hashCipher = EncryptUtil.digestSHA256(hashPlain);//해쉬 값
        } catch (Exception e) {
            logger.error("[" + RES_PARAMS.get("mchtTrdNo") + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
        } finally {
            logger.info("[" + RES_PARAMS.get("mchtTrdNo") + "][SHA256 HASHING] Plain Text[" + hashPlain + "] ---> Cipher Text[" + hashCipher + "]");
        }

        return responseMessage(hashCipher,RES_PARAMS.get("mchtTrdNo"), RES_PARAMS.get("pktHash"),RES_PARAMS.get("outStatCd"));
    }





    private String responseMessage(String hashCipher,String mchtTrdNo, String pktHash, String outStatCd){
        boolean resp = false;
        /**
         hash데이타값이 맞는 지 확인 하는 루틴은 헥토파이낸셜에서 받은 데이타가 맞는지 확인하는 것이므로 꼭 사용하셔야 합니다
         정상적인 결제 건임에도 불구하고 노티 페이지의 오류나 네트웍 문제 등으로 인한 hash 값의 오류가 발생할 수도 있습니다.
         그러므로 hash 오류건에 대해서는 오류 발생시 원인을 파악하여 즉시 수정 및 대처해 주셔야 합니다.
         그리고 정상적으로 데이터를 처리한 경우에도 헥토파이낸셜에서 응답을 받지 못한 경우는 결제결과가 중복해서 나갈 수 있으므로 관련한 처리도 고려되어야 합니다
         */
        if (hashCipher.equals(pktHash)) {
            logger.info("[" + mchtTrdNo + "][SHA256 Hash Check] hashCipher[" + hashCipher + "] pktHash[" + pktHash + "] equals?[TRUE]");
            if ("0021".equals(outStatCd)) {
                // resp = notiSuccess(noti); // 성공 처리
            } else {
                resp = false;
            }
        } else {
            logger.info("[" + mchtTrdNo + "][SHA256 Hash Check] hashCipher[" + hashCipher + "] pktHash[" + pktHash + "] equals?[FALSE]");
            // resp = notiHashError(noti); // 실패 처리
        }

        // OK, FAIL문자열은 헥토파이낸셜로 전송되어야 하는 값이므로 변경하거나 삭제하지마십시오.
        if (resp) {
            logger.info("[" + mchtTrdNo + "][Result] OK");
            return "OK";
        } else {
            logger.info("[" + mchtTrdNo + "][Result] FAIL");
            return "FAIL";
        }
    }


}


