package com.company;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.time.Instant;

public class TCPClient {

    private Socket clientSocket;
    private OutputStream out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = clientSocket.getOutputStream();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage() throws IOException {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<sirena>" +
                "<query>" +
                "<iclient_pub_key>" +
                "<pub_key>MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuNN" +
                "drDTCHfFK4SVafOOfJeJvW2JdiV2jE2PJj7wCii/dL" +
                "H+65QC4X0qwGOQZ+T+SRvrkEqzcf04pUwlti8cLjHjC" +
                "ROscuyswFm02pnAjZaNl2h4nEOel8pi8tlwXpL/Vwph" +
                "EDdrRK5Pd9fYS7x5EtuRnrWuhUUV478Nz2GW5AgQIDAQAB</pub_key>" +
                "</iclient_pub_key>" +
                "</query>" +
                "</sirena>";


        byte[] header = new byte[0];

        //0	4	Integer	Message body length (without header)
        byte[] msg = intToByte(xml.length());

        System.out.println("Xml Length: " + xml.length());

        header = ArrayUtils.addAll(header, msg);

        //4	4	Integer	Request generation time (number of seconds since January 1, 1970 GMT)
        final int epochSecond = (int) Instant.now().getEpochSecond();
        System.out.println("Now: " + epochSecond);
        byte[] timeMsg = intToByte(epochSecond);
        header = ArrayUtils.addAll(header, timeMsg);

        //8	4	Integer	Message identifier
        byte[] idMsg = intToByte(999);
        header = ArrayUtils.addAll(header, idMsg);

        //12	32	 	Reserved (zero byte)
        byte[] reservedMsg = new byte[32];
        header = ArrayUtils.addAll(header, reservedMsg);

        //44	2	Integer	Client identifier ????? 2 bytes for int
        byte[] clientMsg = intToByte2(8153);
        header = ArrayUtils.addAll(header, clientMsg);

        //46	1	 	1st byte of message flags
        byte[] flag1 = new byte[1];
        header = ArrayUtils.addAll(header, flag1);

        //47	1	 	2nd byte of message flags
        byte[] flag2 = new byte[1];
        header = ArrayUtils.addAll(header, flag2);

        //48	4	Integer	Symmetric key identifier
        byte[] keyMsg = intToByte(444);
        header = ArrayUtils.addAll(header, keyMsg);

        //52	48	 	Reserved (zero byte)
        byte[] reserved = new byte[48];
        header = ArrayUtils.addAll(header, reserved);

        out.write(header);

        System.out.println(bytesToHex(header));

        out.write(xml.getBytes());


        String resp = in.readLine();
        return resp;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    private static byte[] intToByte(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    private static byte[] intToByte2(int value) {
        return ByteBuffer.allocate(2).putShort((short) value).array();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

}