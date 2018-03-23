package core.websocket.framing;

import core.websocket.exceptions.InvalidFrameException;

import java.nio.ByteBuffer;

public interface Framedata {

    public boolean isFin();

    public boolean getTransfereMasked();

    public Opcode getOpcode();

    public ByteBuffer getPayloadData();

    public abstract void append(Framedata nextframe) throws InvalidFrameException;

    public enum Opcode {

        CONTINUOUS,
        TEXT,
        BINARY,
        PING,
        PONG,
        CLOSING
        // more to come
    }
}
