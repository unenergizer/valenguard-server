package com.valenguard.server;

import com.valenguard.server.shared.Listener;
import com.valenguard.server.shared.Opcode;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ServerEventBus {

    @AllArgsConstructor
    private class CallbackData {
        private Listener listener;
        private Method method;
    }

    private Map<Character, CallbackData> listeners = new HashMap<>();

    public void registerListener(Listener listener) {
        for (Method method : listener.getClass().getMethods()) {
            for (Opcode opcodeAnno : method.getAnnotationsByType(Opcode.class)) {
                Class<?>[] params = method.getParameterTypes();
                String error = "Listener: " + listener;
                if (params.length != 1)
                    throw new RuntimeException(error + " must have 1 parameters.");
                if (!params[0].equals(ClientHandle.class))
                    throw new RuntimeException(error + " first parameter must be of type ClientHandle");
                listeners.put(opcodeAnno.getOpcode(), new CallbackData(listener, method));
            }
        }
    }

    public void publish(char opcode, ClientHandle clientHandle) {
        CallbackData callbackData = listeners.get(opcode);
        if (callbackData == null) {
            System.out.println("Callback data was null");
            return;
        }
        try {
            callbackData.method.invoke(callbackData.listener, clientHandle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
