package obj;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Room implements Serializable {
    private String owner;
    private int connectedCount;
    private ArrayList<String> names = new ArrayList<>();
    private transient ArrayList<Socket> clients = new ArrayList<>();
    private ArrayList<String> playerFields = new ArrayList<>();

    public Room() {
        connectedCount = 0;
    }

    public void newConnection(String name, Socket client) {
        if (names.size() == 0) {
            owner = name;
        }
        names.add(name);
        clients.add(client);
        playerFields.add("");
        connectedCount += 1;
    }

    public ArrayList<Socket> getClients() {
        return clients;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public String submitField(String name, String field) {
        System.out.println(name + " " + field);
        playerFields.set(names.indexOf(name), field);
        if (!playerFields.get(1 - names.indexOf(name)).equals("")) {
            return "ready";
        }
        return "not";
    }

    public String getOwner() {
        return owner;
    }

    public int getConnectedCount() {
        return connectedCount;
    }
}
