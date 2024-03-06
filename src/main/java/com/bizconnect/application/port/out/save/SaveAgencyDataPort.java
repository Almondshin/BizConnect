package com.bizconnect.application.port.out.save;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;

import java.util.Date;

public interface SaveAgencyDataPort {
    void registerAgency(Agency agency, Client client, SettleManager settleManager);
    void updateAgency(Agency agency, Client client, String paymentStatus);
}
