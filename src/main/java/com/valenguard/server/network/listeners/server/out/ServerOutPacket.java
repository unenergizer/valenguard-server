package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Player;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ServerOutPacket {

    /**
     * The player who will receive the packet.
     */
    protected Player player;

    /**
     * Sends the packet.
     */
    public abstract void sendPacket();
}
