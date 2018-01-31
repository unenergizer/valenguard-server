package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Entity;
import com.valenguard.server.entity.Player;
import com.valenguard.server.network.shared.Opcodes;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class EntityJoinMap extends ServerOutPacket {

    private Entity entityWhoJoined;

    public EntityJoinMap(Player player, Entity entityWhoJoined) {
        super(Opcodes.ENTITY_JOINED_MAP, player);
        this.entityWhoJoined = entityWhoJoined;
        if (player.equals(entityWhoJoined)) throw new RuntimeException("The entity and the player must not be the same!");
    }

    @Override
    protected void createPacket(ObjectOutputStream write) throws IOException {
        write.writeInt(entityWhoJoined.getEntityID());
        write.writeInt(entityWhoJoined.getLocation().getX());
        write.writeInt(entityWhoJoined.getLocation().getY());
    }
}
