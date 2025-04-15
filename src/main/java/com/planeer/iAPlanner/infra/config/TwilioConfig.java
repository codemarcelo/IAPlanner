package com.planeer.iAPlanner.infra.config;

import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Bean
    public TwilioRestClient twilioRestClient() {
        Twilio.init(accountSid, authToken);
        return new TwilioRestClient.Builder(accountSid, authToken).build();
    }

    public String getTwilioPhoneNumber() {
        return twilioPhoneNumber;
    }
}
