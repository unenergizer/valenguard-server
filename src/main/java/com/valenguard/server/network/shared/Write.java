package com.valenguard.server.network.shared;

import java.io.IOException;
import java.io.ObjectOutputStream;

@FunctionalInterface
public interface Write {
    void accept(ObjectOutputStream outStream) throws IOException;
}
