package com.valenguard.server.entity;

import com.valenguard.server.ValenguardMain;
import com.valenguard.server.constants.NewPlayerConstants;
import com.valenguard.server.maps.Location;
import com.valenguard.server.maps.MapData;
import com.valenguard.server.network.ClientHandler;
import com.valenguard.server.network.listeners.server.out.EntityExitMap;
import com.valenguard.server.network.listeners.server.out.EntityJoinMap;
import com.valenguard.server.network.listeners.server.out.InitPlayerClient;
import com.valenguard.server.network.shared.Opcodes;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private int lastFakeID = 0; //Temporary player ID. In the future this ID will come from the database.
    private List<Player> onlinePlayers = new ArrayList<>();

    /**
     * Player has just logged in for the first time. Lets get everyone updated.
     * @param clientHandler The client handle of the player.
     */
    public synchronized void onPlayerConnect(ClientHandler clientHandler) {
        //TODO: GET LAST LOGIN INFO FROM DATABASE, UNLESS PLAYER IS TRUE "NEW PLAYER."
        MapData mapData = ValenguardMain.getInstance().getMapManager().getMapData(NewPlayerConstants.STARTING_MAP);

        // Below we create a starting location for a new player.
        // The Y cord is subtracted from the height of the map.
        // The reason for this is because on the Tiled Editor
        // the Y cord is reversed.  This just makes our job
        // easier if we want to quickly grab a cord from the
        // Tiled Editor without doing the subtraction ourselves.
        Location location = new Location(NewPlayerConstants.STARTING_MAP,
                NewPlayerConstants.STARTING_X_CORD,
                mapData.getMapHeight() - NewPlayerConstants.STARTING_Y_CORD);

        Player player = new Player(lastFakeID, location, 2, clientHandler);
        onlinePlayers.add(player);

        System.out.println("Writing out initialization information to the player.");
        System.out.println("eID: " + lastFakeID + ", X: " + player.getLocation().getX() + ", Y: " + player.getLocation().getY());

        // Send this new client connection information about their location and other various information.
        new InitPlayerClient(player).sendPacket();

        // Lets update everyone because a player has joined the map.
        mapData.getPlayerList().forEach(playerOnMap -> {

            // Send all players on the map info on the new player.
            new EntityJoinMap(playerOnMap, player).sendPacket();

            // Send the new player info about all players already on the map.
            new EntityJoinMap(player, playerOnMap).sendPacket();
        });

        // Add the player to the map they are on.
        mapData.addPlayer(player);

        //TODO: This should be a ID from the database. Until then, increment the fake ID for the next client connection.
        lastFakeID++;
    }

    /**
     * Player is disconnecting. Lets clean up after them.
     * @param clientHandler The client connection that refers to a specific player.
     */
    public synchronized void onPlayerDisconnect(ClientHandler clientHandler) {
        Player player = getPlayer(clientHandler);

        // Lets update everyone because a player has left the map.
        player.getLocation().getMapData().getPlayerList().forEach(playerOnMap -> {

            if (player.equals(playerOnMap)) return;

            // Let all the players on the map know about the exit
            new EntityExitMap(playerOnMap, player).sendPacket();
        });

        // Remove the player from the map they are in.
        ValenguardMain.getInstance().getMapManager().getMapData(player.getMapData().getMapName()).removePlayer(player);
        player.setLocation(null);
        player.setFutureLocation(null);

        // Remove the player from the player manager.
        onlinePlayers.remove(getPlayer(clientHandler));
    }

    /**
     * Helper method to get a entity via their client handle.
     *
     * @param clientHandler The client handle we will use to find a entity.
     * @return The entity associated with this client handle.
     */
    public Player getPlayer(ClientHandler clientHandler) {
        for (Player player : onlinePlayers) {
            if (player.getClientHandler().equals(clientHandler)) {
                return player;
            }
        }
        throw new RuntimeException("Player not found using this ClientHandler.");
    }
}
