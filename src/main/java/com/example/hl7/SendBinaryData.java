package com.example.hl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class SendBinaryData {
    private static int PORT_NUMBER = 8081;//change this to whatever your port number is

    private static HapiContext context = new DefaultHapiContext();

    @GetMapping
    public void createMessage() {
        try{
            // create the HL7 message
            Message oruMessage = OruMessageFactory.createMessage();

            // create a new MLLP client over the specified port
            Connection connection = context.newClient("localhost", PORT_NUMBER, false);
            // the initiator which will be used to transmit our message
            Initiator initiator = connection.getInitiator();
            // send the previously created HL7 message over the connection established
            Parser parser = context.getPipeParser();
//            System.out.println("Sending Message:\n" + parser.encode(oruMessage));
            System.out.println("Sending Message:\n" + oruMessage);
            Message response = initiator.sendAndReceive(oruMessage);

            // display the message response received from remote party
            String responseString = parser.encode(response);
            System.out.println("Received response:\n"+responseString);

        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LLPException e) {
            throw new RuntimeException(e);
        }
    }
}
