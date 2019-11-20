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

import java.util.ArrayList;

public class Main extends Application {
    public static ArrayList<Room> rooms = new ArrayList<>();
    public static String name = "ferr3t";

    @Override
    public void start(Stage primaryStage) throws Exception{
        rooms.add(new Room("ferr3t", 1));
        rooms.add(new Room("rodionkub", 1));
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new Controller());
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

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


    public static void main(String[] args) {
        launch(args);
    }
}
