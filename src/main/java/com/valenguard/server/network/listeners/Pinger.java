package com.valenguard.server.network.listeners;

import com.valenguard.server.network.ClientHandle;
import com.valenguard.server.network.shared.Listener;
import com.valenguard.server.network.shared.Opcode;
import com.valenguard.server.network.shared.Opcodes;

public class Pinger implements Listener {

    @Opcode(getOpcode = Opcodes.PING_PONG)
    public void onPong(ClientHandle clientHandle) {
        //System.out.println(clientHandle.readString());
        clientHandle.write(Opcodes.PING_PONG, (write) -> write.writeUTF("ping"));
    }

   /* @Opcode(getOpcode = Opcodes.LOGIN_REQUEST)
    public void onLoginRequest(ClientHandle clientHandle) {
        String username = clientHandle.readString();
        String password = clientHandle.readString();

        if (Database.lookupUser(username).exist()) {
            if (Database.getUser(username).checkPassword(password)) {
                clientHandle.write(Opcodes.LOGIN_REQUEST, (write) -> write.writeUTF("Successful login"));
            } else {
                clientHandle.write(Opcodes.LOGIN_REQUEST, (write) -> write.writeUTF("Incorrect Password"));
            }
        } else {
            clientHandle.write(Opcodes.LOGIN_REQUEST, (write) -> write.writeUTF("Could not find a server account with that name"));
        }
    }*/
}
