package com.valenguard.server.network;

import com.valenguard.server.ValenguardMain;
import com.valenguard.server.constants.ServerConstants;
import com.valenguard.server.util.ConsoleLogger;
import lombok.Setter;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class ServerConnection implements Runnable {

    private ServerSocket serverSocket;
    private ServerEventBus serverEventBus = new ServerEventBus();
    private Consumer<ServerEventBus> registerListeners;

    // Used to handle closing down all threads associated with
    // the server. Volatile allows the variable to exist
    // between threads
    @Setter
    private volatile boolean running = false;

    /**
     * Opens a server on a given socket and registers event listeners.
     *
     * @param registerListeners Listeners to listen to.
     */
    public void openServer(Consumer<ServerEventBus> registerListeners) {

        // Creates a socket to allow for communication between clients and the server.
        try {
            serverSocket = new ServerSocket(ServerConstants.SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // A callback for registering the listeners at a later time
        this.registerListeners = registerListeners;

        // Runs a thread for setting up
        new Thread(this, "Start").start();
    }

    /**
     * This is ran after the socket is setup. Request that the callback registers the listeners
     * that it needs and then calls a method to listen for incoming connections
     */
    @Override
    public void run() {
        System.out.println(ConsoleLogger.NETWORK.toString() + "Server opened on port: " + ServerConstants.SERVER_PORT);
        System.out.println(ConsoleLogger.INFO.toString() + "Please use the /stop command to stop the server.");
        running = true;
        registerListeners.accept(serverEventBus);
        listenForConnections();
    }

    /**
     * Listen for client connections and if possible establish
     * a link between the client and the server.
     */
    private void listenForConnections() {
        System.out.println(ConsoleLogger.NETWORK + "Listening for client connections...");

        // Creating a thread that runs as long as the server is alive and listens
        // for incoming connections
        new Thread(() -> {
            while (running) {
                try {

                    // Listeners for a new client socket to connect
                    // to the server and throws the socket to a "receeivePackets"
                    // method to be handled
                    receivePackets(serverSocket.accept());

                } catch (IOException e) {

                    if (e instanceof SocketException && !running) {
                        break;
                    }

                    e.printStackTrace();
                    // End application here
                }
            }
        }, "ConnectionListener").start();
    }

    /**
     * Start receiving packets for a new client connection.
     *
     * @param clientSocket A new client connection.
     */
    private void receivePackets(Socket clientSocket) {

        // This thread listens for incoming packets from the socket passed
        // to the method
        new Thread(() -> {
            ClientHandler clientHandler = null;
            // Using a new implementation in java that handles closing the streams
            // upon initialization. These streams are for sending and receiving data
            try (
                    ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream())
            ) {

                // Creating a new client handle that contains the necessary components for
                // sending and receiving data
                clientHandler = new ClientHandler(clientSocket, outStream, inStream);

                // Adding the client handle to a list of current client handles
                ValenguardMain.getInstance().getPlayerManager().onPlayerConnect(clientHandler);

                // Reading in a byte which represents an opcode that the client sent to the
                // server. Based on this opcode the event bus determines which listener should
                // be called
                while (running) serverEventBus.publish(inStream.readByte(), clientHandler);

            } catch (IOException e) {

                if (e instanceof EOFException || e instanceof SocketException) {
                    // The user has logged out of the server
                    if (clientHandler != null && running) {

                        // The player has disconnected
                        System.out.println(ConsoleLogger.NETWORK + clientSocket.getInetAddress().getHostAddress() + " has logged out.");
                        ValenguardMain.getInstance().getPlayerManager().onPlayerDisconnect(clientHandler);
                    }
                } else {
                    e.printStackTrace();
                }

            } finally {
                // Closing the client socket for cleanup
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } // Starting a new thread for the client in the format of address:port for the client and then starting the tread
        }, clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort()).start();
    }

    /**
     * Closing down the server's socket which means no more request from the clients may
     * be handled
     */
    public void close() {
        running = false;
        System.out.println(ConsoleLogger.NETWORK.toString() + "Closing down server...");
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensuring that the server's socket is closed down
     */
    @Override
    protected void finalize() {
        // Double ensuring that that the server is closed
        close();
    }
}
