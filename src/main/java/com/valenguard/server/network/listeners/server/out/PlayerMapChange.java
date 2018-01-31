package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Player;
import com.valenguard.server.maps.Location;
import com.valenguard.server.network.shared.Opcodes;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class PlayerMapChange extends ServerOutPacket {

    private Location teleportLocation;

    public PlayerMapChange(Player player, Location teleportLocation) {
        super(Opcodes.PLAYER_MAP_CHANGE, player);
        this.teleportLocation = teleportLocation;
    }

    @Override
    protected void createPacket(ObjectOutputStream write) throws IOException {
        write.writeUTF(teleportLocation.getMapName());
        write.writeInt(teleportLocation.getX());
        write.writeInt(teleportLocation.getY());
    }
}
