package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.bizconnect.adapter.out.persistence.entity.StatDayJpaEntity;
import com.bizconnect.application.domain.enums.EnumBillingBase;
import com.bizconnect.application.domain.enums.EnumExtensionStatus;
import com.bizconnect.application.domain.enums.EnumPaymentStatus;
import com.bizconnect.application.domain.enums.EnumTradeTrace;
import com.bizconnect.application.domain.model.*;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.exceptions.NoExtensionException;
import com.bizconnect.application.exceptions.exceptions.ValueException;
import com.bizconnect.application.port.in.PaymentUseCase;
import com.bizconnect.application.port.in.StatUseCase;
import com.bizconnect.application.port.out.load.LoadAgencyProductDataPort;
import com.bizconnect.application.port.out.load.LoadEncryptDataPort;
import com.bizconnect.application.port.out.load.LoadPaymentDataPort;
import com.bizconnect.application.port.out.load.LoadStatDataPort;
import com.bizconnect.application.port.out.save.SavePaymentDataPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class PaymentService implements PaymentUseCase, StatUseCase {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final Constant constant;
    private final AgencyService agencyService;
    Logger logger = LoggerFactory.getLogger("HFInitController");
    private final LoadPaymentDataPort loadPaymentDataPort;
    private final LoadEncryptDataPort loadEncryptDataPort;
    private final LoadAgencyProductDataPort loadAgencyProductDataPort;
    private final LoadStatDataPort loadStatDataPort;
    private final SavePaymentDataPort savePaymentDataPort;

    public PaymentService(Constant constant, AgencyService agencyService, LoadPaymentDataPort loadPaymentDataPort, LoadEncryptDataPort loadEncryptDataPort, LoadAgencyProductDataPort loadAgencyProductDataPort, LoadStatDataPort loadStatDataPort, SavePaymentDataPort savePaymentDataPort) {
        this.constant = constant;
        this.agencyService = agencyService;
        this.loadPaymentDataPort = loadPaymentDataPort;
        this.loadEncryptDataPort = loadEncryptDataPort;
        this.loadAgencyProductDataPort = loadAgencyProductDataPort;
        this.loadStatDataPort = loadStatDataPort;
        this.savePaymentDataPort = savePaymentDataPort;
    }


    //TODO
    // [중요!] 최적화
    @Override
    public void checkMchtParams(ClientDataModel clientDataModel) throws ParseException {
        String clientPrice = clientDataModel.getSalesPrice();
        Calendar startDateByCal = Calendar.getInstance();
        Calendar endDateByCal = Calendar.getInstance();
        Calendar yesterDayCal = Calendar.getInstance();
        yesterDayCal.add(Calendar.DATE, -1);
        Date yesterday = yesterDayCal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String agencyId = clientDataModel.getAgencyId();
        String siteId = clientDataModel.getSiteId();
        String rateSel = clientDataModel.getRateSel();
        if (sdf.parse(sdf.format(clientDataModel.getStartDate())).before(yesterday)) {
            throw new NoExtensionException(EnumResultCode.NoExtension, siteId);
        }
        startDateByCal.setTime(sdf.parse(sdf.format(clientDataModel.getStartDate())));
        int offer;
        double price;
        int clientOffer = Integer.parseInt(clientDataModel.getOffer());
        String endDate = "";
        String clientEndDate = sdf.format(clientDataModel.getEndDate());


        AgencyProducts agencyProducts = getAgencyProductByRateSel(rateSel);

        int lastDate = startDateByCal.getActualMaximum(Calendar.DATE);
        int startDate = startDateByCal.get(Calendar.DATE);

        int durations = lastDate - startDate + 1;
        int baseOffer = Integer.parseInt(agencyProducts.getOffer()) / Integer.parseInt(agencyProducts.getMonth());
        int basePrice = Integer.parseInt(agencyProducts.getPrice()) / Integer.parseInt(agencyProducts.getMonth());
        int dataMonth = Integer.parseInt(agencyProducts.getMonth());

        offer = (baseOffer * (dataMonth - 1)) + (baseOffer * durations / lastDate);
        price = ((((double) (basePrice * durations) / lastDate) + (basePrice * (dataMonth - 1))) * 1.1);

        endDateByCal.set(Calendar.MONTH, startDateByCal.get(Calendar.MONTH));
        System.out.println(startDateByCal.get(Calendar.MONTH));
        if (Integer.parseInt(agencyProducts.getMonth()) == 1) {
            if (durations <= 14) {
                endDateByCal.add(Calendar.MONTH, Integer.parseInt(agencyProducts.getMonth()));
                offer = (baseOffer) + (baseOffer * durations / lastDate);
                price = ((((double) (basePrice * durations) / lastDate) + basePrice) * 1.1);
            } else {
                offer = (baseOffer * durations / lastDate);
                price = (((double) (basePrice * durations) / lastDate) * 1.1);
            }
        } else {
            endDateByCal.add(Calendar.MONTH, Integer.parseInt(agencyProducts.getMonth()) - 1);
        }

        Optional<ClientDataModel> info = agencyService.getAgencyInfo(new ClientDataModel(agencyId, siteId));

        if (info.get().getExtensionStatus().equals(EnumExtensionStatus.EXTENDABLE.getCode())) {
            List<PaymentHistoryDataModel> list = getPaymentHistoryByAgency(agencyId, siteId);

            if (sdf.parse(sdf.format(clientDataModel.getStartDate())).before(info.get().getEndDate())) {
                throw new NoExtensionException(EnumResultCode.NoExtension, siteId);
            }
            endDateByCal.setTime(sdf.parse(sdf.format(clientDataModel.getStartDate())));

            if (agencyProducts.getMonth().equals("1")) {
                if (durations <= 14) {
                    endDateByCal.add(Calendar.MONTH, 1);
                }
            } else {
                endDateByCal.add(Calendar.MONTH, Integer.parseInt(agencyProducts.getMonth()) - 1);
            }

            int excessCount;
            double excessAmount = 0;
            if (list.size() > 2) {
                excessCount = Integer.parseInt(list.get(1).getOffer()) - Integer.parseInt(list.get(1).getUseCount());
                if (excessCount < 0) {
                    excessAmount = Math.abs(excessCount) * Integer.parseInt(agencyProducts.getExcessPerCase()) * 1.1;
                }
            }
            price += excessAmount;
        }


        endDateByCal.set(Calendar.DAY_OF_MONTH, endDateByCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = sdf.format(endDateByCal.getTime());
        if (offer != clientOffer || String.valueOf(Math.floor(price)).equals(clientPrice) || !endDate.equals(clientEndDate)) {
            throw new ValueException(offer, clientOffer, (int) Math.floor(price), clientPrice, endDate, clientEndDate, agencyId, siteId);
        }

        logger.info("S ------------------------------[AGENCY] - [setPaymentSiteInfo] ------------------------------ S");
        logger.info("[agencyId] : [" + agencyId + "]");
        logger.info("[siteId] : [" + siteId + "]");
        logger.info("[rateSel] : [" + agencyProducts.getRateSel() + ", " + agencyProducts.getName() + "]");
        logger.info("[startDate] : [" + sdf.format(startDateByCal.getTime()) + "]");
        logger.info("[endDate] : [" + endDate + "]");
        logger.info("[offer] : [" + offer + "]");
        logger.info("[price] : [" + (int) Math.floor(price) + "]");
    }

    @Override
    public String aes256EncryptEcb(ClientDataModel clientDataModel, String tradeNum, String trdDt, String trdTm) {
        String licenseKey = constant.LICENSE_KEY;
        String mchtId;
        if (clientDataModel.getMethod().equals("card") && clientDataModel.getRateSel().contains("autopay")) {
            mchtId = constant.PG_MID_AUTO;
        } else if (clientDataModel.getMethod().equals("card")) {
            mchtId = constant.PG_MID_CARD;
        } else {
            mchtId = constant.PG_MID;
        }
        String hashPlain = new PaymentDataModel(
                mchtId,
                clientDataModel.getMethod(),
                tradeNum,
                trdDt,
                trdTm,
                clientDataModel.getSalesPrice()
        ).getHashPlain() + licenseKey;
        String hashCipher = "";
        /** SHA256 해쉬 처리 */
        try {
            hashCipher = EncryptUtil.digestSHA256(hashPlain);//해쉬 값
        } catch (Exception e) {
            logger.error("[" + tradeNum + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
        } finally {
            logger.info("[" + tradeNum + "][SHA256 HASHING] Plain Text[" + hashPlain + "] ---> Cipher Text[" + hashCipher + "]");
        }
        return hashCipher;

    }

    @Override
    public HashMap<String, String> encodeBase64(ClientDataModel clientDataModel, String tradeNum) {
        String aesKey = constant.AES256_KEY;
//        HashMap<String, String> params = convertToMap(clientDataModel);
        HashMap<String, String> params = new HashMap<>();
        params.put("trdAmt", clientDataModel.getSalesPrice());

        System.out.println("encode Base 64 " + clientDataModel);
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                String aesPlain = params.get(key);
                if (!("".equals(aesPlain))) {
                    byte[] aesCipherRaw = EncryptUtil.aes256EncryptEcb(aesKey, aesPlain);
                    String aesCipher = EncryptUtil.encodeBase64(aesCipherRaw);

                    params.put(key, aesCipher);//암호화된 데이터로 세팅
                    logger.info("[" + tradeNum + "][AES256 Encrypt] " + key + "[" + aesPlain + "] ---> [" + aesCipher + "]");
                }
            }
        } catch (Exception e) {
            logger.error("[" + tradeNum + "][AES256 Encrypt] AES256 Fail! : " + e.toString());
        }
        return params;
    }

    @Override
    public List<PaymentHistoryDataModel> getPaymentHistoryByAgency(String agencyId, String siteId) {
        List<PaymentHistory> paymentHistories = loadPaymentDataPort.getPaymentHistoryByAgency(new Agency(agencyId, siteId));
        return paymentHistories.stream()
                .map(this::convertClient)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentHistoryDataModel getPaymentHistoryByAgencyLastPayment(String agencyId, String siteId) {
        return convertClient(loadPaymentDataPort.getPaymentHistoryByAgencyLastPayment(new Agency(agencyId, siteId)));
    }


    @Override
    public String makeTradeNum() {
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        String formattedRandomNum = String.format("%04d", randomNum);
        return "DREAMSEC" + formatter.format(LocalDateTime.now()) + formattedRandomNum;
    }

    @Override
    public AgencyProducts getAgencyProductByRateSel(String rateSel) {
        return loadAgencyProductDataPort.getAgencyProductByRateSel(rateSel);
    }

    @Override
    public int getExcessAmount(List<PaymentHistoryDataModel> list) {
        SimpleDateFormat convertFormat = new SimpleDateFormat("yyyyMMdd");

        List<PaymentHistoryDataModel> checkedList = list.stream()
                .filter(e -> e.getTrTrace().equals(EnumTradeTrace.USED.getCode()))
                .collect(Collectors.toList());

        if (checkedList.size() < 2) {
            return 0;
        }

        PaymentHistoryDataModel overPaymentTarget = checkedList.get(2);
        Date convertedStartDate = overPaymentTarget.getStartDate();
        Date convertedEndDate = overPaymentTarget.getEndDate();

        String agencyId = overPaymentTarget.getAgencyId();
        if (agencyId == null) {
            return 0;
        }
        String billingBase = loadEncryptDataPort.getAgencyInfoKey(agencyId)
                .map(AgencyInfoKey::getBillingBase)
                .orElse(null);

        if (billingBase == null) {
            return 0;
        }

        String startDate = convertFormat.format(convertedStartDate);
        String endDate = convertFormat.format(convertedEndDate);

        List<StatDay> findStatDayList = getUseCountBySiteId(
                overPaymentTarget.getSiteId(),
                startDate,
                endDate
        );

        System.out.println(findStatDayList);

        long incompleteCountSum = getIncompleteCount(billingBase, findStatDayList);
        System.out.println(incompleteCountSum);

        AgencyProducts products = getAgencyProductByRateSel(overPaymentTarget.getRateSel());
        int offer = Integer.parseInt(overPaymentTarget.getOffer());
        int excessCount = offer - (int) incompleteCountSum;

        return excessCount < 0 ? (int) (Math.abs(excessCount) * Integer.parseInt(products.getExcessPerCase()) * 1.1) : 0;
    }

    @Override
    public void insertAutoPayPaymentHistory(String agencyId, String siteId, AgencyProducts products, String reqData) {
        System.out.println(reqData);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(reqData, new TypeReference<>() {
            });
            String params = mapper.writeValueAsString(map.get("params"));
            String data = mapper.writeValueAsString(map.get("data"));
            Map<String, String> paramsMap = mapper.readValue(params, new TypeReference<>() {
            });
            Map<String, String> dataMap = mapper.readValue(data, new TypeReference<>() {
            });

            System.out.println("outStatCd : " + paramsMap.get("outStatCd"));
            System.out.println("outRsltCd : " + paramsMap.get("outRsltCd"));
            System.out.println("outRsltMsg : " + paramsMap.get("outRsltMsg"));

            if ("0021".equals(paramsMap.get("outStatCd"))) {

                byte[] decodeBase64 = EncryptUtil.decodeBase64(dataMap.get("trdAmt"));
                byte[] resultByte = EncryptUtil.aes256DecryptEcb(constant.AES256_KEY, decodeBase64);
                String decryptedAmount = new String(resultByte, "UTF-8");

                DateTimeFormatter trdDtFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime trDate = LocalDateTime.parse(paramsMap.get("trdDt") + paramsMap.get("trdTm"), trdDtFormat);
                Instant instant = trDate.atZone(ZoneId.systemDefault()).toInstant();
                Date trDateAsDate = Date.from(instant);

                Calendar startDateCal = Calendar.getInstance();
                startDateCal.add(Calendar.MONTH, 1);
                startDateCal.set(Calendar.DAY_OF_MONTH, startDateCal.getActualMinimum(Calendar.DAY_OF_MONTH));

                Calendar endDateCal = Calendar.getInstance();
                endDateCal.add(Calendar.MONTH, 1);
                endDateCal.set(Calendar.DAY_OF_MONTH, endDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));

                Calendar cal = Calendar.getInstance();
                Date regDate = cal.getTime();

                PaymentHistory paymentHistory = PaymentHistory.builder()
                        .tradeNum(paramsMap.get("mchtTrdNo"))
                        .pgTradeNum(paramsMap.get("trdNo"))
                        .agencyId(agencyId)
                        .siteId(siteId)
                        .paymentType(paramsMap.get("method"))
                        .rateSel(products.getRateSel())
                        .amount(decryptedAmount)
                        .offer(products.getOffer())
                        .trTrace(EnumTradeTrace.USED.getCode())
                        .paymentStatus(EnumPaymentStatus.ACTIVE.getCode())
                        .trDate(trDateAsDate)
                        .startDate(sdf.parse(sdf.format(startDateCal.getTime())))
                        .endDate(sdf.parse(sdf.format(endDateCal.getTime())))
                        .billKey(dataMap.get("billKey"))
                        .billKeyExpireDate(dataMap.get("vldDtYear") + dataMap.get("vldDtMon"))
                        .regDate(regDate)
                        .build();

                savePaymentDataPort.insertPayment(paymentHistory);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) throws ParseException {
        String trdDt = "20240226";
        String trdTm = "090202";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter trdDtFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(trdDt + trdTm, trdDtFormat);
        System.out.println(dateTime);
    }

    private long getIncompleteCount(String billingBase, List<StatDay> statDays) {
        if (billingBase.equals(EnumBillingBase.INCOMPLETE.getCode())) {
            LongStream incompleteCounts = statDays.stream().mapToLong(StatDay::getIncompleteCnt);
            return incompleteCounts.sum();
        } else if (billingBase.equals(EnumBillingBase.FINAL_SUCCESS.getCode())) {
            LongStream successFinalCnt = statDays.stream().mapToLong(StatDay::getIncompleteCnt);
            return successFinalCnt.sum();
        } else {
            return 0L;
        }
    }

    @Override
    public List<StatDay> getUseCountBySiteId(String siteId, String startDate, String endDate) {
        return loadStatDataPort.findBySiteIdAndFromDate(siteId, startDate, endDate);
    }

    //TODO
    // Mapper 클래스로 뺄 필요가 있는지 확인 (ClientSideDataModel [DTO] <-> Domain)
    private PaymentHistoryDataModel convertClient(PaymentHistory paymentHistory) {
        return new PaymentHistoryDataModel(
                paymentHistory.getTradeNum(),
                paymentHistory.getPgTradeNum(),
                paymentHistory.getAgencyId(),
                paymentHistory.getSiteId(),
                paymentHistory.getPaymentType(),
                paymentHistory.getRateSel(),
                paymentHistory.getAmount(),
                paymentHistory.getOffer(),
                paymentHistory.getUseCount(),
                paymentHistory.getTrTrace(),
                paymentHistory.getPaymentStatus(),
                paymentHistory.getTrDate(),
                paymentHistory.getStartDate(),
                paymentHistory.getEndDate(),
                paymentHistory.getRcptName(),
                paymentHistory.getBillKey(),
                paymentHistory.getBillKeyExpireDate(),
                paymentHistory.getVbankName(),
                paymentHistory.getVbankCode(),
                paymentHistory.getVbankAccount(),
                paymentHistory.getVbankExpireDate(),
                paymentHistory.getRegDate(),
                paymentHistory.getModDate(),
                paymentHistory.getMemo()
        );
    }
}
