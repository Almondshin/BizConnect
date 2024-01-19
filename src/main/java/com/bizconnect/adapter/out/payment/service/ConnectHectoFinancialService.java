package com.bizconnect.adapter.out.payment.service;

import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.bizconnect.adapter.out.payment.utils.HttpClientUtil;
import com.bizconnect.adapter.out.payment.utils.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConnectHectoFinancialService {

    @Autowired
    Constant constant;
    Logger logger = LoggerFactory.getLogger("ConnectHectoFinancialService");

    public void hashPkt(Map<String, String> REQ_HEADER, Map<String, String> REQ_BODY) {
        String hashPlain = "";
        String hashCipher = "";
        try {
            hashPlain = REQ_HEADER.get("trdDt") + REQ_HEADER.get("trdTm") + REQ_HEADER.get("mchtId") + REQ_HEADER.get("mchtTrdNo") + REQ_BODY.get("cnclAmt") + constant.LICENSE_KEY;
            hashCipher = EncryptUtil.digestSHA256(hashPlain);
        }
        catch (Exception e) {
            logger.error("[" + REQ_HEADER.get("mchtTrdNo") + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
        }
        finally {
            logger.info("[" + REQ_HEADER.get("mchtTrdNo") + "][SHA256 HASHING] Plain Text[" + hashPlain + "] ---> Cipher Text[" + hashCipher + "]");
            REQ_BODY.put("pktHash", hashCipher); //해쉬 결과 값 세팅
        }
    }

    /**
     * API호출(가맹점->세틀) 및 응답 처리
     */
    public Map<String, String> requestCancelAPI(Map<String, String> REQ_HEADER, Map<String, String> REQ_BODY, Map<String, String> RES_HEADER, Map<String, String> RES_BODY) {

        //params, data 이름은 세틀로 전달되야 하는 값이니 변경하지 마십시오.
        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put("params", REQ_HEADER);
        reqParam.put("data", REQ_BODY);
        String requestUrl = "";

        if (REQ_HEADER.get("method").equals("CA"))
            requestUrl = constant.SERVER_URL + "/spay/APICancel.do";
        if (REQ_HEADER.get("method").equals("VA"))
            requestUrl = constant.SERVER_URL + "/spay/APIRefund.do";

        Map<String, String> respParam = new HashMap<>();
        try {
            HttpClientUtil httpClientUtil = new HttpClientUtil();
            //send_api ( API호출 URL, 전송될데이터, 연결 타임아웃, 수신 타임아웃 )
            String resData = httpClientUtil.sendApi(requestUrl, reqParam, constant.CONN_TIMEOUT, constant.READ_TIMEOUT);
            //응답 파라미터 파싱
            JSONObject resp = JSONObject.fromObject(resData);
            JSONObject respHeader = resp.has("params") ? resp.getJSONObject("params") : null;
            JSONObject respBody = resp.has("data") ? resp.getJSONObject("data") : null;
            //응답 파라미터 세팅(헤더)
            if (respHeader != null) {
                for (String key : RES_HEADER.keySet()) {
                    respParam.put(key, StringUtil.isNull(respHeader.has(key) ? respHeader.getString(key) : ""));
                }
            }
            else {
                for (String key : RES_HEADER.keySet()) {
                    respParam.put(key, "");
                }
            }
            //응답 파라미터 세팅(바디)
            if (respBody != null) {
                for (String key : RES_BODY.keySet()) {
                    respParam.put(key, StringUtil.isNull(respBody.has(key) ? respBody.getString(key) : ""));
                }
            }
            else {
                for (String key : RES_BODY.keySet()) {
                    respParam.put(key, "");
                }
            }
        }
        catch (Exception e) {
            respParam.put("outStatCd", "0031");
            respParam.put("outRsltCd", "9999");
            respParam.put("outRsltMsg", "[Response Parsing Error]" + e.toString());
            logger.error("[" + REQ_HEADER.get("mchtTrdNo") + "][Response Parsing Error]" + e.toString());
//            System.out.println(("[" + REQ_HEADER.get("mchtTrdNo") + "][Response Parsing Error]" + e.toString()));
        }

        return respParam;
    }
}
