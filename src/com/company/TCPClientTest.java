package com.company;


import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class TCPClientTest {

    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect1() throws IOException {
        TCPClient client = new TCPClient();
        client.startConnection("127.0.0.1", 6789);

        String response = client.sendMessage();
        //client.stopConnection();
        System.out.println(response);
    }

    @Test
    public void sirena() throws IOException {
        TCPClientSirena client = new TCPClientSirena();
        client.startConnection("194.84.25.50", 34322);

        String response = client.sendMessage();
        //client.stopConnection();
        System.out.println(response);
    }

}
