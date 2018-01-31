package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Player;
import com.valenguard.server.network.shared.Opcodes;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class InitPlayerClient extends ServerOutPacket {


    public InitPlayerClient(Player player) {
        super(Opcodes.INIT_PLAYER_CLIENT, player);
    }

    @Override
    protected void createPacket(ObjectOutputStream write) throws IOException {
        write.writeInt(player.getEntityID());
        write.writeInt(player.getLocation().getX());
        write.writeInt(player.getLocation().getY());
    }
}
