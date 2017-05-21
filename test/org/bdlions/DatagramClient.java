/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions;

import com.auction.util.ACTION;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.SocketException;
import org.bdlions.client.reqeust.threads.IServerCallback;
import org.bdlions.client.reqeust.threads.UDPClient;
import org.bdlions.transport.packet.IPacketHeader;
import org.bdlions.transport.packet.PacketHeaderImpl;

/**
 *
 * @author alamgir
 */
public class DatagramClient implements IServerCallback {

    private Gson gson = new GsonBuilder().create();

    public void initialize(UDPClient cl) {

//            String s = new GsonBuilder().create().toJson(packetHeader);
//            DatagramClient cl = new DatagramClient();
        new Thread(cl).start();

        PacketHeaderImpl packetHeader = new PacketHeaderImpl();
        packetHeader.setAction(ACTION.SIGN_IN);
        cl.send(packetHeader, "{\"userName\": \"" + "alamgir" + "\", \"password\": \"" + "pass12" + "\"}", this);

        PacketHeaderImpl packetHeader2 = new PacketHeaderImpl();
        packetHeader2.setAction(ACTION.FETCH_BID_LIST);
        cl.send(packetHeader2, "{\"userName\": \"" + "alamgir" + "\", \"password\": \"" + "pass12" + "\"}", this);

    }

    public static void main(String[] args) throws SocketException {
        DatagramClient dcl = new DatagramClient();
        UDPClient cl = new UDPClient("192.168.1.107");
        dcl.initialize(cl);
    }

    @Override
    public void timeout(String packetId) {
        System.out.println("Packet Id: " + packetId);
    }

    @Override
    public void errorHandler(IPacketHeader packetHeader, String response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resultHandler(IPacketHeader packetHeader, String response) {
        System.out.println("Result : " + gson.toJson(packetHeader) + " " + response);
    }

}
