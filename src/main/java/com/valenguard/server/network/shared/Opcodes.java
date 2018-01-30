package com.valenguard.server.network.shared;

public class Opcodes {

    /**
     * DONT FORGET TO ADD YOUR NETWORK LISTENERS!!!!!!!!!!
     */

    public static final byte INIT_PLAYER_CLIENT = 0x00;
    public static final byte MOVE_REQUEST = 0x01;
    public static final byte MOVE_REPLY = 0x02;
    public static final byte ENTITY_MOVE_UPDATE = 0x03;
    public static final byte ENTITY_JOINED_MAP = 0x04;
    public static final byte ENTITY_EXIT_MAP = 0x05;
    public static final byte PLAYER_MAP_CHANGE = 0x06;
}
