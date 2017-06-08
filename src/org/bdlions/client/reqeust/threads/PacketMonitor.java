/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.client.reqeust.threads;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author alamgir
 */
public class PacketMonitor implements Runnable {

    private final int PACKET_TIMEOUT = 30000;
    private final ConcurrentHashMap<String, IServerCallback> packetMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> packetTimeoutMap = new ConcurrentHashMap<>();

    public void put(String packetId, IServerCallback callback) {
        packetMap.put(packetId, callback);
        packetTimeoutMap.put(packetId, System.currentTimeMillis());
    }

    public IServerCallback get(String packetId) {
        return packetMap.get(packetId);
    }

    public void removePacket(String packetId) {
        packetMap.remove(packetId);
        packetTimeoutMap.remove(packetId);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Long currentTime = System.currentTimeMillis();

                for (Map.Entry<String, Long> entry : packetTimeoutMap.entrySet()) {
                    String packetId = entry.getKey();
                    Long requestTime = entry.getValue();

                    if (requestTime + PACKET_TIMEOUT >= currentTime) {
                        IServerCallback callback = packetMap.get(packetId);
                        callback.timeout(packetId);
                        packetMap.remove(packetId);
                        packetTimeoutMap.remove(packetId);
                    }
                }
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
