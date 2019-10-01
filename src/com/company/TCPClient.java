package com.company;

import org.apache.commons.lang.ArrayUtils;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

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

        byte[] header = new byte[0];

        //0	4	Integer	Message body length (without header)
        byte[] msg = intToByte(12345);
        header = ArrayUtils.addAll(header, msg);

        //4	4	Integer	Request generation time (number of seconds since January 1, 1970 GMT)
        final long epochSecond = Instant.now().getEpochSecond();
        System.out.println(epochSecond);
        byte[] timeMsg = longToByte(epochSecond);
        header = ArrayUtils.addAll(header, timeMsg);

        //8	4	Integer	Message identifier
        byte[] idMsg = intToByte(999);
        header = ArrayUtils.addAll(header, idMsg);

        //12	32	 	Reserved (zero byte)
        byte[] reservedMsg = new byte[32];
        header = ArrayUtils.addAll(header, reservedMsg);

        //44	2	Integer	Client identifier ????? 2 bytes for int
        byte[] clientMsg = intToByte(1);
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

        String resp = in.readLine();
        return resp;
    }

    private static byte[] intToByte(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    private static byte[] intToByte2(int value) {
        return ByteBuffer.allocate(2).putInt(value).array();
    }

    byte[] longToByte(long value)
    {
        byte [] data = new byte[4];
        data[3] = (byte) value;
        data[2] = (byte) (value >>> 8);
        data[1] = (byte) (value >>> 16);
        data[0] = (byte) (value >>> 32);

        return data;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

}