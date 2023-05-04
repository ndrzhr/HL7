package com.example.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;

import java.io.IOException;

public class AdtMessageFactory {
    public static Object createMessage(String type) throws HL7Exception, IOException {
        switch (type) {
            case "A01":
                return createAdtA01Message();
            case "A02":
                return null; //todo create a builder.
        }
        return null;
    }

    private static ADT_A01 createAdtA01Message() throws HL7Exception, IOException {
        return  new AdtA01MessageBuilder().build();
    }
}
