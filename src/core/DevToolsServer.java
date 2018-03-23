package core;


import com.sun.javafx.scene.web.Debugger;
import core.websocket.WebSocket;
import core.websocket.handshake.ClientHandshake;
import core.websocket.server.WebSocketServer;
import javafx.application.Platform;
import javafx.util.Callback;

import java.net.InetSocketAddress;
import java.util.LinkedList;

public class DevToolsServer extends WebSocketServer implements Callback<String, Void> {

    private final Debugger debugger;
    private final LinkedList<String> sendQueue = new LinkedList<>();
    private WebSocket conn;

    public DevToolsServer(Debugger debugger) {
        super(new InetSocketAddress("127.0.0.1", 51742));
        this.debugger = debugger;
        start();

        debugger.setEnabled(true);
        debugger.sendMessage("{\"id\" : -1, \"method\" : \"Network.enable\"}");
        debugger.setMessageCallback(this);

    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        this.conn = conn;
        System.out.println("Połączone");

        synchronized (sendQueue) {
            for (String s : sendQueue)
                conn.send(s);
            sendQueue.clear();
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Rozłączone");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Platform.runLater(() -> {
            debugger.sendMessage(message);
        });
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public Void call(String data) {
        if (conn == null)
            synchronized (sendQueue) {
                sendQueue.add(data);
                while (sendQueue.size() > 1000)
                    sendQueue.pollFirst();
            }
        else
            conn.send(data);

        return null;
    }
}
