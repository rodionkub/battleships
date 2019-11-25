package serverMessages;

import obj.Room;

import java.io.Serializable;

public class newFieldSubmission implements Serializable {
    private Room room;
    private String name;
    private String field;

    public newFieldSubmission(Room room, String name, String field) {
        this.room = room;
        this.name = name;
        this.field = field;
    }

    public Room getRoom() {
        return room;
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }
}
