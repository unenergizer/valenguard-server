package com.valenguard.server.serverupdates;

import com.valenguard.server.ValenguardMain;
import com.valenguard.server.entity.Player;
import com.valenguard.server.maps.Location;
import com.valenguard.server.network.listeners.server.out.MoveReply;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class UpdateMovements {

    private Queue<Player> movingPlayers = new ConcurrentLinkedDeque<>();

    /**
     * Process the list of moving players.
     */
    public void updatePlayerMovement() {

        if (movingPlayers.isEmpty()) return;

        // Loop through all players and process movement.
        movingPlayers.forEach(player -> {

            // Process the players movement.
            if (player.getCountDownMovementTicks() == 0) {

                // If the entity has moved the required number of ticks, remove them.
                removePlayer(player);
            } else {

                //System.out.println("SPAM: Processing player movement");
                // Process number of ticks left to move
                player.processMovement();
            }
        });
    }

    /**
     * Adds a entity to the list of entities that need to be processed.
     *
     * @param player The entity ot add.
     */
    public void addPlayer(Player player, String mapName, int x, int y) {

        // Prevent the entity from being able to move while their movement is still processing.
        if (player.isMoving()) return;

        Location currentLocation = player.getLocation();
        Location futureLocation = new Location(mapName, currentLocation.getX() + x, currentLocation.getY() + y);

        System.out.println("Player trying to move to-> X: " + futureLocation.getX() + ", Y: " + futureLocation.getY());

        //TODO: Implement map switching. Update TMX Parser to read "TMX MAP OBJECTS (from Tiled Editor) to know what map to switch to. (See mirage realms map for example).

        // Check for out of bounds.
        if (futureLocation.isOutOfBounds()){
            System.out.println("Player attempted to move out of bounds.");

            // Tell the client to not move.
            new MoveReply(player, false, currentLocation).sendPacket();
            return;
        }

        // Check for collision. If the entity is colliding, then don't continue.
        if (!futureLocation.isTraversable()) {
            System.out.println("Player attempted to walk into a wall/object.");

            // Tell the client to not move.
            new MoveReply(player, false, currentLocation).sendPacket();
            return;
        }

        // Setup movement before the action happens.
        player.setupMovement(futureLocation);

        // Add the entity to the list of moving entities to be processed.
        movingPlayers.add(player);

        // Send all players on this map that this player has moved.
        System.out.println("Sending all players a movement update.");
        ValenguardMain.getInstance().getMapManager().sendAllMapPlayersEntityMoveUpdate(player);

        // Tell the client to move to the requested tile.
        new MoveReply(player, true, futureLocation).sendPacket();
    }

    /**
     * Removes a entity from the list of entities that need to be processed.
     * Then resets their movement and sends an update to all players of their
     * current location.
     *
     * @param player The entity to remove.
     */
    private void removePlayer(Player player) {
        // Remove from list so entity will not be updated.
        movingPlayers.remove(player);

        // Reset defaults
        player.resetMovement();
    }
}
