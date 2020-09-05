package pw.narumi.api.room;

import pw.narumi.api.user.User;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final User roomAdmin;
    private final List<User> connectedUsers = new ArrayList<>();

    private String roomName;
    private byte[] roomPassword;

    public Room(final String roomName, final User roomAdmin) {
        this.roomName = roomName;
        this.roomAdmin = roomAdmin;
    }

    public User getRoomAdmin() {
        return this.roomAdmin;
    }

    public List<User> getConnectedUsers() {
        return this.connectedUsers;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(final String roomName) {
        this.roomName = roomName;
    }

    public byte[] getRoomPassword() {
        return this.roomPassword;
    }

    public void setRoomPassword(final byte[] roomPassword) {
        this.roomPassword = roomPassword;
    }
}
