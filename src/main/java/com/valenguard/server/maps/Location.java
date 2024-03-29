package com.valenguard.server.maps;

import com.valenguard.server.ValenguardMain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Location {

    private String mapName;
    private int x;
    private int y;

    /**
     * Helper method to quickly get the map data for this location object.
     *
     * @return The map data that relates to this location object.
     */
    public MapData getMapData() {
        return ValenguardMain.getInstance().getMapManager().getMapData(mapName);
    }

    /**
     * Wrapper method to see if this current location is traversable.
     *
     * @return True if this tile can be walked on, false otherwise.
     */
    public boolean isTraversable() {
        return getMapData().isTraversable(x, y);
    }

    /**
     * Wrapper method to check if this location is going out of bounds of the map.
     *
     * @return True if the location is out of bounds, false otherwise.
     */
    public boolean isOutOfBounds() {
        return getMapData().isOutOfBounds(x, y);
    }

    /**
     * Adds two locations coordinates together.
     *
     * @param location The location to add to this.
     * @return A new location with added X and Y coordinates.
     */
    public Location add(Location location) {
        if (!location.getMapName().equals(mapName))
            throw new RuntimeException("Can't add locations. " + location.getMapName() + " doesn't equal " + mapName + ".");
        return new Location(mapName, this.x + location.getX(), this.y + location.getY());
    }
}
