package test.java.com.company;

import org.junit.Test;

import java.io.IOException;

import main.java.com.company.TCPClient;

public class TCPClientTest {

    @Test
    public void sirena1() throws IOException {
        TCPClient client = new TCPClient();
        client.startConnection("193.104.87.251", 34323);

        String response = client.sendMessage();
        //client.stopConnection();
        System.out.println("Response : ");
        System.out.println(response);
    }

    @Test
    public void sirena2() throws IOException {
        TCPClient client = new TCPClient();
        client.startConnection("193.104.87.251", 34323);

        String response = client.sendMessage1();
        //client.stopConnection();
        System.out.println("Response : ");
        System.out.println(response);
    }

}