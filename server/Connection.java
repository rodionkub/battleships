package server;

import obj.Room;
import serverMessages.newConnectionToRoom;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable {
    private DataInputStream in;
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private DataOutputStream textOut;
    private Socket client;

    public Connection(Socket client) throws IOException {
        this.client = client;

        InputStream is = client.getInputStream();
        this.in = new DataInputStream(is);
        this.objectIn = new ObjectInputStream(is);

        OutputStream os = client.getOutputStream();
        this.objectOut = new ObjectOutputStream(os);
        this.textOut = new DataOutputStream(os);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (in.available() > 0) {
                    Object input = objectIn.readObject();
                    System.out.println(input);

                    if (input instanceof Room) {
                        Server.rooms.add((Room)input);
                        System.out.println("i got the room, bro! its " + ((Room)input).getOwner() + "'s room!");
                    }
                    else if (input instanceof newConnectionToRoom) {
                        Room room = Server.rooms.get(((newConnectionToRoom)input).getRoomIndex());
                        room.newConnection(((newConnectionToRoom) input).getName());
                    }
                    else if (input instanceof String) {
                        if (input.toString().equals("getRooms()")) {
                            objectOut.writeObject(Server.rooms);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
