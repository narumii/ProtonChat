package pw.narumi.api.user;

import pw.narumi.api.room.Room;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

public class User {

    private final Map<String, PrivateKey> decodeKeys = new HashMap<>();

    private Room connectedTo;

    private final String userName;
    private final long userId;

    private KeyPair keyPair;
    private String address;

    public User(final String userName, final long userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public Map<String, PrivateKey> getDecodeKeys() {
        return this.decodeKeys;
    }

    public Room getConnectedTo() {
        return this.connectedTo;
    }

    public void setConnectedTo(final Room connectedTo) {
        this.connectedTo = connectedTo;
    }

    public String getUserName() {
        return this.userName;
    }

    public long getUserId() {
        return this.userId;
    }

    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    public void setKeyPair(final KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }
}
