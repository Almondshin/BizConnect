package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.adapter.out.payment.config.hectofinancial.Constant;
import com.bizconnect.adapter.out.payment.utils.EncryptUtil;
import com.bizconnect.application.domain.enums.EnumAgency;
import com.bizconnect.application.domain.enums.EnumProductType;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.exceptions.NullAgencyIdSiteIdException;
import com.bizconnect.application.exceptions.exceptions.ValueException;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.bizconnect.application.port.out.LoadAgencyDataPort;
import com.bizconnect.application.port.out.LoadPaymentDataPort;
import com.bizconnect.application.port.out.SaveAgencyDataPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AgencyService implements AgencyUseCase {
    private final LoadAgencyDataPort loadAgencyDataPort;
    private final SaveAgencyDataPort saveAgencyDataPort;
    private final LoadPaymentDataPort loadPaymentDataPort;

    private final Constant constant;
    Logger logger = LoggerFactory.getLogger("HFInitController");

    public AgencyService(LoadAgencyDataPort loadAgencyDataPort, SaveAgencyDataPort saveAgencyDataPort, LoadPaymentDataPort loadPaymentDataPort, Constant constant) {
        this.loadAgencyDataPort = loadAgencyDataPort;
        this.saveAgencyDataPort = saveAgencyDataPort;
        this.loadPaymentDataPort = loadPaymentDataPort;
        this.constant = constant;
    }

    @Override
    public void registerAgency(ClientDataModel clientDataModel) {
        if (clientDataModel.getAgencyId() == null || clientDataModel.getAgencyId().isEmpty() || clientDataModel.getSiteId() == null || clientDataModel.getSiteId().isEmpty()) {
            throw new NullAgencyIdSiteIdException(EnumResultCode.NullPointArgument, null);
        }
        ClientDataModel checkAgencyId = new ClientDataModel(clientDataModel.getAgencyId(), clientDataModel.getSiteId());
        saveAgencyDataPort.registerAgency(convertToAgency(checkAgencyId), convertToClient(clientDataModel), convertToSettleManager(clientDataModel));
    }

    @Override
    public Optional<ClientDataModel> getAgencyInfo(ClientDataModel clientDataModel) {
        if (clientDataModel.getAgencyId() == null || clientDataModel.getAgencyId().isEmpty() || clientDataModel.getSiteId() == null || clientDataModel.getSiteId().isEmpty()) {
            throw new NullAgencyIdSiteIdException(EnumResultCode.NullPointArgument, null);
        }
        return loadAgencyDataPort.getAgencyInfo(convertToAgency(clientDataModel), convertToClient(clientDataModel));
    }

    @Override
    public List<Map<String, String>> getProductTypes(String agencyId) {
        EnumProductType[] enumProductTypes = EnumProductType.values();
        List<Map<String, String>> enumValues = new ArrayList<>();

        for (EnumProductType enumProductType : enumProductTypes) {
            Map<String, String> enumData = new HashMap<>();
            //제휴사가 스퀘어스인 경우, 1개월짜리 상품만 제공됨.
            if (agencyId.equals(EnumAgency.SQUARES.getCode())) {
                if (enumProductType.getMonth() == 1) {  // Check if the month is 1
                    enumData.put("type", enumProductType.getType());
                    enumData.put("name", enumProductType.getName());
                    enumData.put("price", String.valueOf(enumProductType.getPrice()));
                    enumData.put("basicOffer", String.valueOf(enumProductType.getBasicOffer()));
                    enumData.put("month", String.valueOf(enumProductType.getMonth()));
                    enumValues.add(enumData);
                }
            } else {
                enumData.put("type", enumProductType.getType());
                enumData.put("name", enumProductType.getName());
                enumData.put("price", String.valueOf(enumProductType.getPrice()));
                enumData.put("basicOffer", String.valueOf(enumProductType.getBasicOffer()));
                enumData.put("month", String.valueOf(enumProductType.getMonth()));
                enumValues.add(enumData);
            }
        }
        return enumValues;
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

        String[] pairs = paymentDataModel.getMchtParam().split("&");

        parseParams(new String[] {"agencyId","siteId","rateSel","startDate","endDate","offer"});

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
        if (offer != clientOffer || (int) Math.floor(price) != clientPrice || !endDate.equals(clientEndDate)) {
            throw new ValueException(offer, clientOffer, (int) Math.floor(price), clientPrice, endDate, clientEndDate, agencyId, siteId);
        }

        logger.info("S ------------------------------[AGENCY] - [setPaymentSiteInfo] ------------------------------ S");
        logger.info("[agencyId] : [" + agencyId + "]");
        logger.info("[siteId] : [" + siteId + "]");
        logger.info("[rateSel] : [" + productType.getType() + ", "+productType.getName() + "]");
        logger.info("[startDate] : [" + sdf.format(startDateByCal.getTime()) + "]");
        logger.info("[endDate] : [" + endDate + "]");
        logger.info("[offer] : [" + offer + "]");
        logger.info("[price] : [" + (int) Math.floor(price) + "]");

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



    private Agency convertToAgency(ClientDataModel clientDataModel) {
        return new Agency(clientDataModel.getAgencyId(), clientDataModel.getSiteId());
    }

    private Client convertToClient(ClientDataModel clientDataModel) {
        return new Client(
                clientDataModel.getSiteName(),
                clientDataModel.getCompanyName(),
                clientDataModel.getBusinessType(),
                clientDataModel.getBizNumber(),
                clientDataModel.getCeoName(),
                clientDataModel.getPhoneNumber(),
                clientDataModel.getAddress(),
                clientDataModel.getCompanySite(),
                clientDataModel.getEmail(),
                clientDataModel.getRateSel(),
                clientDataModel.getSiteStatus(),
                clientDataModel.getStartDate(),
                clientDataModel.getEndDate()
        );
    }

    private SettleManager convertToSettleManager(ClientDataModel clientDataModel) {
        return new SettleManager(
                clientDataModel.getSettleManagerName(),
                clientDataModel.getSettleManagerPhoneNumber(),
                clientDataModel.getSettleManagerEmail()
        );
    }

}
