package com.example.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.datatype.XAD;
import ca.uhn.hl7v2.model.v24.datatype.XPN;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdtA01MessageBuilder {
    ADT_A01 _adtA01Message;
    public ADT_A01 build() throws HL7Exception, IOException {


        _adtA01Message = new ADT_A01();
        _adtA01Message.initQuickstart("ADT","A01","P");
        createMshSegment(getCurrentTimeStamp());
        createPidSegment();
        return _adtA01Message;
    }

    private void createMshSegment(String currentDateTimeString) throws DataTypeException {
        //Populate the MSH Segment
        MSH mshSegment = _adtA01Message.getMSH();
        mshSegment.getFieldSeparator().setValue("|");
        mshSegment.getEncodingCharacters().setValue("^~\\&");
        mshSegment.getSendingApplication().getNamespaceID().setValue("AlmaSolutionHL7");
        mshSegment.getSendingFacility().getNamespaceID().setValue("facility");
        mshSegment.getReceivingApplication().getNamespaceID().setValue("TheirRemoteSystem");
        mshSegment.getReceivingFacility().getNamespaceID().setValue("TheirRemoteFacility");
        mshSegment.getDateTimeOfMessage().getTimeOfAnEvent().setValue(currentDateTimeString);
        mshSegment.getMessageControlID().setValue(getSequenceNumber());
        mshSegment.getVersionID().getVersionID().setValue("2.4");
    }



    private void createPidSegment() throws DataTypeException {
        PID pid = _adtA01Message.getPID();
        XPN patientName = pid.getPatientName(0);
        patientName.getFamilyName().getSurname().setValue("Mouse");
        patientName.getGivenName().setValue("Mickey");
        pid.getPatientIdentifierList(0).getID().setValue("647395721743");
        XAD patientAddress = pid.getPatientAddress(0);
        patientAddress.getStreetAddress().getStreetOrMailingAddress().setValue("123MainStreet");
        patientAddress.getCity().setValue("LakeBuenaVista");
        patientAddress.getStateOrProvince().setValue("FL");
        patientAddress.getCountry().setValue("USA");
    }



    private String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    private String getSequenceNumber() {
        String facilityNumberPrefix = "1234"; // some arbitrary prefix for the facility
        return facilityNumberPrefix.concat(getCurrentTimeStamp());
    }
}
