package com.bizconnect.application.port.out.load;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;

import java.util.Optional;

public interface LoadAgencyDataPort {
    Optional<ClientDataModel> getAgencyInfo(Agency agency, Client client);
}

