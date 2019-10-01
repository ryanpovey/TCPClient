package com.company;

import org.junit.Test;

import java.io.IOException;

public class TCPClientTest {

    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect1() throws IOException {
        TCPClient client = new TCPClient();
        client.startConnection("193.104.87.251", 34323);

        String response = client.sendMessage();
        //client.stopConnection();
        System.out.println(response);
    }

    @Test
    public void sirena() throws IOException {
        TCPClientSirena client = new TCPClientSirena();
        client.startConnection("193.104.87.251", 34323);

        String response = client.sendMessage();
        //client.stopConnection();
        System.out.println(response);
    }


    @Test
    public void sirena2() throws IOException {
        TCPClientSirena client = new TCPClientSirena();
        client.startConnection("193.104.87.251", 34323);

        String response = client.sendMessage();
        //client.stopConnection();
        System.out.println(response);
    }

}