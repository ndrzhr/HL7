package com.example.hl7;
import java.net.Socket;
import ca.uhn.hl7v2.app.ActiveConnection;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.TwoPortService;
import ca.uhn.hl7v2.concurrent.DefaultExecutorService;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ls")
public class BasicListenerWithTwoPortBehavior {

    @GetMapping
    public  void ll() throws Exception {

        try {

            TwoPortService ourTwoPortListeningService;
            int outboundPort = 4567;
            int inboundPort = 5678;

            //instantiate a two port service that listens on these two ports
            ourTwoPortListeningService = new TwoPortService(outboundPort, inboundPort);

            //start the service and listen for ADT A01 messages only
            ourTwoPortListeningService.registerApplication("ADT", "A01", new OurSimpleApplication());
//            ourTwoPortListeningService.start();
            ourTwoPortListeningService.startAndWait();

            Parser ourPipeParser = new PipeParser();
            MinLowerLayerProtocol protocol = new MinLowerLayerProtocol();

            Socket outboundSocket = new Socket("localhost", outboundPort);
            Socket inboundSocket = new Socket("localhost", inboundPort);

            //configure a connection that is capable of sending and receiving messages through the two ports
            Connection conn = new ActiveConnection(ourPipeParser, protocol, inboundSocket, outboundSocket);
            conn.activate();

            ADT_A01 adtMessage = (ADT_A01) AdtMessageFactory.createMessage("A01");

            System.out.println("Sending message to listener through port:" + outboundPort + "\n");
            Message messageResponse = conn.getInitiator().sendAndReceive(adtMessage);
            conn.close();

            String responseString = ourPipeParser.encode(messageResponse);
            System.out.println("Received a message response through port:" + inboundPort + "\n");
            System.out.println("Message response was:\n" + responseString);

            //stop the service and the thread executor associated with it
            ourTwoPortListeningService.stopAndWait();
            DefaultExecutorService.getDefaultService().shutdown();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}

