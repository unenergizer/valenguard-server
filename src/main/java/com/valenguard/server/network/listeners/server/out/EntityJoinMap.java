package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Entity;
import com.valenguard.server.entity.Player;
import com.valenguard.server.network.shared.Opcodes;

public class EntityJoinMap extends ServerOutPacket {

    private Entity entityWhoJoined;

    public EntityJoinMap(Player player, Entity entityWhoJoined) {
        super(player);
        this.entityWhoJoined = entityWhoJoined;
        if (player.equals(entityWhoJoined)) throw new RuntimeException("The entity and the player must not be the same!");
    }

    @Override
    public void sendPacket() {
        player.getClientHandler().write(Opcodes.ENTITY_JOINED_MAP, write -> {
            System.out.println("Sending entity info about a player who has joined.");
            write.writeInt(entityWhoJoined.getEntityID());
            write.writeInt(entityWhoJoined.getLocation().getX());
            write.writeInt(entityWhoJoined.getLocation().getY());
        });
    }
}
