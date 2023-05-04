package com.example.hl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;

import java.io.IOException;
import java.util.Map;

public class OurSimpleApplication implements ReceivingApplication {

    private static HapiContext context = new DefaultHapiContext();

    @Override
    public Message processMessage(Message message, Map map) throws ReceivingApplicationException, HL7Exception {

        String receivedEncodedMessage = context.getPipeParser().encode(message);
        System.out.println("Incoming message:\n"+receivedEncodedMessage+"\n\n");

        try {
            return message.generateACK();
        } catch (IOException e) {

//        //intentionally raise an exception here to see what the default message acknowledgement looks like
            throw new RuntimeException("Some Error Thrown Here. This will be returned in the ERR segment of the message response");
        }

    }

    @Override
    public boolean canProcess(Message message) {
        return true;
    }
}
