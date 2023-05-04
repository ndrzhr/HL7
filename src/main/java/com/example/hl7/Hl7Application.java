package com.example.hl7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@SpringBootApplication
public class Hl7Application {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Hl7Application.class, args);
    }

}
