package server;

import obj.Room;

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
                        System.out.println("i got the room, bro! its " + ((Room)input).getOwner() + "'s room!");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
