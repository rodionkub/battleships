package game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import sample.Main;
import serverMessages.AttackMessage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML
    private GridPane allyGridPane;
    @FXML
    private GridPane enemyGridPane;
    @FXML
    private Label turnLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCells();
        setGridActions();
        setAllyGridPane();
        Main.allyGridPane = allyGridPane;
        try {
            initLabel();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initLabel() throws IOException, ClassNotFoundException {
    }

    private void addCells() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Pane pane = new Pane();
                GridPane.setColumnIndex(pane, i);
                GridPane.setRowIndex(pane, j);
                allyGridPane.getChildren().add(pane);
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Pane pane = new Pane();
                GridPane.setColumnIndex(pane, i);
                GridPane.setRowIndex(pane, j);
                enemyGridPane.getChildren().add(pane);
            }
        }
    }

    private void setAllyGridPane() {
        String field = Main.field;
        for (int i = 1; i < allyGridPane.getChildren().size(); i++) {
            Node node = allyGridPane.getChildren().get(i);
            if (field.charAt(i - 1) == '1') {
                node.setStyle("-fx-background-color: green");
            }
        }
    }

    private void setGridActions() {
        for (int i = 0; i < enemyGridPane.getChildren().size(); i++) {
            Node node = enemyGridPane.getChildren().get(i);
            node.setOnMouseEntered(e -> {
                if (!node.getStyle().contains("red")) {
                    node.setStyle("-fx-background-color: red");
                }
            });
            node.setOnMouseExited(e -> {
                if (!node.getStyle().contains("black") && (!node.getStyle().contains("grey"))) {
                    node.setStyle("-fx-background-color: default");
                }
            });
            node.setOnMouseClicked(e -> {
                try {
                    String reply = (String) Main.sendReturnableMessage(new AttackMessage(Main.name, enemyGridPane.getChildren().indexOf(node) - 1));
                    if (reply.equals("hit")) {
                        node.setStyle("-fx-background-color: black");
                    }
                    else {
                        node.setStyle("-fx-background-color: grey");
                    }
                } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }
}
