package com.example.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

import java.io.IOException;

public class OruMessageFactory {
    public static Message createMessage() throws HL7Exception, IOException {
        return new OruR01MessageBuilder().build();
    }
}
