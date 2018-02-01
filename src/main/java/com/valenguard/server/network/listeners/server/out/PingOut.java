package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Player;
import com.valenguard.server.network.shared.Opcodes;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class PingOut extends ServerOutPacket {

    public PingOut(Player player) {
        super(Opcodes.PING, player);
    }

    @Override
    protected void createPacket(ObjectOutputStream write) throws IOException {
    }
}
