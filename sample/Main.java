package sample;

import battleship.BattleshipBoardController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import obj.Room;

import java.io.*;
import java.net.Socket;

public class Main extends Application {
    public static String name = "ferr3t";
    private static Socket socket;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    public static Room room;
    public static String field;
    public static Window window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new Controller());
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);


        new Thread(() -> {
            while (true) {
                try {
                    String input = (String) in.readObject();
                    System.out.println(input);
                    if (input.equals("ready")) {
                        goToBattleWindow();
                    }
                } catch (IOException | ClassNotFoundException e) {
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
        return in.readObject();
    }

    public static void goToBattleWindow() {
        Platform.runLater(
                () -> {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(BattleshipBoardController.class.getResource("/game/game.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Stage stage = new Stage();
                    stage.setTitle("Поле боя");
                    stage.setScene(new Scene(root));
                    stage.show();
                    window.getScene().getWindow().hide();
                }
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
