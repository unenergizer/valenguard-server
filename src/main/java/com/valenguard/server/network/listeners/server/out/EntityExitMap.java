package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Entity;
import com.valenguard.server.entity.Player;
import com.valenguard.server.network.shared.Opcodes;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class EntityExitMap extends ServerOutPacket {

    private Entity entityWhoJoined;

    public EntityExitMap(Player player, Entity entityWhoLeft) {
        super(Opcodes.ENTITY_EXIT_MAP, player);
        this.entityWhoJoined = entityWhoLeft;
        if (player.equals(entityWhoLeft)) throw new RuntimeException("The entity and the player must not be the same!");
    }

    @Override
    protected void createPacket(ObjectOutputStream write) throws IOException {
        System.out.println("Sending entity info about a player who has left.");
        write.writeInt(entityWhoJoined.getEntityID());
    }

}
