package com.valenguard.server;

import com.valenguard.server.commands.CommandProcessor;
import com.valenguard.server.commands.ConsoleCommandManager;
import com.valenguard.server.entity.PlayerManager;
import com.valenguard.server.maps.MapManager;
import com.valenguard.server.network.ServerConnection;
import com.valenguard.server.network.listeners.server.in.PingIn;
import com.valenguard.server.network.listeners.server.out.MoveReply;
import com.valenguard.server.network.listeners.server.in.MoveRequest;
import com.valenguard.server.util.ConsoleLogger;
import lombok.Getter;

@Getter
public class ValenguardMain {

    private static ValenguardMain instance = null;
    private MapManager mapManager;
    private CommandProcessor commandProcessor;
    private ServerConnection serverConnection;
    private ServerLoop serverLoop;
    private PlayerManager playerManager;
    private volatile boolean isOnline = false;

    /**
     * Singleton implementation of the main class.
     *
     * @return a static instance of this class.
     */
    public static ValenguardMain getInstance() {
        if (instance == null) instance = new ValenguardMain();
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(ConsoleLogger.SERVER.toString() + "Booting Valenguard Server!");
        ValenguardMain.getInstance().start();
    }

    /**
     * Starts all server processes.
     */
    private void start() {
        isOnline = true;

        mapManager = new MapManager();

        // start server loop
        serverLoop = new ServerLoop();
        serverLoop.start();

        // register commands
        registerCommands();

        playerManager = new PlayerManager();

        // start serverConnection code
        initializeNetwork();

//        // TODO: MOVE OR REMOVE....
//        Scanner scanner = new Scanner(System.in);
//        String input;
//        while (true) {
//            input = scanner.nextLine();
//            if (input.equalsIgnoreCase("/stop")) break;
//        }
//        scanner.close();
//        serverConnection.close();
    }

    /**
     * Initializes the network. Starts listening for connection
     * and registers network event listeners.
     */
    private void initializeNetwork() {
        System.out.println(ConsoleLogger.NETWORK.toString() + "Initializing network...");
        serverConnection = new ServerConnection();
        serverConnection.openServer((eventBus) -> {
            eventBus.registerListener(new MoveRequest());
            eventBus.registerListener(new PingIn());
        });
    }

    /**
     * Register server commands.
     */
    private void registerCommands() {
        System.out.println(ConsoleLogger.SERVER.toString() + "Registering commands.");
        commandProcessor = new CommandProcessor();

        // console command manager
        new ConsoleCommandManager().start();
    }

    /**
     * Stops server operations and terminates the program.
     */
    public void stop() {
        System.out.println(ConsoleLogger.SERVER.toString() + "ServerConnection shutdown initialized!");

        isOnline = false; // this will stop the server loop

        //TODO: Stop network functions and shut down nicely.
        serverConnection.close();

        System.out.println(ConsoleLogger.SERVER.toString() + "ServerConnection shutdown complete!");
    }
}
