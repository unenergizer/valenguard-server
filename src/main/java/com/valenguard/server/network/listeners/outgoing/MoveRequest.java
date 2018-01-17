package com.valenguard.server.network.listeners.outgoing;

import com.valenguard.server.network.ClientHandle;
import com.valenguard.server.network.shared.Listener;
import com.valenguard.server.network.shared.Opcode;
import com.valenguard.server.network.shared.Opcodes;

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

public class MoveRequest implements Listener {

    @Opcode(getOpcode = Opcodes.MOVE_REQUEST)
    public void onMoveRequest(ClientHandle clientHandle) {
        byte moveData = clientHandle.readByte();
        System.out.println(Byte.toString(moveData));
        clientHandle.write(Opcodes.MOVE_REPLY, (write) -> write.writeByte(moveData));
    }
}
