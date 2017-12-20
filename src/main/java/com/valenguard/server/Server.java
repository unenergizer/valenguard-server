package com.valenguard.server;

import lombok.Setter;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class Server implements Runnable {

    private final int PORT = 3407;

    private ServerSocket serverSocket;
    private ServerEventBus eventBus = new ServerEventBus();
    private Consumer<ServerEventBus> registerListeners;

    private @Setter volatile boolean running = false;

    public void openServer(Consumer<ServerEventBus> registerListeners) {

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.registerListeners = registerListeners;
        new Thread(this, "Start").start();
    }

    @Override
    public void run() {
        System.out.println("Server opened on port: " + PORT);
        System.out.println("Please use the stop command to stop the server.");
        running = true;
        registerListeners.accept(eventBus);
        listenForConnections();
    }

    private void listenForConnections() {
        new Thread(() -> {
            while (running) {
                try {

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

    private void receivePackets(Socket clientSocket) {

        new Thread(() -> {
            ClientHandle clientHandle = null;
            try (
                    ObjectOutputStream outStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());
            ) {

                clientHandle = new ClientHandle(clientSocket, outStream, inStream);

                while (running) eventBus.publish(inStream.readChar(), clientHandle);

            } catch (IOException e) {

                if (e instanceof EOFException || e instanceof SocketException) {
                    if (clientHandle != null && running) {
                        // The client has logged out....

                    }
                } else {
                    e.printStackTrace();
                }

            } finally {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort()).start();
    }

    public void close() {
        running = false;
        System.out.println("Closing down server...");
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        // Double ensuring that that the server is closed
        close();
    }
}
