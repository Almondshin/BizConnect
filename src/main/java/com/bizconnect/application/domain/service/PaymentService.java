package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.bizconnect.application.domain.model.PaymentHistory;
import com.bizconnect.application.port.in.PaymentUseCase;
import com.bizconnect.application.port.out.SavePaymentDataPort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService implements PaymentUseCase {

    private final Constant constant;
    //    Logger logger = LoggerFactory.getLogger("HFInitController");
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

        System.out.println("aes256EncryptEcb hashPlain : " + hashPlain);
        String hashCipher = "";
        /** SHA256 해쉬 처리 */
        try {
            hashCipher = EncryptUtil.digestSHA256(hashPlain);//해쉬 값
        } catch (Exception e) {
//            logger.error("[" + paymentDataModel.getMchtTrdNo() + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
            System.out.println("[" + paymentDataModel.getMchtTrdNo() + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
        } finally {
//            logger.info("[" + paymentDataModel.getMchtTrdNo() + "][SHA256 HASHING] Plain Text[" + hashPlain + "] ---> Cipher Text[" + hashCipher + "]");
        }

        return hashCipher;
    }

    @Override
    public HashMap<String, String> encodeBase64(PaymentDataModel paymentDataModel) {
        String aesKey = constant.AES256_KEY;
        HashMap<String, String> params = convertToMap(paymentDataModel);
        System.out.println("encodeBase64 params : " + params);
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                String aesPlain = params.get(key);
                if (!("".equals(aesPlain))) {
                    byte[] aesCipherRaw = EncryptUtil.aes256EncryptEcb(aesKey, aesPlain);
                    String aesCipher = EncryptUtil.encodeBase64(aesCipherRaw);

                    params.put(key, aesCipher);//암호화된 데이터로 세팅
//                    logger.info("[" + paymentDataModel.getMchtTrdNo() + "][AES256 Encrypt] " + key + "[" + aesPlain + "] ---> [" + aesCipher + "]");
                    System.out.println("[" + paymentDataModel.getMchtTrdNo() + "][AES256 Encrypt] " + key + "[" + aesPlain + "] ---> [" + aesCipher + "]");
                }
            }
        } catch (Exception e) {
//            logger.error("[" + paymentDataModel.getMchtTrdNo() + "][AES256 Encrypt] AES256 Fail! : " + e.toString());
            System.out.println("[" + paymentDataModel.getMchtTrdNo() + "][AES256 Encrypt] AES256 Fail! : " + e.toString());
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
                    }
                }
            }


        System.out.println("resultMap service : " + resultMap);

        switch (resultMap.get("method")) {
            case "card": {
                PaymentHistory paymentHistory = new PaymentHistory(
                        resultMap.get("mchtTrdNo"),     //상점에서 생성한 TradeNum
                        resultMap.get("trdNo"),         //헥토파이낸셜 TradeNum
                        agencyId,
                        siteId,
                        resultMap.get("method"),        //결제수단
                        resultMap.get("trdAmt"),        //결제금액
                        resultMap.get("authDt"),         //거래일
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
                        resultMap.get("method"),      //결제수단
                        resultMap.get("trdAmt"),        //결제금액
                        resultMap.get("authDt"),         //거래일
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
