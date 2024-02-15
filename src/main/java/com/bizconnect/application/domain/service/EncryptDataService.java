package com.bizconnect.application.domain.service;

import com.bizconnect.application.domain.model.AgencyInfoKey;
import com.bizconnect.application.port.in.EncryptUseCase;
import com.bizconnect.application.port.out.load.LoadEncryptDataPort;
import com.dsmdb.japi.MagicDBAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EncryptDataService implements EncryptUseCase {
    private final LoadEncryptDataPort loadEncryptDataPort;

    public EncryptDataService(LoadEncryptDataPort loadEncryptDataPort) {
        this.loadEncryptDataPort = loadEncryptDataPort;
    }

    /**
     * AES 암호화를 사용하여 데이터를 복호화합니다.
     *
     * @param targetDecode 복호화할 대상 데이터 (Base64 인코딩 되어 있음)
     * @return 복호화된 데이터 바이트 배열
     */
    @Override
    public byte[] decryptData(String agencyId, String targetDecode) throws GeneralSecurityException {
        Map<String,String> keyIv = dbDecrypt(agencyId);
        byte[] key = Base64.getDecoder().decode(keyIv.get("agencyKey"));
        byte[] iv = Base64.getDecoder().decode(keyIv.get("agencyIv"));
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        return cipher.doFinal(Base64.getDecoder().decode(targetDecode));
    }

    /**
     * AES 암호화를 사용하여 데이터를 암호화합니다.
     *
     * @param targetEncode 암호화 대상 데이터
     * @return 암호화된 데이터 문자열
     */
    @Override
    public String encryptData(String agencyId, String targetEncode) throws GeneralSecurityException {
        Map<String,String> keyIv = dbDecrypt(agencyId);
        byte[] key = Base64.getDecoder().decode(keyIv.get("agencyKey"));
        byte[] iv = Base64.getDecoder().decode(keyIv.get("agencyIv"));
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] cipherBytes = cipher.doFinal(targetEncode.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherBytes);
    }

    /**
     * MAP -> JSON string
     * libs 필요
     *
     * @param map JSON으로 만들 MAP 데이터
     * @return JSON string
     */
    @Override
    public String mapToJSONString(Map<String, String> map) {
        try {
            /* libs  필요 */
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            System.out.println("mapToJSONString 실패");
            return null;
        }
    }

    /**
     * HmacSHA256를 사용해 문자열을 해싱합니다.
     *
     * @param target        해싱 대상 문자열
     * @param hmacKeyString Hmac 키 문자열
     * @return HmacSHA256을 사용해 해싱된 문자열을 반환합니다. 예외 발생 시 null을 반환합니다.
     */
    @Override
    public String hmacSHA256(String target, String hmacKeyString) {
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

    public Map<String, String> dbDecrypt(String agencyId){
        Optional<AgencyInfoKey> agencyInfoKey = loadEncryptDataPort.getAgencyInfoKey(agencyId);
        String AES_CBC_256_KEY = "";
        String AES_CBC_256_IV = "";
        if (agencyInfoKey.isPresent()) {
            AgencyInfoKey infoKey = agencyInfoKey.get();
            AES_CBC_256_KEY = MagicDBAPI.decrypt("mokDBEnc", infoKey.getAgencyKey().trim());
            AES_CBC_256_IV = MagicDBAPI.decrypt("mokDBEnc", infoKey.getAgencyIv().trim());
        }
        Map<String, String> result = new HashMap<>();
        result.put("agencyKey", AES_CBC_256_KEY);
        result.put("agencyIv", AES_CBC_256_IV);
        return result;
    }
}
