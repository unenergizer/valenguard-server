package com.valenguard.server.maps;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TmxParser {

    /**
     * This takes in a TMX map and gets the collision elements from it and builds a collision
     * array for checking entity collision server side.
     *
     * @param directory The directory that contains this map.
     * @param fileName  The name of the TMX map file.
     * @return A map data class with information about this map.
     */
    public static MapData loadXMLFile(String directory, String fileName) {
        int mapWidth, mapHeight;

        // Lets get the document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document document = null;
        try {
            document = builder.parse(directory + fileName);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the first element
        Element tmx = document.getDocumentElement();
        mapWidth = Integer.parseInt(tmx.getAttributes().getNamedItem("width").getNodeValue());
        mapHeight = Integer.parseInt(tmx.getAttributes().getNamedItem("height").getNodeValue());

        Tile map[][] = new Tile[mapWidth][mapHeight];

        // Loop through layers
        NodeList layers = tmx.getElementsByTagName("layer");

        for (int i = 0; i < layers.getLength(); i++) {

            // Get collision layer
            if (((Element) layers.item(i)).getAttribute("name").equals("collision")) {

                // Get collision data
                NodeList collisionData = ((Element) layers.item(i)).getElementsByTagName("data");

                // Get Array of tiles
                String[] tiles = collisionData.item(0).getTextContent().split(",");

                // Loop through all tiles
                // Start iteration from top left
                int currentY = mapHeight - 1; // using map height instead of zero, because we are starting at top.
                int currentX = 0; // using 0 because we are starting from the left

                for (String tile : tiles) {

                    tile = tile.trim();

                    // Tile ID
                    int tileType = Integer.parseInt(tile);

                    // Basic coordinate system of our tiled map.
                    // Iteration starts at top left.
                    // So if map height is 50, we start fromY at 50
                    // and then count down (moving down the y access).
                    // Since X starts on the left, we will start it at 0.
                    //
                    // [X,Y]
                    //
                    //////////////////////////////////////
                    // [0,5] [1,5] [2,5] [3,5] [4,5] [5,5]
                    // [0,4] [1,4] [2,4] [3,4] [4,4] [5,4]
                    // [0,3] [1,3] [2,3] [3,3] [4,3] [5,3]
                    // [0,2] [1,2] [2,2] [3,2] [4,2] [5,2]
                    // [0,1] [1,1] [2,1] [3,1] [4,1] [5,1]
                    // [0,0] [1,0] [2,0] [3,0] [4,0] [5,0]

                    // Initializing the instance of the new tile
                    map[currentX][currentY] = new Tile();

                    // Check for tile ID and add it to collision map
                    if (tileType != 0) {
                        map[currentX][currentY].setTraversable(false); // Not traversable
                        System.out.print("#");
                    } else {
                        map[currentX][currentY].setTraversable(true); // Is traversable
                        System.out.print(" ");
                    }

                    // Increment x horizontal value
                    currentX++;

                    // Check for end of map width
                    if (currentX == mapWidth) {
                        System.out.println("");
                        currentX = 0; // reset x counter
                        currentY--; // decrement y value
                    }
                }
            }
        }
        return new MapData(fileName, mapWidth, mapHeight, map);
    }
}
