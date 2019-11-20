package obj;

public class Room {
    private String owner;
    private int connectedCount;

    public Room(String owner, int connectedCount) {
        this.owner = owner;
        this.connectedCount = connectedCount;
    }

    public String getOwner() {
        return owner;
    }

    public int getConnectedCount() {
        return connectedCount;
    }
}
