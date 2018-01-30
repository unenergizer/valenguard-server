package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Entity;
import com.valenguard.server.entity.Player;
import com.valenguard.server.network.ClientHandler;
import com.valenguard.server.network.shared.Listener;
import com.valenguard.server.network.shared.Opcode;
import com.valenguard.server.network.shared.Opcodes;
import lombok.AllArgsConstructor;

import java.io.ObjectOutputStream;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/8/2018 @ 5:28 PM
 * ______________________________________________________
 *
 * Copyright © 2017 Valenguard.com. All Rights Reserved.
 *
 * No part of this project and/or code and/or source code 
 * and/or source may be reproduced, distributed, or 
 * transmitted in any form or by any means, including 
 * photocopying, recording, or other electronic or 
 * mechanical methods, without the prior written 
 * permission of the owner.
 *******************************************************/

public class EntityMoveUpdate extends ServerOutPacket {

    /**
     * We will update the player with the movements from
     * this entity.
     */
    private Entity entityWhoMoved;

    public EntityMoveUpdate(Player player, Entity entityWhoMoved) {
        super(player);
        this.entityWhoMoved = entityWhoMoved;
    }

    @Override
    public void sendPacket() {
        System.out.println("EntityMoveUpdate packet sent");
        player.getClientHandler().write(Opcodes.ENTITY_MOVE_UPDATE, (ObjectOutputStream write) -> {
            write.writeInt(entityWhoMoved.getEntityID());
            write.writeInt(entityWhoMoved.getFutureLocation().getX());
            write.writeInt(entityWhoMoved.getFutureLocation().getY());
        });
    }
}
