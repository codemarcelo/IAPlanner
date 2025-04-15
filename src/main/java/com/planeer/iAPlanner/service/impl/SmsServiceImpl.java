package com.planeer.iAPlanner.service.impl;

import com.planeer.iAPlanner.infra.config.TwilioConfig;
import com.planeer.iAPlanner.service.SmsService;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;


@Service
public class SmsServiceImpl implements SmsService {

    private final TwilioRestClient twilioRestClient;
    private final TwilioConfig twilioConfig;

    public SmsServiceImpl(TwilioRestClient twilioRestClient, TwilioConfig twilioConfig) {
        this.twilioRestClient = twilioRestClient;
        this.twilioConfig = twilioConfig;
    }

    public void sendSms(String to, String message) {

        if (!to.startsWith("+")) {
            to = "+55" + to;
        }
        Message.creator(
                new PhoneNumber(to), // Converte o número de destino
                new PhoneNumber(twilioConfig.getTwilioPhoneNumber()), // Converte o número Twilio
                message // A mensagem que será enviada
        ).create();
    }
}