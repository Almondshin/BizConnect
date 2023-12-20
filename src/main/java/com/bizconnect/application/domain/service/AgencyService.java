package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.enums.EnumResultCode;
import com.bizconnect.adapter.in.exceptions.IllegalAgencyIdMallIdException;
import com.bizconnect.adapter.in.exceptions.NullAgencyIdMallIdException;
import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.adapter.out.persistence.exceptions.DuplicateMemberException;
import com.bizconnect.application.domain.enums.EnumProductType;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.bizconnect.application.port.out.LoadAgencyDataPort;
import com.bizconnect.application.port.out.SaveAgencyDataPort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AgencyService implements AgencyUseCase {
    private final LoadAgencyDataPort loadAgencyDataPort;
    private final SaveAgencyDataPort saveAgencyDataPort;

    public AgencyService(LoadAgencyDataPort loadAgencyDataPort, SaveAgencyDataPort saveAgencyDataPort) {
        this.loadAgencyDataPort = loadAgencyDataPort;
        this.saveAgencyDataPort = saveAgencyDataPort;
    }

    @Override
    public void registerAgency(ClientDataModel clientDataModel) {
        if(clientDataModel.getAgencyId() == null || clientDataModel.getAgencyId().isEmpty() || clientDataModel.getMallId() == null || clientDataModel.getMallId().isEmpty()){
            throw new NullAgencyIdMallIdException(EnumResultCode.NullPointArgument, null);
        }
        saveAgencyDataPort.registerAgency(convertToAgency(clientDataModel), convertToClient(clientDataModel), convertToSettleManager(clientDataModel));
    }

    @Override
    public void checkAgencyId(ClientDataModel clientDataModel) {
        loadAgencyDataPort.checkAgency(convertToAgency(clientDataModel));
    }

    @Override
    public Optional<ClientDataModel> getAgencyInfo(ClientDataModel clientDataModel) {
        Agency agency = convertToAgency(clientDataModel);
        Client client = convertToClient(clientDataModel);
        SettleManager settleManager = convertToSettleManager(clientDataModel);
        return loadAgencyDataPort.getAgencyInfo(agency,client,settleManager);
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
            enumData.put("month", String.valueOf(enumProductType.getMonth()));
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
