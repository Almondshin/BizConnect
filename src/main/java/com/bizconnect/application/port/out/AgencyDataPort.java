package com.bizconnect.application.port.out;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;

public interface AgencyDataPort {
    void checkAgency(Agency agency);
    void registerAgency(Agency agency, Client client, SettleManager settleManager);
}

