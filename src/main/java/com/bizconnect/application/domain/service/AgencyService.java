package com.bizconnect.application.domain.service;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.bizconnect.application.port.out.AgencyDataPort;
import org.springframework.stereotype.Service;

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

    private Agency convertToAgency(ClientDataModel clientDataModel) {
        return new Agency(clientDataModel.getClientAgencyId(), clientDataModel.getClientMallId());
    }

    private Client convertToClient(ClientDataModel clientDataModel) {
        return new Client(
                clientDataModel.getClientIdentifier(),
                clientDataModel.getClientCompanyName(),
                clientDataModel.getClientBusinessType(),
                clientDataModel.getClientBizNumber(),
                clientDataModel.getClientCeoName(),
                clientDataModel.getClientPhoneNumber(),
                clientDataModel.getClientAddress(),
                clientDataModel.getClientCompanySite(),
                clientDataModel.getClientEmail()
        );
    }

    private SettleManager convertToSettleManager(ClientDataModel clientDataModel) {
        return new SettleManager(
                clientDataModel.getClientSettleManagerName(),
                clientDataModel.getClientSettleManagerPhoneNumber(),
                clientDataModel.getClientSettleManagerEmail()
        );
    }
}
