package com.valenguard.server.network.listeners.server.out;

import com.valenguard.server.entity.Player;
import com.valenguard.server.maps.Location;
import com.valenguard.server.network.ClientHandler;
import com.valenguard.server.network.shared.Listener;
import com.valenguard.server.network.shared.Opcode;
import com.valenguard.server.network.shared.Opcodes;

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

public class MoveReply extends ServerOutPacket {

    /**
     * Let the client know if they are able to move.
     * If true, the client will move to another tile.
     * If false, the client will not move.
     */
    private boolean moveAllowed;

    /**
     * We will update the client with this location.
     */
    private Location location;

    public MoveReply(Player player, boolean moveAllowed, Location location) {
        super(player);
        this.moveAllowed = moveAllowed;
        this.location = location;
    }

    @Override
    public void sendPacket() {
        player.getClientHandler().write(Opcodes.MOVE_REPLY, (ObjectOutputStream write) -> {
            write.writeBoolean(moveAllowed);
            write.writeInt(location.getX());
            write.writeInt(location.getY());
        });
    }
}
