package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.application.port.in.PaymentUseCase;
import com.bizconnect.paymentmodule.config.hectofinancial.Constant;
import com.bizconnect.paymentmodule.utils.EncryptUtil;
import com.bizconnect.paymentmodule.utils.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService implements PaymentUseCase {
    Constant constant;
    Logger logger = LoggerFactory.getLogger("HFInitController");

    @Override
    public String aes256EncryptEcb(PaymentDataModel paymentDataModel) {
        String licenseKey = constant.LICENSE_KEY;
        String hashPlain = new PaymentDataModel(
                paymentDataModel.getMchtId(),
                paymentDataModel.getMethod(),
                paymentDataModel.getMchtTrdNo(),
                paymentDataModel.getTrdDt(),
                paymentDataModel.getTrdTm(),
                paymentDataModel.getTaxAmt()
        ) + licenseKey;
        String hashCipher = "";
        /** SHA256 해쉬 처리 */
        try {
            hashCipher = EncryptUtil.digestSHA256(hashPlain);//해쉬 값
        } catch (Exception e) {
            logger.error("[" + paymentDataModel.getMchtTrdNo() + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
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
                }
            }
        } catch (Exception e) {
            logger.error("[" + paymentDataModel.getMchtTrdNo() + "][AES256 Encrypt] AES256 Fail! : " + e.toString());
        }
        return params;
    }

    public HashMap<String, String> convertToMap(PaymentDataModel paymentDataModel) {
        HashMap<String, String> map = new HashMap<>();
        map.put("trdAmt", paymentDataModel.getTrdAmt());
        map.put("mchtCustNm", paymentDataModel.getMchtCustId());
        map.put("cphoneNo", paymentDataModel.getCphoneNo());
        map.put("email", paymentDataModel.getEmail());
        map.put("mchtCustId", paymentDataModel.getMchtCustNm());
        map.put("taxAmt", paymentDataModel.getTaxAmt());
        map.put("vatAmt", paymentDataModel.getTrdAmt());
        map.put("taxFreeAmt", paymentDataModel.getTaxFreeAmt());
        map.put("svcAmt", paymentDataModel.getSvcAmt());
        map.put("clipCustNm", paymentDataModel.getClipCustNm());
        map.put("clipCustCi", paymentDataModel.getClipCustCi());
        map.put("clipCustPhoneNo", paymentDataModel.getClipCustPhoneNo());
        return map;
    }


}
