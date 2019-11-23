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
    public static String name = "ferr3t";
    private static Socket socket;
    private boolean trashRead = false;
    private static DataInputStream dis;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new Controller());
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        InputStream is = socket.getInputStream();
        dis = new DataInputStream(is);

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


    public static void sendMessageToServer(Object message) throws IOException {
        out.writeObject(message);
    }

    public static Object sendReturnableMessage(Object message) throws IOException, ClassNotFoundException {
        socket = new Socket("localhost", 2000);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(message);
        in = new ObjectInputStream(socket.getInputStream());
        Object input = in.readObject();
        System.out.println("found: " + input);
        return input;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
