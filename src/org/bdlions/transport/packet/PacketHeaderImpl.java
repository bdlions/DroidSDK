/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.transport.packet;

import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import java.util.UUID;

/**
 *
 * @author alamgir
 */
public class PacketHeaderImpl implements IPacketHeader{

    public PacketHeaderImpl() {
        packetId = UUID.randomUUID().toString();
    }

    
    private ACTION action ;
    private REQUEST_TYPE requestType;
    private String sessionId;
    private boolean isBroken;
    private final String packetId;
    
    public void setAction(ACTION action) {
        this.action = action;
    }

    public void setRequestType(REQUEST_TYPE requestType) {
        this.requestType = requestType;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    
    @Override
    public ACTION getAction() {
        return action;
    }

    @Override
    public REQUEST_TYPE getRequestType() {
        return requestType;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String getPacketId() {
        return packetId;
    }

    @Override
    public boolean isBroken() {
        return isBroken;
    }
    
}
