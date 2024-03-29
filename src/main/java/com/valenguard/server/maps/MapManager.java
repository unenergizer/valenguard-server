package com.valenguard.server.maps;

import com.valenguard.server.entity.Entity;
import com.valenguard.server.entity.Player;
import com.valenguard.server.network.listeners.server.out.EntityMoveUpdate;
import com.valenguard.server.network.listeners.server.out.PlayerMapChange;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MapManager {

    private static final String MAP_DIRECTORY = "src/main/resources/maps/";
    //private static final String MAP_DIRECTORY = "maps/";

    private Map<String, MapData> tmxMaps = new HashMap<>();

    public MapManager() {
        loadAllMaps();
    }

    /**
     * This will dynamically load all TMX maps for the game.
     *
     * @throws RuntimeException No maps were found.
     */
    private void loadAllMaps() {
        // Find all our maps.
        File dir = new File(MAP_DIRECTORY);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".tmx"));

        System.out.println(dir.getAbsolutePath());

        // Check to make sure we have some maps
        if (files == null) {
            throw new RuntimeException("No game maps were loaded.");
        }

        // Now load all of our maps
        for (File file : files) {
            String mapName = file.getName();
            tmxMaps.put(mapName, TmxParser.loadXMLFile(MAP_DIRECTORY, mapName));
        }
    }

    /**
     * Gets the map data associated with a map name. The map name is determined by
     * the file name of the TMX map file.
     *
     * @param mapName The name of the TMX map.
     * @return MapData that contains information about this map.
     * @throws RuntimeException Requested map could not be found or was not loaded.
     */
    public MapData getMapData(String mapName) throws RuntimeException {
        if (tmxMaps.containsKey(mapName)) {
            return tmxMaps.get(mapName);
        } else {
            throw new RuntimeException("Tried to get the map " + mapName + ", but it doesn't exist or was not loaded.");
        }
    }

    /**
     * Sends a location update to all players of a entity that moved.
     *
     * @param entityWhoMoved The entity that moved.
     */
    public void sendAllMapPlayersEntityMoveUpdate(Entity entityWhoMoved) {

        // Get all the players on the map.
        for (Player playersToUpdate : entityWhoMoved.getMapData().getPlayerList()) {

            // Don't send the player an update about themselves.
            if (playersToUpdate.equals(entityWhoMoved)) continue;

            // Send packets!
            new EntityMoveUpdate(playersToUpdate, entityWhoMoved).sendPacket();
        }
    }
}
