package com.example.hl7;


import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.parser.Parser;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/create")
public class CreateHL7Message {

    @GetMapping()
    public ResponseEntity createMessage() throws HL7Exception, IOException {

        ADT_A01 adt = new ADT_A01();
        adt.initQuickstart("ADT","A01","P");

        //Populate the MSH Segment
        MSH mshSegment = adt.getMSH();
        mshSegment.getSendingApplication().getNamespaceID().setValue("TestSendingSystem");
        mshSegment.getSequenceNumber().setValue("123");

        //Populate the PID segment
        PID pid = adt.getPID();
        pid.getPatientName(0).getFamilyName().getSurname().setValue("Doe");
        pid.getPatientName(0).getGivenName().setValue("John");
        pid.getPatientIdentifierList(0).getID().setValue("123456");

        /*
         * In a real situation, of course, many more segments and fields would be populated
         */

        // Now, encode the message and look at the output
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        String encodedMessage = parser.encode(adt);
        System.out.println("Printing HL7 encoded message: ");
        System.out.println(encodedMessage);


        parser = context.getXMLParser();
        encodedMessage = parser.encode(adt);
        System.out.println("printing XML Encoded message");
        System.out.println(encodedMessage);

        return null;
    }

}
