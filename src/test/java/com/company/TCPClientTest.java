package test.java.com.company;

import org.junit.Test;

import java.io.IOException;

import main.java.com.company.TCPClient;

public class TCPClientTest {

    @Test
    public void sirena1() throws IOException {
        TCPClient client = new TCPClient();
        client.startConnection("127.0.0.1", 6789);

        String response = client.sendMessage();
        //client.stopConnection();
        System.out.println("Response : ");
        System.out.println(response);
    }

    @Test
    public void sirena2() throws IOException {
        TCPClient client = new TCPClient();
        client.startConnection("127.0.0.1", 6789);

        String response = client.sendMessage1();
        //client.stopConnection();
        System.out.println("Response : ");
        System.out.println(response);
    }

}