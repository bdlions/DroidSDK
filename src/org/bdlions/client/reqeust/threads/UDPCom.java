package org.bdlions.client.reqeust.threads;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bdlions.transport.packet.IPacketHeader;
import org.bdlions.transport.packet.PacketHeaderImpl;
import org.bdlions.transport.packet.PacketImpl;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alamgir
 */
public class UDPCom implements Runnable {

//    private final int SERVER_PORT = 10001;
//    private final int CLIENT_PORT = 5000;
    private static UDPCom instance;
    private int serverPort;
    private String serverAddress;

    private PacketMonitor packetMonitor = new PacketMonitor();

    boolean running = true;
    private DatagramSocket ds;
//    private static IPacketHeader packetHeader;
    private Gson gson = new GsonBuilder().create();

    private UDPCom(String serverAddress, int port) {
        try {
            this.serverAddress = serverAddress;
            this.serverPort = port;
//        ds = new DatagramSocket(CLIENT_PORT);
            ds = new DatagramSocket();
            new Thread(packetMonitor).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public static UDPCom getInstance(String serverAddress, int port) {
        if (instance == null) {
            instance = new UDPCom(serverAddress, port);
            instance.start();
        }
        return instance;
    }

    public void start(){
        new Thread(this).start();
    }
    
    public void send(IPacketHeader packetHeader, String packetBodyContent, IServerCallback callback) {
        try {

            packetMonitor.put(packetHeader.getPacketId(), callback);

            int packetHeaderLengthSize = 2;
            int packetBodyLengthSize = 2;

            String packetHeaderContent = new GsonBuilder().create().toJson(packetHeader);
//            String packetBodyContent = "{\"userName\": \"" + "alamgir" + "\", \"password\": \"" + "pass" + "\"}";

            int packetSize = packetHeaderLengthSize + packetBodyLengthSize + packetHeaderContent.length() + packetBodyContent.length();
            byte[] sendPacket = new byte[packetSize];

            int start = 0;
            sendPacket[start++] = (byte) ((packetHeaderContent.length() >> 8) & 0xFF);
            sendPacket[start++] = (byte) (packetHeaderContent.length() & 0xFF);

            System.arraycopy(packetHeaderContent.getBytes(), 0, sendPacket, start, packetHeaderContent.length());
            start += packetHeaderContent.length();

            sendPacket[start++] = (byte) ((packetBodyContent.length() >> 8) & 0xFF);
            sendPacket[start++] = (byte) (packetBodyContent.length() & 0xFF);

            System.arraycopy(packetBodyContent.getBytes(), 0, sendPacket, start, packetBodyContent.length());

            ds.send(new DatagramPacket(sendPacket, sendPacket.length, InetAddress.getByName(serverAddress), serverPort));

        } catch (IOException ex) {
            Logger.getLogger(UDPCom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                int packetHeaderLengthSize = 2;
                int packetBodyLengthSize = 2;

                byte[] received = new byte[3048];
                DatagramPacket packet = new DatagramPacket(received, received.length);
                ds.receive(packet);
                byte[] data = packet.getData();
                int packetHeaderLength = ((data[0] & 0xff) << 8) | (data[1] & 0xff);
                if (packetHeaderLength <= 0) {
                    //exception thorw invalid header
                    return;
                }
                byte[] pacektHeaderData = new byte[packetHeaderLength];
                System.arraycopy(data, packetHeaderLengthSize, pacektHeaderData, 0, packetHeaderLength);
                String packetHeaderContent = new String(pacektHeaderData);
                IPacketHeader packetHeader = gson.fromJson(packetHeaderContent, PacketHeaderImpl.class);

                int dataLenghtPosition = packetHeaderLengthSize + packetHeaderLength;
                String packetDataContent = null;
                PacketImpl packetImpl = new PacketImpl();
                packetImpl.setPacketHeader(packetHeader);
                packetImpl.setPacketHeaderData(pacektHeaderData);
                int packetDataLength = ((data[dataLenghtPosition] & 0xff) << 8) | (data[dataLenghtPosition + 1] & 0xff);
                if (packetDataLength > 0) {
                    byte[] packetDataBytes = new byte[packetDataLength];
                    int packetDataStart = packetHeaderLengthSize + packetHeaderLength + packetBodyLengthSize;
                    System.arraycopy(data, packetDataStart, packetDataBytes, 0, packetDataLength);
                    packetDataContent = new String(packetDataBytes);
                    packetImpl.setPacketBody(packetDataContent);

                    packetMonitor.get(packetHeader.getPacketId()).resultHandler(packetHeader, packetDataContent);
                    packetMonitor.removePacket(packetHeader.getPacketId());
                }
            } catch (IOException ex) {
                Logger.getLogger(UDPCom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
