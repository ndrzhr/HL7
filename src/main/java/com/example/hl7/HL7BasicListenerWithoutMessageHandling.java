package com.example.hl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.parser.Parser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("listen")
public class HL7BasicListenerWithoutMessageHandling {
    private static final int PORT_NUMBER = 61386;
    private static HapiContext context = new DefaultHapiContext();

    @GetMapping()
    private static void main() {
        try{
            boolean useSecureConnection = false; // are you using TLS/SSL ?

            Connection connection = context.newLazyClient("localhost", PORT_NUMBER, useSecureConnection);
            Initiator initiator = connection.getInitiator();

            HL7Service hl7Server = context.newServer(PORT_NUMBER, useSecureConnection);
            hl7Server.startAndWait();

            ADT_A01 adtMessage = (ADT_A01) AdtMessageFactory.createMessage("A01");

            Parser pipeParser = context.getPipeParser();
            Message messageResponse = initiator.sendAndReceive(adtMessage);
            String responseString = pipeParser.encode(messageResponse);
            System.out.println("Received a message response:\n" + responseString);

            connection.close();
            hl7Server.startAndWait();
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (LLPException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
