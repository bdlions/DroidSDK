/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions;

import com.bdlions.util.ACTION;
import com.bdlions.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bdlions.client.reqeust.threads.IServerCallback;
import org.bdlions.client.reqeust.threads.UDPCom;
import org.bdlions.transport.packet.IPacketHeader;
import org.bdlions.transport.packet.PacketHeaderImpl;

/**
 *
 * @author alamgir
 */
public class DatagramClient implements IServerCallback {

    private Gson gson = new GsonBuilder().create();

    public void initialize(UDPCom cl) {

//            String s = new GsonBuilder().create().toJson(packetHeader);
//            DatagramClient cl = new DatagramClient();
//        new Thread(cl).start();

        PacketHeaderImpl packetHeader = new PacketHeaderImpl();
        packetHeader.setAction(ACTION.SIGN_IN);
        packetHeader.setRequestType(REQUEST_TYPE.AUTH);
        cl.send(packetHeader, "{\"userName\": \"" + "bdlions@gmail.com" + "\", \"password\": \"" + "password" + "\"}", this);

//        PacketHeaderImpl packetHeader2 = new PacketHeaderImpl();
//        packetHeader2.setAction(ACTION.FETCH_BID_LIST);
//        packetHeader2.setRequestType(REQUEST_TYPE.REQUEST);
//        cl.send(packetHeader2, "{\"userName\": \"" + "alamgir" + "\", \"password\": \"" + "pass12" + "\"}", this);

    }

    public static void main(String[] args) throws SocketException {
        DatagramClient dcl = new DatagramClient();
        UDPCom cl =  UDPCom.getInstance("185.5.54.210", 10000);
//        UDPClient cl = new UDPClient("127.0.0.1");
//        dcl.initialize(cl);

        for(int i  = 0; i < 1000; i ++){
            org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
            packetHeader.setAction(ACTION.FETCH_ALL_UOMS);
            packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
            String sessionId = "5653cdbf-c070-49b8-bb43-77b2ef8975e9";
            packetHeader.setSessionId(sessionId);
        
            try {
                cl.send(packetHeader, "{}", dcl);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void timeout(String packetId) {
        System.out.println("Packet Id - " + packetId + " timed out.");
    }

    @Override
    public void resultHandler(IPacketHeader packetHeader, String response) {
        System.out.println("Header : " + gson.toJson(packetHeader));
        System.out.println(response);
    }

}
