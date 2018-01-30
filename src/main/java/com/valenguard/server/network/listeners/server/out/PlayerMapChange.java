package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Player;
import com.valenguard.server.maps.Location;
import com.valenguard.server.network.shared.Opcodes;

public class PlayerMapChange extends ServerOutPacket {

    private Location teleportLocation;

    public PlayerMapChange(Player player, Location teleportLocation) {
        super(player);
        this.teleportLocation = teleportLocation;
    }

    @Override
    public void sendPacket() {
        player.getClientHandler().write(Opcodes.PLAYER_MAP_CHANGE, write -> {
            System.out.println("Attempting map change.");
            write.writeUTF(teleportLocation.getMapName());
            write.writeInt(teleportLocation.getX());
            write.writeInt(teleportLocation.getY());
        });
    }
}
