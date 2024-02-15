package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.bizconnect.application.domain.enums.EnumExtensionStatus;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.AgencyProducts;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.exceptions.NoExtensionException;
import com.bizconnect.application.exceptions.exceptions.ValueException;
import com.bizconnect.application.port.in.PaymentUseCase;
import com.bizconnect.application.port.out.load.LoadAgencyProductDataPort;
import com.bizconnect.application.port.out.load.LoadPaymentDataPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentService implements PaymentUseCase {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final Constant constant;
    private final AgencyService agencyService;
    Logger logger = LoggerFactory.getLogger("HFInitController");

    private final LoadPaymentDataPort loadPaymentDataPort;

    private final LoadAgencyProductDataPort loadAgencyProductDataPort;

    public PaymentService(Constant constant, AgencyService agencyService, LoadPaymentDataPort loadPaymentDataPort, LoadAgencyProductDataPort loadAgencyProductDataPort) {
        this.constant = constant;
        this.agencyService = agencyService;
        this.loadPaymentDataPort = loadPaymentDataPort;
        this.loadAgencyProductDataPort = loadAgencyProductDataPort;
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


        Optional<AgencyProducts> products = loadAgencyProductDataPort.getAgencyProductList(rateSel);
        AgencyProducts agencyProducts = products.orElse(null);

        int lastDate = startDateByCal.getActualMaximum(Calendar.DATE);
        int startDate = startDateByCal.get(Calendar.DATE);

        int durations = lastDate - startDate + 1;
        int baseOffer = Integer.parseInt(agencyProducts.getOffer()) / Integer.parseInt(agencyProducts.getMonth());
        int basePrice = Integer.parseInt(agencyProducts.getPrice()) /Integer.parseInt(agencyProducts.getMonth());
        int dataMonth = Integer.parseInt(agencyProducts.getMonth());

        offer = (baseOffer * (dataMonth - 1)) + (baseOffer * durations / lastDate);
        price = ((((double) (basePrice * durations) / lastDate) + (basePrice * (dataMonth - 1))) * 1.1);

        endDateByCal.set(Calendar.MONTH, startDateByCal.get(Calendar.MONTH));
        System.out.println(startDateByCal.get(Calendar.MONTH));
        if (Integer.parseInt(agencyProducts.getMonth()) == 1) {
            if (durations <= 14) {
                endDateByCal.add(Calendar.MONTH,  Integer.parseInt(agencyProducts.getMonth()));
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

            if (agencyProducts.getMonth().equals("1")){
                if (durations <= 14) {
                    endDateByCal.add(Calendar.MONTH, 1);
                }
            } else {
                endDateByCal.add(Calendar.MONTH, Integer.parseInt(agencyProducts.getMonth()) - 1);
            }


            int excessCount;
            double excessAmount = 0;

            if (list.size() > 2) {
                excessCount = Integer.parseInt(list.get(1).getOffer()) - list.get(1).getUseCount();
                if (excessCount < 0) {
                    excessAmount = Math.abs(excessCount) * 50 * 1.1;
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
    public String aes256EncryptEcb(ClientDataModel clientDataModel , String tradeNum, String trdDt, String trdTm) {
        String licenseKey = constant.LICENSE_KEY;
        String mchtId;
        if (clientDataModel.getMethod().equals("card") && clientDataModel.getRateSel().contains("autopay")) {
            mchtId = constant.PG_MID_AUTO;
        } else if (clientDataModel.getMethod().equals("card")){
            mchtId = constant.PG_MID_CARD;
        }else {
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
        return loadPaymentDataPort.getPaymentHistoryByAgency(new Agency(agencyId, siteId));
    }


    @Override
    public String makeTradeNum() {
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        String formattedRandomNum = String.format("%04d", randomNum);
        return "DREAMSEC" + formatter.format(LocalDateTime.now()) + formattedRandomNum;
    }


    public HashMap<String, String> convertToMap(PaymentDataModel paymentDataModel) {
        HashMap<String, String> map = new HashMap<>();
//        map.put("trdAmt", paymentDataModel.getPlainTrdAmt());
//        map.put("mchtCustId", paymentDataModel.getPlainMchtCustId());
//        map.put("cphoneNo", paymentDataModel.getPlainCphoneNo());
//        map.put("email", paymentDataModel.getPlainEmail());
//        map.put("mchtCustNm", paymentDataModel.getPlainMchtCustNm());
//        map.put("taxAmt", paymentDataModel.getPlainTaxAmt());
//        map.put("vatAmt", paymentDataModel.getPlainTrdAmt());
//        map.put("taxFreeAmt", paymentDataModel.getPlainTaxFreeAmt());
//        map.put("svcAmt", paymentDataModel.getPlainSvcAmt());
//        map.put("clipCustNm", paymentDataModel.getPlainClipCustNm());
//        map.put("clipCustCi", paymentDataModel.getPlainClipCustCi());
//        map.put("clipCustPhoneNo", paymentDataModel.getPlainClipCustPhoneNo());
//        map.put("mchtParam", paymentDataModel.getMchtParam());

        map.put("trdAmt", paymentDataModel.getPlainTrdAmt());
//        map.put("mchtCustId", paymentDataModel.getPlainMchtCustId());
//        map.put("cphoneNo", paymentDataModel.getPlainCphoneNo());
//        map.put("email", paymentDataModel.getPlainEmail());
//        map.put("mchtCustNm", paymentDataModel.getPlainMchtCustNm());
//        map.put("taxAmt", paymentDataModel.getPlainTaxAmt());
//        map.put("vatAmt", paymentDataModel.getPlainTrdAmt());
//        map.put("taxFreeAmt", paymentDataModel.getPlainTaxFreeAmt());
//        map.put("svcAmt", paymentDataModel.getPlainSvcAmt());
//        map.put("clipCustNm", paymentDataModel.getPlainClipCustNm());
//        map.put("clipCustCi", paymentDataModel.getPlainClipCustCi());
//        map.put("clipCustPhoneNo", paymentDataModel.getPlainClipCustPhoneNo());
        map.put("mchtParam", paymentDataModel.getMchtParam());
        return map;
    }

    private Map<String, String> parseParams(String[] pairs) {
        Map<String, String> parsedParams = new HashMap<>();
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                parsedParams.put(keyValue[0], keyValue[1]);
            }
        }
        return parsedParams;
    }

}
