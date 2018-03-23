package core.websocket.handshake;

public interface ClientHandshakeBuilder extends HandshakeBuilder, ClientHandshake {

    public void setResourceDescriptor(String resourceDescriptor);
}
