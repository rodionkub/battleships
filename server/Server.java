package server;

import obj.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    public static ArrayList<Connection> clients = null;
    public static ArrayList<Room> rooms = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        int port = 2000;
        clients = new ArrayList<>();
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket client = serverSocket.accept();
            System.out.println(client);
            Connection connection = new Connection(client);
            clients.add(connection);
            new Thread(connection).start();
        }
    }
}
