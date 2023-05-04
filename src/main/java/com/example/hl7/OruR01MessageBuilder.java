package com.example.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.*;
import ca.uhn.hl7v2.model.v24.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v24.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v24.message.ORU_R01;
import ca.uhn.hl7v2.model.v24.segment.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OruR01MessageBuilder {
    private ORU_R01 _oruR01Message;
    private Base64Helper base64Helper = new Base64Helper();

    public ORU_R01 build() throws HL7Exception, IOException {
        String currentDateTimeString = getCurrentTimeStamp();
        _oruR01Message = new ORU_R01();
        // use the context class's newMessage methode to instantiate a message if you want
        _oruR01Message.initQuickstart("ORU", "R01", "P");
        createMshSegment(currentDateTimeString);
        createPidSegment();
        createPv1Segment();
        createObrSegment();
        createObxSegment();
        return _oruR01Message;
    }

    private void createMshSegment(String currentDateTimeString) throws DataTypeException {
        MSH mshSegment = _oruR01Message.getMSH();
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
        PID pid = _oruR01Message.getPATIENT_RESULT().getPATIENT().getPID();
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
    private void createPv1Segment() throws DataTypeException {
        PV1 pv1 = _oruR01Message.getPATIENT_RESULT().getPATIENT().getVISIT().getPV1();
        pv1.getPatientClass().setValue("0");// to represent an 'Outpatient'
        PL assignedPatientLocation = pv1.getAssignedPatientLocation();
        assignedPatientLocation.getFacility().getNamespaceID().setValue("SomeTreatmentFacilityName");
        assignedPatientLocation.getPointOfCare().setValue("SomePointOfCare");
        pv1.getAdmissionType().setValue("ALERT");
        XCN referringDoctor = pv1.getReferringDoctor(0);
        referringDoctor.getIDNumber().setValue("9999999999");
        referringDoctor.getFamilyName().getSurname().setValue("Smith");
        referringDoctor.getGivenName().setValue("Jack");
        referringDoctor.getIdentifierTypeCode().setValue("3456789");
        pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(getCurrentTimeStamp());
    }

    private void createObrSegment() throws DataTypeException {
        ORU_R01_ORDER_OBSERVATION orderObservation = _oruR01Message.getPATIENT_RESULT().getORDER_OBSERVATION();
        OBR obr = orderObservation.getOBR();
        obr.getSetIDOBR().setValue("1");
        obr.getPlacerOrderNumber().getUniversalID().setValue("98876343");
        obr.getFillerOrderNumber().getUniversalID().setValue("2323232");
        obr.getUniversalServiceIdentifier().getText().setValue("Document");
        obr.getObservationEndDateTime().getTimeOfAnEvent().setValue(getCurrentTimeStamp());
        obr.getResultStatus().setValue("F");
    }

    private void createObxSegment() throws DataTypeException,IOException {
        ORU_R01_OBSERVATION observation = _oruR01Message.getPATIENT_RESULT().getORDER_OBSERVATION().getOBSERVATION(0);
        OBX obx = observation.getOBX();
        obx.getSetIDOBX().setValue("0");
        obx.getValueType().setValue("ED");
        obx.getObservationIdentifier().getIdentifier().setValue("Report");
        Varies value = obx.getObservationValue(0);
        ED encapsulatedData = new ED(_oruR01Message);
        String base64EncodedStringOfReport = base64Helper.convertToBase64String(new File("pdfTestFile.pdf"));
        encapsulatedData.getEd1_SourceApplication().getHd1_NamespaceID().setValue("AlmaHL7Application");
        encapsulatedData.getTypeOfData().setValue("AP"); //see HL7 table 0191: Type of referenced data
        encapsulatedData.getDataSubtype().setValue("PDF");
        encapsulatedData.getEncoding().setValue("Base64");

        encapsulatedData.getData().setValue(base64EncodedStringOfReport);
        value.setData(encapsulatedData);

    }

    private String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    private String getSequenceNumber() {
        String facilityNumberPrefix = "1234"; // some arbitrary prefix for the facility
        return facilityNumberPrefix.concat(getCurrentTimeStamp());
    }
}
































