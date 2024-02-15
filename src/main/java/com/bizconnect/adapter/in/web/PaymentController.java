package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.bizconnect.adapter.out.payment.utils.HttpClientUtil;
import com.bizconnect.application.domain.enums.EnumExtensionStatus;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.domain.enums.EnumSiteStatus;
import com.bizconnect.application.exceptions.exceptions.IllegalAgencyIdSiteIdException;
import com.bizconnect.application.exceptions.exceptions.NoExtensionException;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.bizconnect.application.port.in.PaymentUseCase;
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

@Slf4j
@RestController
@RequestMapping(value = {"/agency/payment", "/payment"})
public class PaymentController {

    private static final int DAYS_BEFORE_EXPIRATION = 15;
    private final AgencyUseCase agencyUseCase;
    private final Constant constant;
    private final PaymentUseCase paymentUseCase;

    @Value("${external.url}")
    private String profileSpecificUrl;

    @Value("${external.payment.url}")
    private String profileSpecificPaymentUrl;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PaymentController(AgencyUseCase agencyUseCase, Constant constant, PaymentUseCase paymentUseCase) {
        this.agencyUseCase = agencyUseCase;
        this.constant = constant;
        this.paymentUseCase = paymentUseCase;
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

            if (clientInfo.getExtensionStatus().equals(EnumExtensionStatus.EXTENDABLE.getCode())) {
                checkExtraCount(responseMessage, paymentUseCase.getPaymentHistoryByAgency(clientInfo.getAgencyId(), clientInfo.getSiteId()));
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
        responseMessage.put("siteId", clientDataModel.getSiteId());
        responseMessage.put("listSel", productTypes);

        logger.info("[resultCode] : [" + responseMessage.get("resultCode") + "]");
        logger.info("[resultMsg] : [" + responseMessage.get("resultMsg") + "]");
        logger.info("E ------------------------------[AGENCY] - [getPaymentInfo] ------------------------------ E");
        return ResponseEntity.ok(responseMessage);
    }


    @PostMapping("/setPaymentSiteInfo")
    public ResponseEntity<?> setPaymentSiteInfo(@RequestBody ClientDataModel clientDataModel) {
        Map<String, Object> responseMessage = new HashMap<>();
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        String tradeNum = paymentUseCase.makeTradeNum();
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

    @PostMapping("/setDuckPaymentSiteInfo")
    public ResponseEntity<?> setPaymentDuck(@RequestBody String duck) {
        System.out.println("====== " + duck + " ======");
        System.out.println("====== " + duck + " ======");
        System.out.println("====== " + duck + " ======");
        return ResponseEntity.ok(duck);
    }


    @PostMapping(value = "/bill")
    public void requestBillKeyPayment(@RequestBody Map<String, String> requestMap) {
        Map<String, Object> requestData = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        String ver = "0A19";
        String method = "CA";
        String bizType = "B0";
        String encCd = "23";
        String mchtId = constant.PG_MID_AUTO;
        String mchtTrdNo = "DREAMSEC" + UUID.randomUUID().toString().replace("-", "");
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

        System.out.println(requestMap.get("siteId"));
        System.out.println(requestMap.get("billKey"));
        System.out.println(requestMap.get("trdAmt"));
        System.out.println(requestMap.get("pmtprdNm"));

        //TODO
        // 정기결제 요청 데이터 생성
        //
        Map<String, String> data = new HashMap<>();
        String billKey = "SBILL_PGCAnxca_jt_gu20249964770206160258";

        data.put("pmtprdNm", "테스트상품");
        data.put("mchtCustNm", "드림시큐리티");
        data.put("mchtCustId", "Dreamsecurity");
        data.put("billKey", billKey);
        data.put("instmtMon", "00"); // 할부개월
        data.put("crcCd", "KRW");
        data.put("trdAmt", "1000");
        try {
            data.put("pktHash", EncryptUtil.digestSHA256(trdDt + trdTm + mchtId + mchtTrdNo + data.get("trdAmt") + constant.LICENSE_KEY));
            data.put("trdAmt", Base64.getEncoder().encodeToString(EncryptUtil.aes256EncryptEcb(constant.AES256_KEY, data.get("trdAmt"))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        requestData.put("params", params);
        requestData.put("data", data);
        String url = constant.BILL_SERVER_URL + "/spay/APICardActionPay.do";

        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String resData = httpClientUtil.sendApi(url, requestData, 5000, 25000);
        System.out.println(resData);
    }


    private ResponseEntity<?> decideSiteStatus(Map<String, Object> responseMessage, ClientDataModel clientInfo) {
        if (clientInfo.getSiteStatus().equals(EnumSiteStatus.TELCO_PENDING.getCode())) {
            responseMessage.put("resultCode", EnumResultCode.PendingTelcoApprovalStatus.getCode());
            responseMessage.put("resultMsg", EnumResultCode.PendingTelcoApprovalStatus.getValue());
            return ResponseEntity.ok(responseMessage);
        } else if (clientInfo.getSiteStatus().equals(EnumSiteStatus.PENDING.getCode())) {
            responseMessage.put("resultCode", EnumResultCode.PendingApprovalStatus.getCode());
            responseMessage.put("resultMsg", EnumResultCode.PendingApprovalStatus.getValue());
            return ResponseEntity.ok(responseMessage);
        }
        return null;
    }


    private String decideRateSel(ClientDataModel clientInfo, ClientDataModel clientDataModel) {
        return clientDataModel.getRateSel() != null && !clientDataModel.getRateSel().isEmpty() ? clientDataModel.getRateSel() : clientInfo.getRateSel() != null ? clientInfo.getRateSel() : null;
    }

    private String decideStartDate(SimpleDateFormat sdf, ClientDataModel clientInfo, ClientDataModel clientDataModel) {
        Date startDateClient = clientDataModel.getStartDate();
        Date startDateInfo = clientInfo.getStartDate();

        if (clientInfo.getExtensionStatus().equals(EnumExtensionStatus.DEFAULT.getCode())) {
            if (startDateClient != null) {
                return sdf.format(startDateClient);
            } else if (startDateInfo != null) {
                return sdf.format(startDateInfo);
            } else {
                return null;
            }
        } else if (clientInfo.getExtensionStatus().equals(EnumExtensionStatus.EXTENDABLE.getCode())) {
            // 연장이 활성화된 경우
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
        } else if (clientInfo.getExtensionStatus().equals(EnumExtensionStatus.NOT_EXTENDABLE.getCode())) {
            throw new NoExtensionException(EnumResultCode.NoExtension, clientInfo.getSiteId());
        }
        throw new IllegalAgencyIdSiteIdException(EnumResultCode.IllegalArgument, clientDataModel.getSiteId());
    }


    public void checkExtraCount(Map<String, Object> responseMessage, List<PaymentHistoryDataModel> list) {
        if (list.size() > 2) {
            int excessCount = Integer.parseInt(list.get(2).getOffer()) - list.get(2).getUseCount();
            responseMessage.put("excessAmount", Math.abs(excessCount) * 50 * 1.1);
        } else {
            responseMessage.put("excessAmount", 0);
        }
    }


}
