package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.domain.enums.EnumAgency;
import com.bizconnect.application.domain.enums.EnumProductType;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.exceptions.NullAgencyIdSiteIdException;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.bizconnect.application.port.out.LoadAgencyDataPort;
import com.bizconnect.application.port.out.LoadPaymentDataPort;
import com.bizconnect.application.port.out.SaveAgencyDataPort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AgencyService implements AgencyUseCase {
    private final LoadAgencyDataPort loadAgencyDataPort;
    private final SaveAgencyDataPort saveAgencyDataPort;
    private final LoadPaymentDataPort loadPaymentDataPort;

    public AgencyService(LoadAgencyDataPort loadAgencyDataPort, SaveAgencyDataPort saveAgencyDataPort, LoadPaymentDataPort loadPaymentDataPort) {
        this.loadAgencyDataPort = loadAgencyDataPort;
        this.saveAgencyDataPort = saveAgencyDataPort;
        this.loadPaymentDataPort = loadPaymentDataPort;
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
        System.out.println(clientDataModel);
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
