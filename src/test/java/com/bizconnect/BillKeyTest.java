package com.bizconnect;

import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.bizconnect.application.domain.service.AgencyService;
import com.bizconnect.application.port.in.PaymentUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class BillKeyTest {

    private final Constant constant;
    Logger logger = LoggerFactory.getLogger("HFInitController");

    public BillKeyTest(Constant constant) {
        this.constant = constant;
    }

    @Test
    public void notifyPaymentSiteInfoTest() throws GeneralSecurityException {

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mchtId", "nxca_jt_bi");
        paramsMap.put("ver", "0A19");
        paramsMap.put("method", "CA");
        paramsMap.put("bizType", "B0");
        paramsMap.put("encCd", "23");
        paramsMap.put("mchtTrdNo", "PAYMENT202401221500035856");
        paramsMap.put("trdDt", "20240122");
        paramsMap.put("trdTm", "150000");
        paramsMap.put("mobileYn", "N");
        paramsMap.put("osType", "W");

        // 'data' 맵 생성
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("pktHash", aes256EncryptEcb(new PaymentDataModel("nxca_jt_bi","CA","PAYMENT202401221500035856", "20240122","150000","")));
        dataMap.put("pmtprdNm", "테스트상품");
        dataMap.put("mchtCustNm", "홍길동");
        dataMap.put("mchtCustId", "HongGilDong");
        dataMap.put("billKey", "SBILL_PGCAnxca_jt_gu20241286510122150046");
        dataMap.put("instmtMon", "00");
        dataMap.put("crcCd", "KRW");
        dataMap.put("trdAmt", "10000");

        // 'billKeyInfoMap'에 'params' 와 'data' 맵 삽입
        Map<String, Map<String, String>> billKeyInfoMap = new HashMap<>();
        billKeyInfoMap.put("params", paramsMap);
        billKeyInfoMap.put("data", dataMap);

        send("https://tbgw.settlebank.co.kr/spay/APIService.do",billKeyInfoMap);
    }

    public static String mapToJSONString(Map<String, String> map) {
        try {
            /* libs  필요 */
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            System.out.println("mapToJSONString 실패");
            return null;
        }
    }

    private static String encryptData(String targetEncode) throws GeneralSecurityException {
        String AES_CBC_256_KEY = "tmT6HUMU+3FW/RR5fxU05PbaZCrJkZ1wP/k6pfZnSj8=";
        String AES_CBC_256_IV = "/SwvI/9aT7RiMmfm8CfP4g==";

        byte[] key = Base64.getDecoder().decode(AES_CBC_256_KEY);
        byte[] iv = Base64.getDecoder().decode(AES_CBC_256_IV);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] cipherBytes = cipher.doFinal(targetEncode.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherBytes);
    }

    public static String hmacSHA256(String target, String hmacKeyString) {
        try {
            byte[] hmacKey = hmacKeyString.getBytes(StandardCharsets.UTF_8);

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(hmacKey, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hashed = sha256_HMAC.doFinal(target.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String send(String targetUrl, String data) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(data.getBytes(StandardCharsets.UTF_8));


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


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
        } finally {
            logger.info("[" + paymentDataModel.getMchtTrdNo() + "][SHA256 HASHING] Plain Text[" + hashPlain + "] ---> Cipher Text[" + hashCipher + "]");
        }

        return hashCipher;
    }


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

    public static String send(String targetUrl, Map<String, Map<String, String>> dataMap) {
        JSONObject json = JSONObject.fromObject(dataMap);
        String data = json.toString();

        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(data.getBytes(StandardCharsets.UTF_8));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}