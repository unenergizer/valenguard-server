package com.valenguard.server.network;

import com.valenguard.server.network.shared.Write;
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

    @FunctionalInterface
    private interface Reader {
        void accept() throws IOException;
    }

    public String readString() {
        try {
            return inStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int readInt() {
        readIn(() -> inStream.readInt());
        return 0;
    }

    public int readChar() {
        readIn(() -> inStream.readChar());
        return 0;
    }

    public void readIn(Reader reader) {
        try {
            reader.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public byte readByte() {
        try {
            return inStream.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0x0;
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
