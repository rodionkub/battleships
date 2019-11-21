package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import obj.Room;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Main extends Application {
    public static ArrayList<Room> rooms = new ArrayList<>();
    public static String name = "ferr3t";
    private static Socket socket;
    private boolean trashRead = false;
    private static DataInputStream dis;
    private ObjectOutputStream out;

    @Override
    public void start(Stage primaryStage) throws Exception {
        rooms.add(new Room("ferr3t", 1));
        System.out.println(rooms.get(0));
        rooms.add(new Room("rodionkub", 1));
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new Controller());
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        socket = new Socket("localhost", 2000);
        dis = new DataInputStream(socket.getInputStream());
        OutputStream os = socket.getOutputStream();
        out = new ObjectOutputStream(os);
        out.writeObject(rooms.get(0));

        new Thread(() -> {
            while (true) {
                try {
                    if (!trashRead) {
                        dis.readInt();
                        trashRead = true;
                    }
                    if (dis.available() > 0) {
                        System.out.println(dis.readUTF());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        primaryStage.setTitle("Выбор комнаты");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void addNewRoom(Room room) {
        rooms.add(room);
    }

    private void removeRoom(Room room) {
        rooms.remove(room);
    }

    public void sendMessageToServer(String message) throws IOException {
        out.writeObject(message);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
