package com.valenguard.server;

import com.valenguard.server.shared.Write;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@AllArgsConstructor
@Getter
public class ClientHandle {
    private Socket clientSocket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public String readString() {
        try {
            return inStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(char opcode, Write writeCallback) {
        try {
            outStream.writeChar(opcode);
            writeCallback.accept(outStream);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
