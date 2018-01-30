package com.valenguard.server.entity;

import com.valenguard.server.maps.Location;
import com.valenguard.server.network.ClientHandler;
import com.valenguard.server.network.shared.Write;
import lombok.Getter;

public class Player extends Entity {

    @Getter
    ClientHandler clientHandler;

    public Player(int entityID, Location location, float moveSpeed, ClientHandler clientHandler) {
        super(entityID, location, moveSpeed);
        this.clientHandler = clientHandler;
    }

    public void sendPacket(byte opcode, Write writeCallback) {
        clientHandler.write(opcode, writeCallback);
    }

    @Override
    protected void finalize() {
        System.out.println("Player Destroyed! IP: " + clientHandler.getClientSocket().getInetAddress() + ", eID: " + getEntityID());
    }
}
