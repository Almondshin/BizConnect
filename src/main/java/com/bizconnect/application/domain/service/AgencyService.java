package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.domain.model.*;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.exceptions.NullAgencyIdSiteIdException;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.bizconnect.application.port.out.load.LoadAgencyDataPort;
import com.bizconnect.application.port.out.load.LoadAgencyProductDataPort;
import com.bizconnect.application.port.out.load.LoadEncryptDataPort;
import com.bizconnect.application.port.out.save.SaveAgencyDataPort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AgencyService implements AgencyUseCase {
    private final LoadAgencyDataPort loadAgencyDataPort;
    private final SaveAgencyDataPort saveAgencyDataPort;
    private final LoadEncryptDataPort loadEncryptDataPort;
    private final LoadAgencyProductDataPort loadAgencyProductDataPort;

    public AgencyService(LoadAgencyDataPort loadAgencyDataPort, SaveAgencyDataPort saveAgencyDataPort, LoadEncryptDataPort loadEncryptDataPort, LoadAgencyProductDataPort loadAgencyProductDataPort) {
        this.loadAgencyDataPort = loadAgencyDataPort;
        this.saveAgencyDataPort = saveAgencyDataPort;
        this.loadEncryptDataPort = loadEncryptDataPort;
        this.loadAgencyProductDataPort = loadAgencyProductDataPort;
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
        Optional<AgencyInfoKey> optAgencyInfoKey = loadEncryptDataPort.getAgencyInfoKey(agencyId);
        List<Map<String, String>> productsList = new ArrayList<>();

        if (optAgencyInfoKey.isPresent()) {
            AgencyInfoKey agencyInfoKey = optAgencyInfoKey.get();
            String[] productTypes = agencyInfoKey.getAgencyProductType().split(",");
            System.out.println(Arrays.toString(productTypes));
            for (String productType : productTypes) {
                Map<String, String> enumData = new HashMap<>();
                if (loadAgencyProductDataPort.getAgencyProductList(productType).isPresent()) {
                    AgencyProducts agencyProducts = loadAgencyProductDataPort.getAgencyProductList(productType).get();
                    enumData.put("type", agencyProducts.getRateSel());
                    enumData.put("name", agencyProducts.getName());
                    enumData.put("price", agencyProducts.getPrice());
                    enumData.put("basicOffer", agencyProducts.getOffer());
                    enumData.put("month", agencyProducts.getMonth());
                    enumData.put("feePerCase", agencyProducts.getFeePerCase());
                    enumData.put("excessFeePerCase", agencyProducts.getExcessPerCase());
                    productsList.add(enumData);
                }
            }
        }
        return productsList;
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
                clientDataModel.getEndDate(),
                clientDataModel.getServiceUseAgree(),
                clientDataModel.getPrivateColAgree()
        );
    }

    private SettleManager convertToSettleManager(ClientDataModel clientDataModel) {
        return new SettleManager(
                clientDataModel.getSettleManagerName(),
                clientDataModel.getSettleManagerPhoneNumber(),
                clientDataModel.getSettleManagerTelNumber(),
                clientDataModel.getSettleManagerEmail()
        );
    }

}
