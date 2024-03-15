package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.bizconnect.adapter.out.payment.utils.HttpClientUtil;
import com.bizconnect.application.domain.enums.*;
import com.bizconnect.application.domain.model.AgencyInfoKey;
import com.bizconnect.application.domain.model.AgencyProducts;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.exceptions.NoExtensionException;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.bizconnect.application.port.in.NotiUseCase;
import com.bizconnect.application.port.in.PaymentUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Payment controller.
 */
@Slf4j
@RestController
@RequestMapping(value = {"/agency/payment", "/payment"})
public class PaymentController {

    private static final int DAYS_BEFORE_EXPIRATION = 15;
    private final AgencyUseCase agencyUseCase;
    private final Constant constant;
    private final PaymentUseCase paymentUseCase;
    private final NotiUseCase notiUseCase;

    @Value("${external.url}")
    private String profileSpecificUrl;

    @Value("${external.payment.url}")
    private String profileSpecificPaymentUrl;

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Instantiates a new Payment controller.
     *
     * @param agencyUseCase  the agency use case
     * @param constant       the constant
     * @param paymentUseCase the payment use case
     */
    public PaymentController(AgencyUseCase agencyUseCase, Constant constant, PaymentUseCase paymentUseCase, NotiUseCase notiUseCase) {
        this.agencyUseCase = agencyUseCase;
        this.constant = constant;
        this.paymentUseCase = paymentUseCase;
        this.notiUseCase = notiUseCase;
    }


    /**
     * 결제 정보 요청
     *
     * @param clientDataModel 필수 값 : AgencyId, SiteId , 옵션 값 : RateSel, StartDate
     * @return resultCode, resultMsg, siteId, RateSel list,
     */
    @PostMapping("/getPaymentInfo")
    public ResponseEntity<?> getPaymentInfo(@RequestBody ClientDataModel clientDataModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Optional<ClientDataModel> optClientInfo = agencyUseCase.getAgencyInfo(new ClientDataModel(clientDataModel.getAgencyId(), clientDataModel.getSiteId(), clientDataModel.getRateSel(), clientDataModel.getStartDate()));
        List<Map<String, String>> productTypes = agencyUseCase.getProductTypes(clientDataModel.getAgencyId());
        Map<String, Object> responseMessage = new HashMap<>();

        logger.info("S ------------------------------[AGENCY] - [getPaymentInfo] ------------------------------ S");
        logger.info("[agencyId] : [" + clientDataModel.getAgencyId() + "]");
        logger.info("[siteId] : [" + clientDataModel.getSiteId() + "]");
        logger.info("[rateSel] : [" + clientDataModel.getRateSel() + "]");
        logger.info("[startDate] : [" + clientDataModel.getStartDate() + "]");


        if (optClientInfo.isPresent()) {
            ClientDataModel clientInfo = optClientInfo.get();

            String rateSel = decideRateSel(clientInfo, clientDataModel);
            String startDate = decideStartDate(sdf, clientInfo, clientDataModel);

            ResponseEntity<?> siteStatusResponse = decideSiteStatus(responseMessage, clientInfo);
            if (siteStatusResponse != null) {
                return siteStatusResponse;
            }

            if (clientInfo.getScheduledRateSel() != null && clientInfo.getScheduledRateSel().contains("autopay")) {
                responseMessage.put("resultCode", EnumResultCode.Subscription.getCode());
                responseMessage.put("resultMsg", EnumResultCode.Subscription.getValue());
                return ResponseEntity.ok(responseMessage);
            }

            //초과 건수 계산
            if (clientInfo.getExtensionStatus().equals(EnumExtensionStatus.EXTENDABLE.getCode())) {
                Map<String, Integer> excessMap = paymentUseCase.getExcessAmount(
                        paymentUseCase.getPaymentHistoryByAgency(clientInfo.getAgencyId(), clientInfo.getSiteId())
                );

                responseMessage.put("extensionStatus", EnumExtensionStatus.EXTENDABLE.getCode());
                responseMessage.put("excessCount", excessMap.get("excessCount"));
                responseMessage.put("excessAmount", excessMap.get("excessAmount"));
                logger.info("[Retrieved excessCount] : [" + excessMap.get("excessCount") + "]");
                logger.info("[Retrieved excessAmount] : [" + excessMap.get("excessAmount") + "]");
            }

            logger.info("[Retrieved agencyId] : [" + clientInfo.getAgencyId() + "]");
            logger.info("[Retrieved siteId] : [" + clientInfo.getSiteId() + "]");
            logger.info("[Retrieved startDate] : [" + clientInfo.getStartDate() + "]");
            logger.info("[Retrieved endDate] : [" + clientInfo.getEndDate() + "]");

            responseMessage.put("clientInfo", clientInfo.getCompanyName() + "," + clientInfo.getBizNumber() + "," + clientInfo.getCeoName());
            responseMessage.put("rateSel", rateSel);
            responseMessage.put("startDate", startDate);
        }


        responseMessage.put("resultCode", EnumResultCode.SUCCESS.getCode());
        responseMessage.put("resultMsg", EnumResultCode.SUCCESS.getValue());
        responseMessage.put("profileUrl", profileSpecificUrl);
        responseMessage.put("profilePaymentUrl", profileSpecificPaymentUrl);
        responseMessage.put("siteId", clientDataModel.getAgencyId() + "-" + clientDataModel.getSiteId());
        responseMessage.put("listSel", productTypes);

        logger.info("[resultCode] : [" + responseMessage.get("resultCode") + "]");
        logger.info("[resultMsg] : [" + responseMessage.get("resultMsg") + "]");
        logger.info("E ------------------------------[AGENCY] - [getPaymentInfo] ------------------------------ E");
        return ResponseEntity.ok(responseMessage);
    }


