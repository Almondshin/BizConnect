package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.bizconnect.application.domain.enums.EnumProductType;
import com.bizconnect.application.domain.model.PaymentHistory;
import com.bizconnect.application.exceptions.exceptions.ValueException;
import com.bizconnect.application.port.in.PaymentUseCase;
import com.bizconnect.application.port.out.SavePaymentDataPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentService implements PaymentUseCase {

    private final Constant constant;
    Logger logger = LoggerFactory.getLogger("HFInitController");
    private final SavePaymentDataPort savePaymentDataPort;

    public PaymentService(Constant constant, SavePaymentDataPort savePaymentDataPort) {
        this.constant = constant;
        this.savePaymentDataPort = savePaymentDataPort;
    }

    @Override
    public String aes256EncryptEcb(PaymentDataModel paymentDataModel) {
        String licenseKey = constant.LICENSE_KEY;
        String hashPlain = new PaymentDataModel(
                paymentDataModel.getMchtId(),
                paymentDataModel.getMethod(),
                paymentDataModel.getMchtTrdNo(),
                paymentDataModel.getTrdDt(),
                paymentDataModel.getTrdTm(),
                paymentDataModel.getPlainTrdAmt()
        ).getHashPlain() + licenseKey;

        String hashCipher = "";
        /** SHA256 해쉬 처리 */
        try {
            hashCipher = EncryptUtil.digestSHA256(hashPlain);//해쉬 값
        } catch (Exception e) {
            logger.error("[" + paymentDataModel.getMchtTrdNo() + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
//            System.out.println("[" + paymentDataModel.getMchtTrdNo() + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
        } finally {
            logger.info("[" + paymentDataModel.getMchtTrdNo() + "][SHA256 HASHING] Plain Text[" + hashPlain + "] ---> Cipher Text[" + hashCipher + "]");
        }

        return hashCipher;
    }

    @Override
    public HashMap<String, String> encodeBase64(PaymentDataModel paymentDataModel) {
        String aesKey = constant.AES256_KEY;
        HashMap<String, String> params = convertToMap(paymentDataModel);
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                String aesPlain = params.get(key);
                if (!("".equals(aesPlain))) {
                    byte[] aesCipherRaw = EncryptUtil.aes256EncryptEcb(aesKey, aesPlain);
                    String aesCipher = EncryptUtil.encodeBase64(aesCipherRaw);

                    params.put(key, aesCipher);//암호화된 데이터로 세팅
                    logger.info("[" + paymentDataModel.getMchtTrdNo() + "][AES256 Encrypt] " + key + "[" + aesPlain + "] ---> [" + aesCipher + "]");
//                    System.out.println("[" + paymentDataModel.getMchtTrdNo() + "][AES256 Encrypt] " + key + "[" + aesPlain + "] ---> [" + aesCipher + "]");
                }
            }
        } catch (Exception e) {
            logger.error("[" + paymentDataModel.getMchtTrdNo() + "][AES256 Encrypt] AES256 Fail! : " + e.toString());
//            System.out.println("[" + paymentDataModel.getMchtTrdNo() + "][AES256 Encrypt] AES256 Fail! : " + e.toString());
        }
        return params;
    }

    @Override
    public void insertPaymentData(Map<String, String> resultMap) {

        String[] pairs = resultMap.get("mchtParam").split("&");

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


            switch (resultMap.get("method")) {
                case "card": {
                    PaymentHistory paymentHistory = new PaymentHistory(
                            resultMap.get("mchtTrdNo"),     //상점에서 생성한 TradeNum
                            resultMap.get("trdNo"),         //헥토파이낸셜 TradeNum
                            agencyId,
                            siteId,
                            resultMap.get("method"),        //결제수단
                            rateSel,                        //결제상품
                            resultMap.get("trdAmt"),        //결제금액
                            offer,
                            originalFormat.parse(resultMap.get("authDt")),        //거래일
                            startDate,
                            endDate,
                            "Y",
                            regDate
                    );
                    savePaymentDataPort.insertPayment(paymentHistory);
                    break;
                }
                case "vbank": {
                    PaymentHistory paymentHistory = new PaymentHistory(
                            resultMap.get("mchtTrdNo"),     //상점에서 생성한 TradeNum
                            resultMap.get("trdNo"),         //헥토파이낸셜 TradeNum
                            agencyId,
                            siteId,
                            resultMap.get("method"),        //결제수단
                            rateSel,                        //결제상품
                            resultMap.get("trdAmt"),        //결제금액
                            offer,
                            originalFormat.parse(resultMap.get("authDt")),        //거래일
                            "(주)드림시큐리티",
                            "M",
                            resultMap.get("fnNm"),
                            resultMap.get("fnCd"),
                            resultMap.get("vtlAcntNo"),
                            sdf.format(originalFormat.parse(resultMap.get("expireDt"))),
                            startDate,
                            endDate,
                            regDate
                    );
                    System.out.println("vBank paymentHistory : " + paymentHistory);
                    savePaymentDataPort.insertPayment(paymentHistory);
                    break;
                }

            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void checkMchtParams(PaymentDataModel paymentDataModel) {

        int clientPrice = Integer.parseInt(paymentDataModel.getPlainTrdAmt());
        Calendar startDateByCal = Calendar.getInstance();
        Calendar endDateByCal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String agencyId = "";
        String siteId = "";
        String rateSel = "";
        int offer;
        double price;
        int clientOffer = 0;
        String endDate = "";
        String clientEndDate = "";

        String[] pairs = decrypt(paymentDataModel.getMchtParam()).split("&");

        try {
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
                        case "rateSel": {
                            rateSel = keyValue[1];
                            break;
                        }
                        case "startDate": {
                            startDateByCal.setTime(sdf.parse(keyValue[1]));
                            break;
                        }
                        case "endDate": {
                            clientEndDate = keyValue[1];
                            break;
                        }
                        case "offer": {
                            clientOffer = Integer.parseInt(keyValue[1]);
                            break;
                        }
                    }
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // enumProductType.getType이랑 rateSel이랑 같은 열거형을 찾는다.
        EnumProductType productType = EnumProductType.getProductTypeByString(rateSel);
        int lastDate = startDateByCal.getActualMaximum(Calendar.DATE);
        int startDate = startDateByCal.get(Calendar.DATE);
        int durations = lastDate - startDate + 1;
        int baseOffer = productType.getBasicOffer() / productType.getMonth();
        int basePrice = productType.getPrice() / productType.getMonth();
        int dataMonth = productType.getMonth();

        //TODO
        // offer, Price  조겅이 필요함

        offer = (baseOffer * (dataMonth - 1)) + (baseOffer * durations / lastDate);
        price = ((((double) (basePrice * durations) / lastDate) + (basePrice * (dataMonth - 1))) * 1.1);

        if (productType.getMonth() == 1) {
            if (durations <= 15) {
                endDateByCal.add(Calendar.MONTH, startDateByCal.get(Calendar.MONTH) + productType.getMonth());
                offer = (baseOffer) + (baseOffer * durations / lastDate);
                price = ((((double) (basePrice * durations) / lastDate) + basePrice) * 1.1);
            } else {
                offer = (baseOffer * durations / lastDate);
                price = (((double) (basePrice * durations) / lastDate) * 1.1);
            }
        } else {
            endDateByCal.add(Calendar.MONTH, startDateByCal.get(Calendar.MONTH) + productType.getMonth() - 1);
        }

        endDateByCal.set(Calendar.DAY_OF_MONTH, endDateByCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = sdf.format(endDateByCal.getTime());
        System.out.println("마지막endDateByCal.getTime() : " + sdf.format(endDateByCal.getTime()));

        if (offer != clientOffer || (int) Math.floor(price) != clientPrice || !endDate.equals(clientEndDate)) {
            throw new ValueException(offer, clientOffer, (int) Math.floor(price), clientPrice, endDate, clientEndDate, agencyId, siteId);
        }
    }


    private String decrypt(String encryptedData) {
        String AES_CBC_256_KEY = "tmT6HUMU+3FW/RR5fxU05PbaZCrJkZ1wP/k6pfZnSj8=";
        String AES_CBC_256_IV = "/SwvI/9aT7RiMmfm8CfP4g==";

        byte[] key = Base64.getDecoder().decode(AES_CBC_256_KEY);
        byte[] iv = Base64.getDecoder().decode(AES_CBC_256_IV);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] plainBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(plainBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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


    public HashMap<String, String> convertToMap(PaymentDataModel paymentDataModel) {
        HashMap<String, String> map = new HashMap<>();
        map.put("trdAmt", paymentDataModel.getPlainTrdAmt());
        map.put("mchtCustId", paymentDataModel.getPlainMchtCustId());
        map.put("cphoneNo", paymentDataModel.getPlainCphoneNo());
        map.put("email", paymentDataModel.getPlainEmail());
        map.put("mchtCustNm", paymentDataModel.getPlainMchtCustNm());
        map.put("taxAmt", paymentDataModel.getPlainTaxAmt());
        map.put("vatAmt", paymentDataModel.getPlainTrdAmt());
        map.put("taxFreeAmt", paymentDataModel.getPlainTaxFreeAmt());
        map.put("svcAmt", paymentDataModel.getPlainSvcAmt());
        map.put("clipCustNm", paymentDataModel.getPlainClipCustNm());
        map.put("clipCustCi", paymentDataModel.getPlainClipCustCi());
        map.put("clipCustPhoneNo", paymentDataModel.getPlainClipCustPhoneNo());
        map.put("mchtParam", paymentDataModel.getMchtParam());
        return map;
    }


}
