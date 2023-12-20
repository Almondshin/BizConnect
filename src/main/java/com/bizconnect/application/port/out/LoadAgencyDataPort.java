package com.bizconnect.application.port.out;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;

import java.util.Optional;

public interface LoadAgencyDataPort {
    void checkAgency(Agency agency);
    Optional<ClientDataModel> getAgencyInfo(Agency agency, Client client, SettleManager settleManager);
}

