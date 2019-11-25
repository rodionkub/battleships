package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import obj.Room;
import serverMessages.NewConnectionToRoom;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private VBox vBox;
    @FXML private Button updateButton;
    @FXML private Button nameUpdateButton;
    @FXML private Label nameLabel;
    @FXML private TextField nameField;
    @FXML private Pane mainPane;
    private ArrayList<Room> rooms;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            updateRooms();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        updateName();
        setNameOnClick();
        setRoomOnClick();
        setUpdateOnClick();
    }

    private void updateRooms() throws IOException, ClassNotFoundException, InterruptedException {
        Node node = vBox.getChildren().get(0);
        vBox.getChildren().clear();
        vBox.getChildren().add(node);
        rooms = (ArrayList<Room>) Main.sendReturnableMessage("getRooms()");
        for (Room roomObject: rooms) {
            if (roomObject.getConnectedCount() < 2) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("room.fxml"));
                Pane roomTemplate = null;
                try {
                    roomTemplate = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Pane newRoom = new Pane();
                newRoom.getChildren().addAll(roomTemplate.getChildren());
                ((Label) newRoom.getChildren().get(1)).setText(roomObject.getOwner());
                ((Label) newRoom.getChildren().get(3)).setText(roomObject.getConnectedCount() + "/2");
                vBox.getChildren().add(newRoom);
            }
        }
    }

    private void setRoomOnClick() {
        for (Node node: vBox.getChildren()) {
            node.setOnMouseClicked(e -> {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/battleship/battleshipBoard.fxml"));
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
                mainPane.getScene().getWindow().hide();

                int clickedRoomIndex = vBox.getChildren().indexOf(node);
                String name = nameLabel.getText();

                Main.room = rooms.get(clickedRoomIndex - 1);
                try {
                    Main.sendMessageToServer(new NewConnectionToRoom(clickedRoomIndex - 1, name));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            });
        }
    }

    private void updateName() {
        String name = Main.name;
        nameLabel.setText(name);
    }

    private void updateName(String name) {
        Main.name = name;
        nameLabel.setText(name);
    }

    private void setNameOnClick() {
        nameUpdateButton.setOnMouseClicked(e -> updateName(nameField.getText()));
    }

    private void setUpdateOnClick() {
        updateButton.setOnMouseClicked(e -> {
            try {
                updateRooms();
                setRoomOnClick();
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }
}