    /**
     * Sets payment site info.
     *
     * @param clientDataModel the client data model
     * @return the payment site info
     */
    @PostMapping("/setPaymentSiteInfo")
    public ResponseEntity<?> setPaymentSiteInfo(@RequestBody ClientDataModel clientDataModel) {
        Map<String, Object> responseMessage = new HashMap<>();
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        String tradeNum = paymentUseCase.makeTradeNum();

        //TODO
        // 연장결제 시 기간 선택 불가능
        // 초과금액 계산 마지막거래건이 아닌 전전거래건을 찾아갈 수 있도록 변경 필요
        try {
            paymentUseCase.checkMchtParams(clientDataModel);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String trdDt = ldt.format(dateFormatter);
        String trdTm = ldt.format(timeFormatter);
        String mchtId;
        if (clientDataModel.getMethod().equals("card") && clientDataModel.getRateSel().contains("autopay")) {
            mchtId = constant.PG_MID_AUTO;
        } else if (clientDataModel.getMethod().equals("card")) {
            mchtId = constant.PG_MID_CARD;
        } else {
            mchtId = constant.PG_MID;
        }

        responseMessage.put("resultCode", EnumResultCode.SUCCESS.getCode());
        responseMessage.put("resultMsg", EnumResultCode.SUCCESS.getValue());
        responseMessage.put("mchtId", mchtId);
        responseMessage.put("method", clientDataModel.getMethod());
        responseMessage.put("trdDt", trdDt);
        responseMessage.put("trdTm", trdTm);
        responseMessage.put("mchtTrdNo", tradeNum);
        responseMessage.put("trdAmt", clientDataModel.getSalesPrice());
        responseMessage.put("hashCipher", paymentUseCase.aes256EncryptEcb(clientDataModel, tradeNum, trdDt, trdTm));
        responseMessage.put("encParams", paymentUseCase.encodeBase64(clientDataModel, tradeNum));

        logger.info("[resultCode] : [" + responseMessage.get("resultCode") + "]");
        logger.info("[resultMsg] : [" + responseMessage.get("resultMsg") + "]");
        logger.info(responseMessage.toString());
        logger.info("E ------------------------------[AGENCY] - [setPaymentSiteInfo] ------------------------------ E");
        return ResponseEntity.ok(responseMessage);
    }

    /**
     * Sets payment duck.
     *
     * @param duck the duck
     * @return the payment duck
     */
    @PostMapping("/setDuckPaymentSiteInfo")
    public ResponseEntity<?> setPaymentDuck(@RequestBody String duck) {
        System.out.println("====== " + duck + " ======");
        System.out.println("====== " + duck + " ======");
        System.out.println("====== " + duck + " ======");
        return ResponseEntity.ok(duck);
    }


    @PostMapping(value = "/cardCancel")
    public void requestCardCancelPayment(@RequestBody String requestSiteId) throws JsonProcessingException {
        Map<String, Object> requestData = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        String ver = "0A19"; // 전문의 버전 "0A19"고정값
        String method = "CA"; // 결제 수단 "CA" 고정값
        String bizType = "C0"; // 업무 구분코드 "C0" 고정값
        String encCd = "23"; //  암호화 구분 코드 "23" 고정값
        String mchtId = constant.getPG_CANCEL_MID_CARD(); // 상점아이디
        String mchtTrdNo = "DREAMSECCNCL" + UUID.randomUUID().toString().replace("-", "");
        LocalDateTime now = LocalDateTime.now();
        String trdDt = now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String trdTm = now.toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss"));

        params.put("mchtId", mchtId); // 헥토파이낸셜 부여 상점 아이디
        params.put("ver", ver); // 버전 (고정값)
        params.put("method", method); // 결제수단 (고정값)
        params.put("bizType", bizType); // 업무 구분 코드 (고정값)
        params.put("encCd", encCd);   // 암호화 구분 코드 (고정값)
        params.put("mchtTrdNo", mchtTrdNo); // 상점 주문번호
        params.put("trdDt", trdDt); // 취소 요청 일자
        params.put("trdTm", trdTm);   // 취소 요청 시간


        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> mapData = mapper.readValue(requestSiteId, Map.class);

        String agencyId = mapData.get("agencyId");
        String siteId = mapData.get("siteId").split("-")[1];

        Optional<PaymentHistoryDataModel> optPaymentHistory = paymentUseCase.getPaymentHistoryByAgency(agencyId, siteId)
                .stream()
                .filter(e -> e.getTradeNum().equals(mapData.get("tradeNum")))
                .findFirst();

        if (optPaymentHistory.isPresent()) {
            PaymentHistoryDataModel paymentHistoryDataModel = optPaymentHistory.get();
            Map<String, String> data = new HashMap<>();

            if (paymentHistoryDataModel.getRateSel().contains("autopay")) {
                mchtId = constant.getPG_CANCEL_MID_AUTO();
                params.put("mchtId", mchtId); // 헥토파이낸셜 부여 상점 아이디
            }

            data.put("orgTrdNo", paymentHistoryDataModel.getPgTradeNum()); // 원거래번호 : 결제시 헥토에서 발급한 거래번호
            data.put("crcCd", "KRW"); // 통화구분 "KRW" 고정값
            data.put("cnclOrd", "001"); // 취소 회차
            data.put("cnclAmt", paymentHistoryDataModel.getAmount()); // 취소 금액 AES 암호화

            try {
                // 취소요청일자 + 취소요청시간 + 상점아이디 + 상점주문번호 + 취소금액(평문) + 해쉬키
                data.put("pktHash", EncryptUtil.digestSHA256(
                        trdDt + trdTm + mchtId + mchtTrdNo + paymentHistoryDataModel.getAmount() + constant.LICENSE_KEY)
                );
                data.put("cnclAmt", Base64.getEncoder().encodeToString(EncryptUtil.aes256EncryptEcb(constant.AES256_KEY, data.get("cnclAmt"))));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            requestData.put("params", params);
            requestData.put("data", data);
            String url = constant.BILL_SERVER_URL + "/spay/APICancel.do";

            HttpClientUtil httpClientUtil = new HttpClientUtil();
            String resData = httpClientUtil.sendApi(url, requestData, 5000, 25000);
            System.out.println(resData);

            String hashCipher = "";
            String hashPlain = trdDt + trdTm + mchtId + mchtTrdNo + paymentHistoryDataModel.getAmount() + constant.LICENSE_KEY;

            /** SHA256 해쉬 처리 */
            try {
                hashCipher = EncryptUtil.digestSHA256(hashPlain);//해쉬 값
            } catch (Exception e) {
                logger.error("[" + params.get("mchtTrdNo") + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
            } finally {
                logger.info("[" + params.get("mchtTrdNo") + "][SHA256 HASHING] Plain Text[" + hashPlain + "] ---> Cipher Text[" + hashCipher + "]");
            }


            //TODO
            // 결제 취소 시 제휴사 알림 필요 사이트 상태는 : P로 전달
            String targetUrl = paymentUseCase.makeTargetUrl(agencyId, "SiteStatusMsg");
        }
    }


    /**
     * Request bill key payment.
     *
     * @param requestMsg the request map
     */
    @PostMapping(value = "/bill")
    public void requestBillKeyPayment(@RequestBody String requestMsg) {
        Map<String, Object> responseData = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        String ver = "0A19";
        String method = "CA";
        String bizType = "B0";
        String encCd = "23";
        String mchtId = constant.PG_MID_AUTO;
        String mchtTrdNo = "DREAMSECAUTO" + UUID.randomUUID().toString().replace("-", "");
        LocalDateTime now = LocalDateTime.now();
        String trdDt = now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String trdTm = now.toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss"));

        params.put("mchtId", mchtId); // 헥토파이낸셜 부여 상점 아이디
        params.put("ver", ver); // 버전 (고정값)
        params.put("method", method); // 결제수단 (고정값)
        params.put("bizType", bizType); // 업무 구분 코드 (고정값)
        params.put("encCd", encCd);   // 암호화 구분 코드 (고정값)
        params.put("mchtTrdNo", mchtTrdNo); // 상점 주문번호
        params.put("trdDt", trdDt); // 주문 날짜
        params.put("trdTm", trdTm);   // 주문 시간


        if (requestMsg.equals("BillPaymentService")) {
            List<ClientDataModel> agencyInfoList = agencyUseCase.selectAgencyInfo().stream()
                    .filter(e -> e.getSiteStatus().equals(EnumSiteStatus.ACTIVE.getCode()))
                    .filter(e -> e.getExtensionStatus().equals(EnumExtensionStatus.EXTENDABLE.getCode()))
                    .filter(e -> e.getScheduledRateSel() != null && e.getScheduledRateSel().contains("auto"))
                    .collect(Collectors.toList());

            agencyInfoList.forEach(agencyInfo -> {
                String convertSiteId = agencyInfo.getSiteId();

                Optional<ClientDataModel> clientDataModel = agencyUseCase.getAgencyInfo(new ClientDataModel(agencyInfo.getAgencyId(), convertSiteId));
                if (clientDataModel.isPresent()) {
                    ClientDataModel info = clientDataModel.get();

                    AgencyProducts products;
                    if (info.getScheduledRateSel() == null || info.getScheduledRateSel().isEmpty()) {
                        products = paymentUseCase.getAgencyProductByRateSel(info.getRateSel());
                    } else {
                        products = paymentUseCase.getAgencyProductByRateSel(info.getScheduledRateSel());
                    }
                    List<PaymentHistoryDataModel> paymentHistoryList = paymentUseCase.getPaymentHistoryByAgency(agencyInfo.getAgencyId(), convertSiteId)
                            .stream()
                            .filter((PaymentHistoryDataModel e) -> e.getTrTrace().equals(EnumTradeTrace.USED.getCode()))
                            .filter((PaymentHistoryDataModel e) -> e.getExtraAmountStatus().equals(EnumExtraAmountStatus.PASS.getCode()))
                            .collect(Collectors.toList());


                    Map<String, String> data = new HashMap<>();
                    String billKey = paymentHistoryList.get(0).getBillKey();

                    String productName = products.getName();

                    int excessCount = 0;
                    int excessAmount = 0;
                    if (paymentHistoryList.size() > 2) {
                        Map<String, Integer> excessMap = paymentUseCase.getExcessAmount(
                                paymentUseCase.getPaymentHistoryByAgency(info.getAgencyId(), info.getSiteId())
                        );
                        excessCount = excessMap.get("excessCount");
                        excessAmount = excessMap.get("excessAmount");
                    }

                    String amount = paymentUseCase.getAgencyProductByRateSel(info.getRateSel()).getPrice() + excessAmount;

                    data.put("pmtprdNm", productName);
                    data.put("mchtCustNm", "드림시큐리티");
                    data.put("mchtCustId", mchtId);
                    data.put("billKey", billKey);
                    data.put("instmtMon", "00"); // 할부개월
                    data.put("crcCd", "KRW");
                    data.put("trdAmt", amount);

                    try {
                        data.put("pktHash", EncryptUtil.digestSHA256(trdDt + trdTm + mchtId + mchtTrdNo + data.get("trdAmt") + constant.LICENSE_KEY));
                        data.put("trdAmt", Base64.getEncoder().encodeToString(EncryptUtil.aes256EncryptEcb(constant.AES256_KEY, data.get("trdAmt"))));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    String url = constant.BILL_SERVER_URL + "/spay/APICardActionPay.do";

                    HttpClientUtil httpClientUtil = new HttpClientUtil();

                    responseData.put("params", params);
                    responseData.put("data", data);
                    String reqData = httpClientUtil.sendApi(url, responseData, 5000, 25000);

                    String hashCipher = "";
                    String hashPlain = trdDt + trdTm + mchtId + mchtTrdNo + data.get("trdAmt") + constant.LICENSE_KEY;

                    /** SHA256 해쉬 처리 */
                    try {
                        hashCipher = EncryptUtil.digestSHA256(hashPlain);//해쉬 값
                    } catch (Exception e) {
                        logger.error("[" + params.get("mchtTrdNo") + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
                    } finally {
                        logger.info("[" + params.get("mchtTrdNo") + "][SHA256 HASHING] Plain Text[" + hashPlain + "] ---> Cipher Text[" + hashCipher + "]");
                    }
                    paymentUseCase.insertAutoPayPaymentHistory(agencyInfo.getAgencyId(), convertSiteId, paymentUseCase.getAgencyProductByRateSel(info.getRateSel()), reqData);
                }
            });
        }

    }


    /* 결제대기상태가 아닌 경우, 초기 결제로 판단 제휴사 승인대기, 통신사 승인대기 상태 전달. */

    /**
     * @param responseMessage
     * @param clientInfo
     * @return
     */
    private ResponseEntity<?> decideSiteStatus(Map<String, Object> responseMessage, ClientDataModel clientInfo) {
        // TRADE_PENDING : 결제 대기 상태 (통신사 심사 승인 완료 이후 결제 대기 알림 발송완료 상태)
        // TELCO_PENDING : 통신사 승인 대기 ( 관리자가 가맹점 등록 후 통신사 정보 등록 전 상태 )
        // PENDING : 제휴사 승인 대기 ( 관리자가 등록하기 전 상태 )

        if (clientInfo.getSiteStatus().equals(EnumSiteStatus.SUSPENDED.getCode())) {
            responseMessage.put("resultCode", EnumResultCode.SuspendedSiteId.getCode());
            responseMessage.put("resultMsg", EnumResultCode.SuspendedSiteId.getValue());
            return ResponseEntity.ok(responseMessage);
        }

        if (!clientInfo.getSiteStatus().equals(EnumSiteStatus.TRADE_PENDING.getCode())) {
            if (clientInfo.getSiteStatus().equals(EnumSiteStatus.REJECT.getCode())) {
                responseMessage.put("resultCode", EnumResultCode.RejectAgency.getCode());
                responseMessage.put("resultMsg", EnumResultCode.RejectAgency.getValue());
                return ResponseEntity.ok(responseMessage);
            } else if (clientInfo.getSiteStatus().equals(EnumSiteStatus.PENDING.getCode())) {
                responseMessage.put("resultCode", EnumResultCode.PendingApprovalStatus.getCode());
                responseMessage.put("resultMsg", EnumResultCode.PendingApprovalStatus.getValue());
                return ResponseEntity.ok(responseMessage);
            }
        }
        return null;
    }


    private String decideRateSel(ClientDataModel clientInfo, ClientDataModel clientDataModel) {
        return clientDataModel.getRateSel() != null && !clientDataModel.getRateSel().isEmpty() ? clientDataModel.getRateSel() : clientInfo.getRateSel() != null ? clientInfo.getRateSel() : null;
    }

    private String decideStartDate(SimpleDateFormat sdf, ClientDataModel clientInfo, ClientDataModel clientDataModel) {
        Date startDateClient = clientDataModel.getStartDate();
        Date startDateInfo = clientInfo.getStartDate();

        // 초기 결제인 경우
        if (clientInfo.getExtensionStatus().equals(EnumExtensionStatus.DEFAULT.getCode())) {
            if (startDateClient != null) {
                return sdf.format(startDateClient);
            } else if (startDateInfo != null) {
                return sdf.format(startDateInfo);
            } else {
                return null;
            }
            // 연장이 활성화된 경우
        } else if (clientInfo.getExtensionStatus().equals(EnumExtensionStatus.EXTENDABLE.getCode())) {
            Date endDate = clientInfo.getEndDate();

            // 만료일로부터 15일 전 계산
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_MONTH, -DAYS_BEFORE_EXPIRATION);
            Date fifteenDaysBeforeExpiration = calendar.getTime();

            Calendar yesterDayCal = Calendar.getInstance();
            yesterDayCal.add(Calendar.DATE, -1);
            Date yesterday = yesterDayCal.getTime();

            if (startDateClient != null) {
                // 요청된 시작일이 제공된 경우, 그 시작일은 만료일로부터 15일 전 이후여야 함
                if (startDateClient.after(fifteenDaysBeforeExpiration) && startDateClient.after(yesterday)) {
                    return sdf.format(startDateClient);
                } else {
                    // 잘못된 시작일을 처리하는 방법을 여기서 처리 (예외 던지기 등)
                    throw new NoExtensionException(EnumResultCode.NoExtension, clientInfo.getSiteId());
                }
            } else {
                // 요청된 시작일이 제공되지 않은 경우, 시작일을 만료일 다음 날짜로 설정
                Calendar nextEndDate = Calendar.getInstance();
                nextEndDate.setTime(endDate);
                nextEndDate.add(Calendar.DATE, 1);
                return sdf.format(nextEndDate.getTime());
            }
        } else {
            throw new NoExtensionException(EnumResultCode.NoExtension, clientInfo.getSiteId());
        }
    }

    private String makeTargetUrl(Optional<AgencyInfoKey> agencyInfoKey, String agencyId) throws JsonProcessingException {
        String msgType = "";
        if (agencyInfoKey.isPresent()) {
            AgencyInfoKey info = agencyInfoKey.get();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> agencyUrlJson = mapper.readValue(info.getAgencyUrl(), new TypeReference<>() {
            });
            EnumAgency[] enumAgencies = EnumAgency.values();
            for (EnumAgency enumAgency : enumAgencies) {
                if (enumAgency.getCode().equals(agencyId)) {
                    msgType = enumAgency.getPaymentMsg();
                    break;
                }
            }
            return agencyUrlJson.get(msgType);
        }
        return "";
    }


}
