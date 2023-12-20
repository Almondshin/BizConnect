package com.bizconnect.application.port.out;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;

public interface SaveAgencyDataPort {
    void registerAgency(Agency agency, Client client, SettleManager settleManager);
}
