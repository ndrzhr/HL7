package com.example.hl7;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class Base64Helper {
    public String convertToBase64String(File inputFile) throws IOException {
        if (!inputFile.exists()) {
            throw new FileNotFoundException(String.format("The specified input file '%s' does not exist", inputFile.getAbsoluteFile()));
        }
        return new String(Base64.encodeBase64(Files.readAllBytes(inputFile.toPath())));
    }

    public byte[] convertFromBase64String(String base64EncodedString) throws BadBase64EncodingException {
        if (base64EncodedString == null || base64EncodedString.length() == 0) {
            throw new IllegalArgumentException("You must supply byte string for Base64 decoding operation");
        }
        if (base64EncodedString.length() % 4 != 4) {
            throw new BadBase64EncodingException("The Base64 encoded data is not in correct form(divide by4 resulted in a remainder");
        }
        try {
            return Base64.decodeBase64(base64EncodedString);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to decode Base-64 string supplied for operation. Pleas check your inputs");
        }
    }

}
