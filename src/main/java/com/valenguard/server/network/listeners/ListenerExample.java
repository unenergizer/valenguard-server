package com.valenguard.server.network.listeners;

import com.valenguard.server.network.ClientHandle;
import com.valenguard.server.network.shared.Listener;
import com.valenguard.server.network.shared.Opcode;
import com.valenguard.server.network.shared.Opcodes;

import java.io.Serializable;

public class ListenerExample implements Listener {

    class MyObject implements Serializable {
        int x, y;
    }

//    @Opcode(getOpcode = Opcodes.ANOTHER)
//    public void onIncoming(ClientHandle clientHandle) {
//
//        // Reading in the data that the client sent us
//        clientHandle.readInt();
//        clientHandle.readChar();
//
//        // Sending out a opcode followed by writing out a byte and a object
//        clientHandle.write(Opcodes.EXAMPLE_CASE, (write) -> {
//            write.writeByte(4);
//            write.writeObject(new MyObject());
//        });
//    }
}
