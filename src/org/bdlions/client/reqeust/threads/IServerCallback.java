/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.client.reqeust.threads;

import com.auction.dto.response.ClientResponse;
import org.bdlions.transport.packet.IPacketHeader;

/**
 *
 * @author alamgir
 */
public interface IServerCallback {
    public void timeout(String packetId);
    public void errorHandler(IPacketHeader packetHeader, String response);
    public void resultHandler(IPacketHeader packetHeader, String response);
}
