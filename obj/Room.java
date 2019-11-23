package obj;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
    private String owner;
    private int connectedCount;
    private ArrayList<String> names = new ArrayList<>();

    public Room() {
        connectedCount = 0;
    }

    public void newConnection(String name) {
        if (names.size() == 0) {
            this.owner = name;
            this.names.add(name);
        }
        else {
            this.names.add(name);
        }
        this.connectedCount += 1;
        System.out.println(name);
        System.out.println("size: " + names.size());
        System.out.println("owner: " + owner);
    }

    public String getOwner() {
        return owner;
    }

    public int getConnectedCount() {
        return connectedCount;
    }
}
