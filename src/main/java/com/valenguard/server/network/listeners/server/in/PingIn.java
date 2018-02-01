package com.valenguard.server.network.listeners.server.in;

import com.valenguard.server.ValenguardMain;
import com.valenguard.server.network.ClientHandler;
import com.valenguard.server.network.listeners.server.out.PingOut;
import com.valenguard.server.network.shared.Listener;
import com.valenguard.server.network.shared.Opcode;
import com.valenguard.server.network.shared.Opcodes;

public class PingIn implements Listener {

    @Opcode(getOpcode = Opcodes.PING)
    public void onPingIn(ClientHandler clientHandler) {
        new PingOut(ValenguardMain.getInstance().getPlayerManager().getPlayer(clientHandler)).sendPacket();
    }
}
