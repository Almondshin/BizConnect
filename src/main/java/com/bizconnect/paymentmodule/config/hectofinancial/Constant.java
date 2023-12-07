package com.bizconnect.paymentmodule.config.hectofinancial;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "hectofinancial")
public class Constant {
    public String PG_MID;
    public String PG_CANCEL_MID;
    public String LICENSE_KEY;
    public String AES256_KEY;
    public String SERVER_URL;
    public int CONN_TIMEOUT;
    public int READ_TIMEOUT;
}
