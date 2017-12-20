package com.valenguard.server;

import com.valenguard.server.shared.Listener;
import com.valenguard.server.shared.Opcode;
import com.valenguard.server.shared.Opcodes;

import java.util.Scanner;

public class ServerMain implements Listener {
    public static void main(String[] args) {
        Server server = new Server();
        server.openServer((eventBus) -> {
            eventBus.registerListener(new ServerMain());
        });

        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("stop")) break;
        }
        scanner.close();

        server.close();
    }

    @Opcode(getOpcode = Opcodes.EXAMPLE_OPCODE)
    public void onIncomingData(ClientHandle clientHandle) {
        System.out.println(clientHandle.readString());
        clientHandle.write(Opcodes.EXAMPLE_OPCODE, (outStream) -> outStream.writeUTF("pong"));
    }
}
