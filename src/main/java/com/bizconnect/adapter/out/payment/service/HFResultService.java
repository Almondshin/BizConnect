package com.bizconnect.adapter.out.payment.service;

import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.model.HFDataModel;
import com.bizconnect.adapter.out.payment.model.HFResultDataModel;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.PaymentHistory;
import com.bizconnect.application.port.out.SaveAgencyDataPort;
import com.bizconnect.application.port.out.SavePaymentDataPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HFResultService {
    private final Constant constant;
    private final SavePaymentDataPort savePaymentDataPort;
    private final SaveAgencyDataPort saveAgencyDataPort;
    private final ConnectHectoFinancialService connectHectoFinancialService;

    @Value("${external.admin.url}")
    private String profileSpecificUrl;
    Logger logger = LoggerFactory.getLogger("HFResultController");

    public HFResultService(Constant constant, SavePaymentDataPort savePaymentDataPort, SaveAgencyDataPort saveAgencyDataPort, ConnectHectoFinancialService connectHectoFinancialService) {
        this.constant = constant;
        this.savePaymentDataPort = savePaymentDataPort;
        this.saveAgencyDataPort = saveAgencyDataPort;
        this.connectHectoFinancialService = connectHectoFinancialService;
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
        RES_PARAMS.put("outStatCd", hfDataModel.getOutStatCd());                    //거래상태
        RES_PARAMS.put("trdNo", hfDataModel.getTrdNo());                            //거래번호
        RES_PARAMS.put("method", hfDataModel.getMethod());                            //결제수단 (카드CA, 가상계좌VA)
        RES_PARAMS.put("bizType", hfDataModel.getBizType());                        //업무구분
        RES_PARAMS.put("mchtId", hfDataModel.getMchtId());                            //상점아이디 (헥토파이낸셜에서 부여하는 아이디)
        RES_PARAMS.put("mchtTrdNo", hfDataModel.getMchtTrdNo());                    //상점주문번호 (상점에서 생성하는 거래번호)
        RES_PARAMS.put("mchtCustNm", hfDataModel.getMchtCustNm());                    //주문자명 (실제 결제자의 주문자명)
        RES_PARAMS.put("mchtName", hfDataModel.getMchtName());                        //상점 한글명
        RES_PARAMS.put("pmtprdNm", hfDataModel.getPmtprdNm());                        //상품명
        RES_PARAMS.put("trdDtm", hfDataModel.getTrdDtm());                            //거래일시
        RES_PARAMS.put("trdAmt", hfDataModel.getTrdAmt());                            //거래금액
        RES_PARAMS.put("billKey", hfDataModel.getBillKey());                        //자동결제 키 (빌키 이용 상점만 해당)
        RES_PARAMS.put("billKeyExpireDt", hfDataModel.getBillKeyExpireDt());        //자동결제 키 유효기간
        RES_PARAMS.put("bankCd", hfDataModel.getBankCd());                            //은행코드
        RES_PARAMS.put("bankNm", hfDataModel.getBankNm());                            //은행명
        RES_PARAMS.put("cardCd", hfDataModel.getCardCd());                            //카드사 코드
        RES_PARAMS.put("cardNm", hfDataModel.getCardNm());                            //카드명
        RES_PARAMS.put("telecomCd", hfDataModel.getTelecomCd());                    //이통사코드
        RES_PARAMS.put("telecomNm", hfDataModel.getTelecomNm());                    //이통사명
        RES_PARAMS.put("vAcntNo", hfDataModel.getVAcntNo());                        //가상계좌번호
        RES_PARAMS.put("expireDt", hfDataModel.getExpireDt());                        //가상계좌 입금만료 일시
        RES_PARAMS.put("AcntPrintNm", hfDataModel.getAcntPrintNm());                //통장인자명 (고객통장에 찍힐 인자명)
        RES_PARAMS.put("dpstrNm", hfDataModel.getDpstrNm());                        //입금자명 (가상계좌에 실제 입금한 사람 이름)
        RES_PARAMS.put("email", hfDataModel.getEmail());                            //상점고객email
        RES_PARAMS.put("mchtCustId", hfDataModel.getMchtCustId());                    //상점고객아이디
        RES_PARAMS.put("cardNo", hfDataModel.getCardNo());                            //카드번호
        RES_PARAMS.put("cardApprNo", hfDataModel.getCardApprNo());                    //카드승인번호
        RES_PARAMS.put("instmtMon", hfDataModel.getInstmtMon());                    //할부개월수
        RES_PARAMS.put("instmtType", hfDataModel.getInstmtType());                    //할부타입
        RES_PARAMS.put("phoneNoEnc", hfDataModel.getPhoneNoEnc());                    //휴대폰번호암호화
        RES_PARAMS.put("orgTrdNo", hfDataModel.getOrgTrdNo());                        //원거래번호
        RES_PARAMS.put("orgTrdDt", hfDataModel.getOrgTrdDt());                        //원거래일자
        RES_PARAMS.put("mixTrdNo", hfDataModel.getMixTrdNo());                        //복합결제 거래번호
        RES_PARAMS.put("mixTrdAmt", hfDataModel.getMixTrdAmt());                    //복합결제 금액
        RES_PARAMS.put("payAmt", hfDataModel.getPayAmt());                            //실 결제금액 (복합결제 금액을 제외한 결제금액)
        RES_PARAMS.put("csrcIssNo", hfDataModel.getCsrcIssNo());                    //현금영수증 승인번호
        RES_PARAMS.put("cnclType", hfDataModel.getCnclType());                        //취소거래 타입
        RES_PARAMS.put("mchtParam", hfDataModel.getMchtParam());                    //상점예약필드  (추가 정보 필드)
        RES_PARAMS.put("pktHash", hfDataModel.getPktHash());                        //해쉬값

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

        return responseMessage(hashCipher, RES_PARAMS.get("mchtTrdNo"), RES_PARAMS.get("pktHash"), RES_PARAMS.get("outStatCd"), RES_PARAMS);
    }


    public String notiVAData(HFDataModel hfDataModel) {
        Map<String, String> RES_PARAMS = new LinkedHashMap<>();
        RES_PARAMS.put("outStatCd", hfDataModel.getOutStatCd());        //거래상태
        RES_PARAMS.put("trdNo", hfDataModel.getTrdNo());                //거래번호
        RES_PARAMS.put("method", hfDataModel.getMethod());                //결제수단 (카드CA, 가상계좌VA)
        RES_PARAMS.put("bizType", hfDataModel.getBizType());            //업무구분	(???)
        RES_PARAMS.put("mchtId", hfDataModel.getMchtId());                //상점아이디 (헥토파이낸셜에서 부여하는 아이디)
        RES_PARAMS.put("mchtTrdNo", hfDataModel.getMchtTrdNo());        //상점주문번호 (상점에서 생성하는 거래번호)
        RES_PARAMS.put("mchtCustNm", hfDataModel.getMchtCustNm());        //고객명
        RES_PARAMS.put("mchtName", hfDataModel.getMchtName());            //상점한글명
        RES_PARAMS.put("pmtprdNm", hfDataModel.getPmtprdNm());            //상품명
        RES_PARAMS.put("trdDtm", hfDataModel.getTrdDtm());                //거래일시
        RES_PARAMS.put("trdAmt", hfDataModel.getTrdAmt());                //거래금액
        RES_PARAMS.put("bankCd", hfDataModel.getBankCd());                //은행코드 (금융기관 식별자 011, ...)
        RES_PARAMS.put("bankNm", hfDataModel.getBankNm());                //은행명 (NH 농협, 신한)
        RES_PARAMS.put("acntType", hfDataModel.getAcntType());            //계좌구분 ("1 : 기본(회전식), 2 : 고정식 ..." )
        RES_PARAMS.put("vAcntNo", hfDataModel.getVAcntNo());            //가상계좌번호
        RES_PARAMS.put("expireDt", hfDataModel.getExpireDt());            //가상계좌 입금만료 일시
        RES_PARAMS.put("AcntPrintNm", hfDataModel.getAcntPrintNm());    //통장인자명 (고객통장에 찍힐 인자명)
        RES_PARAMS.put("dpstrNm", hfDataModel.getDpstrNm());            //입금자명 (가상계좌에 실제 입금한 사람 이름)
        RES_PARAMS.put("email", hfDataModel.getEmail());                //상점고객email
        RES_PARAMS.put("mchtCustId", hfDataModel.getMchtCustId());        //상점고객아이디
        RES_PARAMS.put("orgTrdNo", hfDataModel.getOrgTrdNo());            //원거래번호
        RES_PARAMS.put("orgTrdDt", hfDataModel.getOrgTrdDt());            //원거래일자
        RES_PARAMS.put("csrcIssNo", hfDataModel.getCsrcIssNo());        //현금영수증 승인번호
        RES_PARAMS.put("cnclType", hfDataModel.getCnclType());            //취소거래타입
        RES_PARAMS.put("mchtParam", hfDataModel.getMchtParam());        //상점예약필드 (추가 정보 필드)
        RES_PARAMS.put("pktHash", hfDataModel.getPktHash());            //해쉬값

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

        return responseMessage(hashCipher, RES_PARAMS.get("mchtTrdNo"), RES_PARAMS.get("pktHash"), RES_PARAMS.get("outStatCd"), RES_PARAMS);
    }


    private String responseMessage(String hashCipher, String mchtTrdNo, String pktHash, String outStatCd, Map<String, String> responseParam) {
        boolean resp = false;
        /**
         hash데이타값이 맞는 지 확인 하는 루틴은 헥토파이낸셜에서 받은 데이타가 맞는지 확인하는 것이므로 꼭 사용하셔야 합니다
         정상적인 결제 건임에도 불구하고 노티 페이지의 오류나 네트웍 문제 등으로 인한 hash 값의 오류가 발생할 수도 있습니다.
         그러므로 hash 오류건에 대해서는 오류 발생시 원인을 파악하여 즉시 수정 및 대처해 주셔야 합니다.
         그리고 정상적으로 데이터를 처리한 경우에도 헥토파이낸셜에서 응답을 받지 못한 경우는 결제결과가 중복해서 나갈 수 있으므로 관련한 처리도 고려되어야 합니다
         */
        if (hashCipher.equals(pktHash)) {
            logger.info("[" + mchtTrdNo + "][SHA256 Hash Check] hashCipher[" + hashCipher + "] pktHash[" + pktHash + "] equals?[TRUE]");

            String[] pairs = responseParam.get("mchtParam").split("&");
            String agencyId = null;
            String siteId = null;
            Date startDate = null;
            Date endDate = null;
            String rateSel = null;
            String offer = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            Calendar cal = Calendar.getInstance();
            Date regDate = cal.getTime();
            Date modDate = cal.getTime();

            try {
                // 분리된 문자열 처리
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        switch (keyValue[0]) {
                            case "agencyId":
                                agencyId = keyValue[1];
                                break;
                            case "siteId":
                                siteId = keyValue[1];
                                break;
                            case "startDate":
                                startDate = sdf.parse(keyValue[1]);
                                break;
                            case "endDate":
                                endDate = sdf.parse(keyValue[1]);
                                break;
                            case "rateSel":
                                rateSel = keyValue[1];
                                break;
                            case "offer":
                                offer = keyValue[1];
                                break;
                        }
                    }
                }

                if ("0021".equals(outStatCd)) {
                    System.out.println("outStatCd equals 0021");
                    switch (responseParam.get("method")) {
                        case "CA": {
                            resp = true;
                            PaymentHistory paymentHistory = new PaymentHistory(
                                    responseParam.get("mchtTrdNo"),     //상점에서 생성한 TradeNum
                                    responseParam.get("trdNo"),         //헥토파이낸셜 TradeNum
                                    agencyId,
                                    siteId,
                                    responseParam.get("method"),        //결제수단
                                    rateSel,
                                    responseParam.get("trdAmt"),        //결제금액
                                    offer,
                                    "Y",
                                    originalFormat.parse(responseParam.get("trdDtm")),         //거래일
                                    startDate,
                                    endDate,
                                    regDate
                            );
                            System.out.println("0021 card paymentHistory : " + paymentHistory);
                            savePaymentDataPort.insertPayment(paymentHistory);
                            saveAgencyDataPort.updateAgency(new Agency(agencyId,siteId),new Client(rateSel,startDate,endDate));
                            this.paymentCompletionNoti(agencyId,siteId, responseParam.get("mchtTrdNo"));
                            break;
                        }
                        case "VA": {
                            resp = true;
                            PaymentHistory paymentHistory = new PaymentHistory(
                                    responseParam.get("mchtTrdNo"),     //상점에서 생성한 TradeNum
                                    responseParam.get("trdNo"),         //헥토파이낸셜 TradeNum
                                    agencyId,
                                    siteId,
                                    responseParam.get("method"),        //결제수단
                                    rateSel,
                                    responseParam.get("trdAmt"),        //결제금액
                                    offer,
                                    "Y",
                                    originalFormat.parse(responseParam.get("trdDtm")),       //거래일
                                    responseParam.get("AcntPrintNm"),
                                    responseParam.get("bankNm"),
                                    responseParam.get("bankCd"),
                                    responseParam.get("vAcntNo"),
                                    originalFormat.parse(responseParam.get("expireDt")),
                                    startDate,
                                    endDate,
                                    regDate,
                                    modDate
                            );
                            System.out.println("0021 vBank paymentHistory : " + paymentHistory);
                            savePaymentDataPort.updatePayment(paymentHistory);
                            saveAgencyDataPort.updateAgency(new Agency(agencyId,siteId),new Client(rateSel,startDate,endDate));
                            this.paymentCompletionNoti(agencyId,siteId, responseParam.get("mchtTrdNo"));
                            break;
                        }
                    }
                } else if ("0051".equals(outStatCd)) {
                    PaymentHistory paymentHistory = new PaymentHistory(
                            responseParam.get("mchtTrdNo"),     //상점에서 생성한 TradeNum
                            responseParam.get("trdNo"),         //헥토파이낸셜 TradeNum
                            agencyId,
                            siteId,
                            responseParam.get("method"),        //결제수단
                            rateSel,
                            responseParam.get("trdAmt"),        //결제금액
                            offer,
                            "M",    //결제상태
                            null,        //거래일
                            responseParam.get("AcntPrintNm"),
                            responseParam.get("bankNm"),
                            responseParam.get("bankCd"),
                            responseParam.get("vAcntNo"),
                            originalFormat.parse(responseParam.get("expireDt")),
                            startDate,
                            endDate,
                            regDate,
                            null
                    );
                    System.out.println("0051 vBank paymentHistory : " + paymentHistory);
                    savePaymentDataPort.insertPayment(paymentHistory);
                    resp = true;
                } else {
                    resp = false;
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } else {
            logger.info("[" + mchtTrdNo + "][SHA256 Hash Check] hashCipher[" + hashCipher + "] pktHash[" + pktHash + "] equals?[FALSE]");
            // resp = notiHashError(noti); // 실패 처리
        }
        System.out.println("HF Service ResponseMessage : " + resp);

        // OK, FAIL문자열은 헥토파이낸셜로 전송되어야 하는 값이므로 변경하거나 삭제하지마십시오.
        if (resp) {
            logger.info("[" + mchtTrdNo + "][Result] OK");
            return "OK";
        } else {
            logger.info("[" + mchtTrdNo + "][Result] FAIL");
            return "FAIL";
        }
    }


    public Map<String, String> decryptData(HFResultDataModel resultDataModel) {
        /** 응답 파라미터 세팅 */
        Map<String, String> responseParams = new LinkedHashMap<>();
        /** 설정 정보 저장 */
        String aesKey = constant.AES256_KEY;

        responseParams.put("mchtId", resultDataModel.getMchtId());            //상점아이디
        responseParams.put("outStatCd", resultDataModel.getOutStatCd());         //결과코드
        responseParams.put("outRsltCd", resultDataModel.getOutRsltCd());         //거절코드
        responseParams.put("outRsltMsg", resultDataModel.getOutRsltMsg());        //결과메세지
        responseParams.put("method", resultDataModel.getMethod());            //결제수단
        responseParams.put("mchtTrdNo", resultDataModel.getMchtTrdNo());         //상점주문번호
        responseParams.put("mchtCustId", resultDataModel.getMchtCustId());        //상점고객아이디
        responseParams.put("trdNo", resultDataModel.getTrdNo());             //세틀뱅크 거래번호
        responseParams.put("trdAmt", resultDataModel.getTrdAmt());            //거래금액
        responseParams.put("mchtParam", resultDataModel.getMchtParam());         //상점 예약필드
        responseParams.put("authDt", resultDataModel.getAuthDt());            //승인일시
        responseParams.put("authNo", resultDataModel.getAuthNo());            //승인번호
        responseParams.put("reqIssueDt", resultDataModel.getReqIssueDt());       //채번요청일시
        responseParams.put("intMon", resultDataModel.getIntMon());            //할부개월수
        responseParams.put("fnNm", resultDataModel.getFnNm());              //카드사명
        responseParams.put("fnCd", resultDataModel.getFnCd());              //카드사코드
        responseParams.put("pointTrdNo", resultDataModel.getPointTrdNo());        //포인트거래번호
        responseParams.put("pointTrdAmt", resultDataModel.getPointTrdAmt());       //포인트거래금액
        responseParams.put("cardTrdAmt", resultDataModel.getCardTrdAmt());        //신용카드결제금액
        responseParams.put("vtlAcntNo", resultDataModel.getVtlAcntNo());         //가상계좌번호
        responseParams.put("expireDt", resultDataModel.getExpireDt());          //입금기한
        responseParams.put("cphoneNo", resultDataModel.getCphoneNo());          //휴대폰번호
        responseParams.put("billKey", resultDataModel.getBillKey());           //자동결제키
        responseParams.put("csrcAmt", resultDataModel.getCsrcAmt());           //현금영수증 발급 금액(네이버페이)

        //AES256 복호화 필요 파라미터
//        String[] DECRYPT_PARAMS = {"mchtCustId", "trdAmt", "pointTrdAmt", "cardTrdAmt", "vtlAcntNo", "cphoneNo", "csrcAmt"};
        String[] DECRYPT_PARAMS = {"mchtCustId", "trdAmt", "vtlAcntNo"};


        connectHectoFinancialService.decryptParams(DECRYPT_PARAMS, responseParams, responseParams);

        System.out.println("responseParams " + responseParams);
        return responseParams;
    }


    public void paymentCompletionNoti(String agencyId,String siteId, String tradeNum){
        WebClient webClient = WebClient.create(profileSpecificUrl);  // 외부 서버의 URL
        // 데이터
        Map<String, String> data = new HashMap<>();
        data.put("agencyId", agencyId);
        data.put("siteId", siteId);
        data.put("tradeNum", tradeNum);

        Mono<Void> response = webClient.post()
                .uri("/clientManagement/agency/payment/noti")  // 요청을 보낼 엔드포인트
                .body(BodyInserters.fromValue(data))
                .retrieve()
                .bodyToMono(Void.class);

        response.subscribe();  // 비동기로 요청 실행
    }
}


