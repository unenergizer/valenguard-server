package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Entity;
import com.valenguard.server.entity.Player;
import com.valenguard.server.network.shared.Opcodes;

public class EntityExitMap extends ServerOutPacket {

    private Entity entityWhoJoined;

    public EntityExitMap(Player player, Entity entityWhoLeft) {
        super(player);
        this.entityWhoJoined = entityWhoLeft;
        if (player.equals(entityWhoLeft)) throw new RuntimeException("The entity and the player must not be the same!");
    }

    @Override
    public void sendPacket() {
        player.getClientHandler().write(Opcodes.ENTITY_EXIT_MAP, write -> {
            System.out.println("Sending entity info about a player who has left.");
            write.writeInt(entityWhoJoined.getEntityID());
        });
    }
}
