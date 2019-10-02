package main.java.com.company;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TCPClient {

    private Socket clientSocket;
    private OutputStream out;
    private InputStream in;

    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
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

    private static int fromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    private static int from2ByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getShort();
    }

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = clientSocket.getOutputStream();
        in = clientSocket.getInputStream();
    }

    public String sendMessage() throws IOException {
        send();

        List<Byte> byteList = getHeaderInputStream();

        System.out.println("Header : " + byteList);
        System.out.println("Header size : " + byteList.size());
        System.out.println("-------------------------------");

        getHeaderInfo(byteList);

        String resp = getBufferedReaderResponse();
        System.out.println("Response Length : " + resp.length());

        stopConnection();
        return resp;
    }

    public String sendMessage1() throws IOException {
        send();

        final List<Byte> byteList = getHeaderInputStreamAndBuffered();
        System.out.println("Header : " + byteList);
        System.out.println("Header size : " + byteList.size());
        System.out.println("-------------------------------");

        getHeaderInfo(byteList);

        String resp = getBufferedReaderResponse();
        stopConnection();
        return resp;
    }

    private List<Byte> getHeaderInputStream() throws IOException {
        byte[] inputTwo = new byte[100];
        List<Byte> byteList = new ArrayList<>();
        int result = 0;
        while (result > -1 && byteList.isEmpty()) {
            result = in.read(inputTwo);
            if (result > -1) {
                for (byte b : inputTwo) {
                    byteList.add(b);
                }
            }
        }
        return byteList;
    }

    private List<Byte> getHeaderInputStreamAndBuffered() throws IOException {
        int line;
        int responseHeaderCount = 1;
        List<Byte> byteList = new ArrayList<>();
        while ((line = in.read()) != -1 && responseHeaderCount < 100) {
            System.out.println("Int : " + line);
            System.out.println("Char : " + ((char) line));
            System.out.println("Count : " + responseHeaderCount);
            byte b = (byte) line;
            byteList.add(b);
            responseHeaderCount++;
        }
        return byteList;
    }

    private void getHeaderInfo(final List<Byte> byteList) {

        byte[] msgLenBytes = new byte[] {byteList.get(0), byteList.get(1), byteList.get(2), byteList.get(3)};
        final int msgLen = fromByteArray(msgLenBytes);
        System.out.println("msgLen : " + msgLen);

        byte[] timeBytes = new byte[] {byteList.get(4), byteList.get(5), byteList.get(6), byteList.get(7)};
        final int time = fromByteArray(timeBytes);
        System.out.println("time : " + time);

        byte[] idMsgBytes = new byte[] {byteList.get(8), byteList.get(9), byteList.get(10), byteList.get(11)};
        final int idMsg = fromByteArray(idMsgBytes);
        System.out.println("idMsg : " + idMsg);

        byte[] clientIdBytes = new byte[] {byteList.get(44), byteList.get(45)};
        final int clientId = from2ByteArray(clientIdBytes);
        System.out.println("clientId : " + clientId);

        final int flag1 = byteList.get(46);
        System.out.println("flag1 : " + flag1);

        final int flag2 = byteList.get(47);
        System.out.println("flag2 : " + flag2);

        byte[] keyBytes = new byte[] {byteList.get(48), byteList.get(49), byteList.get(50), byteList.get(51)};
        final int key = fromByteArray(keyBytes);
        System.out.println("key : " + key);

    }

    private String getBufferedReaderResponse() throws IOException {
        BufferedReader inXml = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        StringBuilder responseXml = new StringBuilder();
        String responseXmlLine;

        while ((responseXmlLine = inXml.readLine()) != null && !StringUtils.equals(responseXmlLine, "</sirena>")) {
            responseXml.append(responseXmlLine);
        }
        responseXml.append(responseXmlLine);
        return responseXml.toString();
    }

    private void send() throws IOException {
//        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//                "<sirena>" +
//                "<query>" +
//                "<iclient_pub_key>" +
//                "<pub_key>MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuNN" +
//                "drDTCHfFK4SVafOOfJeJvW2JdiV2jE2PJj7wCii/dL" +
//                "H+65QC4X0qwGOQZ+T+SRvrkEqzcf04pUwlti8cLjHjC" +
//                "ROscuyswFm02pnAjZaNl2h4nEOel8pi8tlwXpL/Vwph" +
//                "EDdrRK5Pd9fYS7x5EtuRnrWuhUUV478Nz2GW5AgQIDAQAB</pub_key>" +
//                "</iclient_pub_key>" +
//                "</query>" +
//                "</sirena>";


        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<sirena>" +
                "<query>" +
                "<pricing>" +
                "<segment>" +
                "<departure>CPT</departure>" +
                "<arrival>JNB</arrival>" +
                "<date>04.10.19</date>" +
                "</segment>" +
                "<passenger>" +
                "<code>ADT</code>" +
                "<count>1</count>" +
                "<age>30</age>" +
                "</passenger>" +
                "</pricing>" +
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
        out.write(xml.getBytes());

        System.out.println(bytesToHex(header));

    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

}