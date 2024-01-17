package com.bizconnect.application.port.in;

import java.security.GeneralSecurityException;
import java.util.Map;

public interface EncryptUseCase {
    byte[] decryptData(String targetDecode) throws GeneralSecurityException;
    String encryptData(String targetEncode) throws GeneralSecurityException;
    String mapToJSONString(Map<String, String> map);
    String hmacSHA256(String target, String hmacKeyString);
}
