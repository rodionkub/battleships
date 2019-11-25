package server;

import obj.Room;
import serverMessages.newConnectionToRoom;
import serverMessages.newFieldSubmission;

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

                    if (input instanceof Room) {
                        Server.rooms.add((Room)input);
                    }
                    else if (input instanceof newConnectionToRoom) {
                        Room room = Server.rooms.get(((newConnectionToRoom)input).getRoomIndex());
                        room.newConnection(((newConnectionToRoom) input).getName(), client);
                    }
                    else if (input instanceof newFieldSubmission) {
                        String name = ((newFieldSubmission) input).getName();
                        String field = ((newFieldSubmission) input).getField();

                        Room foundRoom = null;

                        for (Room room: Server.rooms) {
                            if (foundRoom == null) {
                                for (String nameInRoom : room.getNames()) {
                                    if (name.equals(nameInRoom)) {
                                        foundRoom = room;
                                    }
                                }
                            }
                        }
                        String allReady = Server.rooms.get(Server.rooms.indexOf(foundRoom)).submitField(name, field);
                        if (allReady.equals("ready")) {
                            for (Connection conn : Server.clients) {
                                if (conn.getSocket() == foundRoom.getClients().get(0)) {
                                    System.out.println("writing to " + foundRoom.getClients().get(0));
                                    conn.getObjectOut().writeObject("ready");
                                }
                            }
                        }
                        objectOut.writeObject(allReady);
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

    public ObjectOutputStream getObjectOut() {
        return objectOut;
    }

    public Socket getSocket() {
        return client;
    }
}
