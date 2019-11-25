package sample;

import battleship.BattleshipBoardController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import obj.Room;
import serverMessages.AttackMessage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    public static String name;
    private Socket socket;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    public static Room room;
    public static String field;
    public static Window window;
    private static boolean firstTime = false;
    private static Object returnedMessage;
    public static GridPane allyGridPane;
    public static GridPane enemyGridPane;
    public static Label turnLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        name = "user" + new Random().nextInt(1000);
        socket = new Socket("localhost", 2000);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new Controller());
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);


        new Thread(() -> {
            while (true) {
                try {
                    Object input;
                    input = in.readObject();
                    if (input instanceof ArrayList) {
                        returnedMessage = input;
                        continue;
                    }
                    returnedMessage = input;
                    if (input.equals("ready")) {
                        goToBattleWindow();
                    }
                    else if (((String)input).contains("new hit on")) {
                        attackedOn(Integer.parseInt(((String)input).split(" ")[3]));
                    }
                    else if (input.equals("victory")) {
                        victoryBlock();
                        Platform.runLater(() -> {
                            turnLabel.setText("Вы победили!");
                        });
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

    public static Object sendReturnableMessage(Object message) throws IOException, ClassNotFoundException, InterruptedException {
        if (!firstTime) {
            firstTime = true;
            out.writeObject(message);
            return in.readObject();
        }
        out.writeObject(message);
        Thread.sleep(100);
        return returnedMessage;
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

    private static void attackedOn(int attackedIndex) throws IOException {
        Node attackedNode = allyGridPane.getChildren().get(attackedIndex + 1);
        if (attackedNode.getStyle().contains("green")) {
            attackedNode.setStyle("-fx-background-color: black");
        }
        else if (!attackedNode.getStyle().contains("color")) {
            attackedNode.setStyle("-fx-background-color: grey");
        }
        unblock();
        if (!checkIfAnyAlive()) {
            victoryBlock();
            Platform.runLater(() -> turnLabel.setText("Все ваши корабли подбиты. Вы проиграли."));
            sendMessageToServer(name + " dead");
        }
    }

    private static boolean checkIfAnyAlive() {
        for (Node node: allyGridPane.getChildren()) {
            if (node.getStyle().contains("green")) {
                return true;
            }
        }
        return false;
    }
    public static void unblock() {
        Platform.runLater(() -> turnLabel.setText("Ваш ход"));
        for (Node node: enemyGridPane.getChildren()) {
            node.setOnMouseClicked(e -> {
                try {
                    String reply = (String) Main.sendReturnableMessage(new AttackMessage(Main.name, enemyGridPane.getChildren().indexOf(node) - 1));
                    if (reply.equals("hit")) {
                        node.setStyle("-fx-background-color: black");
                    } else {
                        node.setStyle("-fx-background-color: grey");
                    }
                    blockGrid();
                } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    private static void blockGrid() {
        Platform.runLater(() -> turnLabel.setText("Ход оппонента"));
        for (Node node: enemyGridPane.getChildren()) {
            node.setOnMouseClicked(e -> {});
        }
    }

    private static void victoryBlock() {
        for (Node node: enemyGridPane.getChildren()) {
            node.setOnMouseClicked(e -> {});
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
