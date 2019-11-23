package serverMessages;

import obj.Room;

import java.io.Serializable;

public class newConnectionToRoom implements Serializable {
    private int roomIndex;
    private String name;

    public newConnectionToRoom(int roomIndex, String name) {
        this.roomIndex = roomIndex;
        this.name = name;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    public String getName() {
        return name;
    }
}
