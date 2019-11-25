package server;

import obj.Room;
import serverMessages.AttackMessage;
import serverMessages.NewConnectionToRoom;
import serverMessages.NewFieldSubmission;

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
                    else if (input instanceof NewConnectionToRoom) {
                        Room room = Server.rooms.get(((NewConnectionToRoom)input).getRoomIndex());
                        room.newConnection(((NewConnectionToRoom) input).getName(), client);
                    }
                    else if (input instanceof NewFieldSubmission) {
                        String name = ((NewFieldSubmission) input).getName();
                        String field = ((NewFieldSubmission) input).getField();

                        Room foundRoom = null;
                        int nameIndex = -1;

                        for (Room room: Server.rooms) {
                            if (foundRoom == null) {
                                for (int i = 0; i < room.getNames().size(); i++) {
                                    if (name.equals(room.getNames().get(i))) {
                                        foundRoom = room;
                                        nameIndex = i;
                                    }
                                }
                            }
                        }
                        String allReady = Server.rooms.get(Server.rooms.indexOf(foundRoom)).submitField(name, field);
                        if (allReady.equals("ready")) {
                            for (Connection conn : Server.clients) {
                                if (conn.getSocket() == foundRoom.getClients().get(1 - nameIndex)) {
                                    conn.getObjectOut().writeObject("ready");
                                }
                            }
                        }
                        objectOut.writeObject(allReady);
                    }
                    else if (input instanceof AttackMessage) {
                        String turnName = ((AttackMessage) input).getTurnName();
                        int attackIndex = ((AttackMessage) input).getAttackIndex();

                        Room foundRoom = null;
                        int nameIndex = -1;

                        for (Room room: Server.rooms) {
                            if (foundRoom == null) {
                                for (int i = 0; i < room.getNames().size(); i++) {
                                    if (turnName.equals(room.getNames().get(i))) {
                                        foundRoom = room;
                                        nameIndex = i;
                                    }
                                }
                            }
                        }

                        String field = foundRoom.getField(1 - nameIndex);
                        if (field.charAt(attackIndex) == '1') {
                            objectOut.writeObject("hit");
                        }
                        else {
                            objectOut.writeObject("miss");
                        }
                        for (Connection conn : Server.clients) {
                            if (conn.getSocket() == foundRoom.getClients().get(1 - nameIndex)) {
                                conn.getObjectOut().writeObject("new hit on " + attackIndex);
                            }
                        }
                    }
                    else if (input instanceof String) {
                        if (input.toString().equals("getRooms()")) {
                            objectOut.writeObject(Server.rooms);
                        }
                        else if (input.toString().contains("dead")) {
                            String name = input.toString().split(" ")[0];
                            int nameIndex = -1;
                            Room foundRoom = null;
                            for (Room room: Server.rooms) {
                                if (foundRoom == null) {
                                    for (int i = 0; i < room.getNames().size(); i++) {
                                        if (name.equals(room.getNames().get(i))) {
                                            foundRoom = room;
                                            nameIndex = i;
                                        }
                                    }
                                }
                            }
                            for (Connection conn : Server.clients) {
                                if (conn.getSocket() == foundRoom.getClients().get(1 - nameIndex)) {
                                    conn.getObjectOut().writeObject("hit");
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    conn.getObjectOut().writeObject("victory");
                                }
                            }
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
