package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.domain.enums.EnumProductType;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.bizconnect.application.port.out.AgencyDataPort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AgencyService implements AgencyUseCase {
    private final AgencyDataPort agencyDataPort;

    public AgencyService(AgencyDataPort agencyDataPort) {
        this.agencyDataPort = agencyDataPort;
    }

    @Override
    public void registerAgency(ClientDataModel clientDataModel) {
        agencyDataPort.checkAgency(convertToAgency(clientDataModel));
        agencyDataPort.registerAgency(convertToAgency(clientDataModel), convertToClient(clientDataModel), convertToSettleManager(clientDataModel));
    }

    @Override
    public void checkAgencyId(ClientDataModel clientDataModel) {
        agencyDataPort.checkAgency(convertToAgency(clientDataModel));
    }

    @Override
    public Optional<ClientDataModel> getAgencyInfo(ClientDataModel clientDataModel) {
        Agency agency = convertToAgency(clientDataModel);
        Client client = convertToClient(clientDataModel);
        SettleManager settleManager = convertToSettleManager(clientDataModel);
        return agencyDataPort.getAgencyInfo(agency,client,settleManager);
    }

    @Override
    public List<Map<String, String>> getEnumValues() {
        EnumProductType[] enumProductTypes = EnumProductType.values();
        List<Map<String, String>> enumValues = new ArrayList<>();

        for (EnumProductType enumProductType : enumProductTypes) {
            Map<String, String> enumData = new HashMap<>();
            enumData.put("type", enumProductType.getType());
            enumData.put("name", enumProductType.getName());
            enumData.put("price", String.valueOf(enumProductType.getPrice()));
            enumData.put("basicOffer", String.valueOf(enumProductType.getBasicOffer()));
            enumValues.add(enumData);
        }
        return enumValues;
    }

    private Agency convertToAgency(ClientDataModel clientDataModel) {
        return new Agency(clientDataModel.getAgencyId(), clientDataModel.getMallId());
    }

    private Client convertToClient(ClientDataModel clientDataModel) {
        return new Client(
                clientDataModel.getClientId(),
                clientDataModel.getCompanyName(),
                clientDataModel.getBusinessType(),
                clientDataModel.getBizNumber(),
                clientDataModel.getCeoName(),
                clientDataModel.getPhoneNumber(),
                clientDataModel.getAddress(),
                clientDataModel.getCompanySite(),
                clientDataModel.getEmail()
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
