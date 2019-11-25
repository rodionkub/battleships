package battleship;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import sample.Main;
import serverMessages.NewFieldSubmission;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BattleshipBoardController implements Initializable {

    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView battleship1ImageView;
    @FXML
    private ImageView battleship2ImageView;
    @FXML
    private ImageView battleship3ImageView;
    @FXML
    private ImageView battleship4ImageView;
    @FXML
    private Label battleship1Label;
    @FXML
    private Label battleship2Label;
    @FXML
    private Label battleship3Label;
    @FXML
    private Label battleship4Label;
    @FXML
    private AnchorPane window;
    @FXML
    private Button readyButton;
    private double startDragX;
    private double startDragY;
    private ArrayList<Rectangle> rectangles = new ArrayList<>();
    private ArrayList<Battleship> battleships = new ArrayList<>();
    private ArrayList<Label> labels = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCells();
        setGridActions();
        setReadyButtonOnClick();
        initializeBattleships();
        setArrayOfLabels();
        for (Battleship battleship : battleships) {
            setImageOnDrag(battleship);
        }
    }

    private void setArrayOfLabels() {
        labels.add(battleship1Label);
        labels.add(battleship2Label);
        labels.add(battleship3Label);
        labels.add(battleship4Label);
    }

    private void initializeBattleships() {
        battleships.add(new Battleship(battleship1ImageView, 4, 1));
        battleships.add(new Battleship(battleship2ImageView, 3, 2));
        battleships.add(new Battleship(battleship3ImageView, 2, 3));
        battleships.add(new Battleship(battleship4ImageView, 1, 4));
    }

    private void setGridActions() {
        for (int i = 0; i < gridPane.getChildren().size(); i++) {
            Node node = gridPane.getChildren().get(i);
            double x = 28 + (i / 10) * 30;
            double y = 37 + (i % 10) * 30;
            rectangles.add(new Rectangle(x, y, 30, 30));
            node.setOnMouseEntered(e -> {
                if (!node.getStyle().contains("green")) {
                    node.setStyle("-fx-background-color: black");
                }
            });
            node.setOnMouseExited(e -> {
                if (!node.getStyle().contains("green")) {
                    node.setStyle("-fx-background-color: default");
                }
            });
            node.setOnMouseClicked(e -> System.out.println(GridPane.getColumnIndex(node) + " " + GridPane.getRowIndex(node)));
        }
    }

    private void setImageOnDrag(Battleship battleship) {
        ImageView battleshipCopy = new ImageView(battleship.getImageView().getImage());
        battleshipCopy.setFitWidth(30 * battleship.getLength());
        battleshipCopy.setFitHeight(30);

        battleship.getImageView().setOnMousePressed(e -> {
            if (battleship.getLeft() > 0) {
                battleshipCopy.setLayoutX(battleship.getImageView().getLayoutX());
                battleshipCopy.setLayoutY(battleship.getImageView().getLayoutY());
                window.getChildren().add(battleshipCopy);
                startDragX = e.getSceneX();
                startDragY = e.getSceneY();
            }
        });

        battleship.getImageView().setOnMouseDragged(e -> {
            battleshipCopy.setTranslateX(e.getSceneX() - startDragX);
            battleshipCopy.setTranslateY(e.getSceneY() - startDragY);
        });

        battleship.getImageView().setOnMouseReleased(e -> {
            if (battleship.getLeft() > 0) {
                int i = -1;
                for (Rectangle rect : rectangles) {
                    if (rect.contains(e.getSceneX(), e.getSceneY())) {
                        i = rectangles.indexOf(rect);
                    }
                }
                if (i != -1) {
                    if (i + (battleship.getLength() - 1) * 10 <= 100) {
                        if (isPlaceable(i + 1, battleship.getLength())) {
                            for (int j = 0; j < battleship.getLength(); j++) {
                                gridPane.getChildren().get(i + 1 + j * 10).setStyle("-fx-background-color: green");
                            }
                            battleship.used();
                            labels.get(battleships.indexOf(battleship)).setText(battleship.getLeft() + "x");
                            if (allShipsSet()) {
                                readyButton.setText("Готово. В бой!");
                                readyButton.setDisable(false);
                            }
                        }
                    }
                }
                window.getChildren().remove(battleshipCopy);
            }
        });
    }

    private boolean isPlaceable(int i, int length) {
        for (int j = i; j < i + length * 10; j += 10) {
            if (j % 10 != 0 && j % 10 != 1) {
                if (!((j - 1 <= 0 || !gridPane.getChildren().get(j - 1).getStyle().contains("green")) &&
                        (j + 1 > 100 || !gridPane.getChildren().get(j + 1).getStyle().contains("green")) &&
                        (j - 10 <= 0 || !gridPane.getChildren().get(j - 10).getStyle().contains("green")) &&
                        (j + 10 > 100 || !gridPane.getChildren().get(j + 10).getStyle().contains("green")) &&
                        (j + 11 > 100 || !gridPane.getChildren().get(j + 11).getStyle().contains("green")) &&
                        (j + 9 > 100 || !gridPane.getChildren().get(j + 9).getStyle().contains("green")) &&
                        (j - 11 <= 0 || !gridPane.getChildren().get(j - 11).getStyle().contains("green")) &&
                        (j - 9 <= 0 || !gridPane.getChildren().get(j - 9).getStyle().contains("green")))) {
                    return false;
                }
            } else if (j == 1) {
                if (!(!gridPane.getChildren().get(2).getStyle().contains("green") &&
                        !gridPane.getChildren().get(11).getStyle().contains("green") &&
                        !gridPane.getChildren().get(12).getStyle().contains("green"))) {
                    return false;
                }
            } else if (j == 10) {
                if (!(!gridPane.getChildren().get(9).getStyle().contains("green") &&
                        !gridPane.getChildren().get(19).getStyle().contains("green") &&
                        !gridPane.getChildren().get(20).getStyle().contains("green"))) {
                    return false;
                }
            } else if (j == 91) {
                if (!(!gridPane.getChildren().get(81).getStyle().contains("green") &&
                        !gridPane.getChildren().get(82).getStyle().contains("green") &&
                        !gridPane.getChildren().get(92).getStyle().contains("green"))) {
                    return false;
                }
            } else if (j == 100) {
                if (!(!gridPane.getChildren().get(89).getStyle().contains("green") &&
                        !gridPane.getChildren().get(99).getStyle().contains("green") &&
                        !gridPane.getChildren().get(90).getStyle().contains("green"))) {
                    return false;
                }
            } else if (j % 10 == 0) {
                if (!(!gridPane.getChildren().get(j - 1).getStyle().contains("green") &&
                        !gridPane.getChildren().get(j - 10).getStyle().contains("green") &&
                        !gridPane.getChildren().get(j + 10).getStyle().contains("green") &&
                        !gridPane.getChildren().get(j - 11).getStyle().contains("green") &&
                        !gridPane.getChildren().get(j + 9).getStyle().contains("green"))) {
                    return false;
                }
            } else { // if j % 10 == 1
                if (!(!gridPane.getChildren().get(j + 1).getStyle().contains("green") &&
                        !gridPane.getChildren().get(j - 10).getStyle().contains("green") &&
                        !gridPane.getChildren().get(j + 10).getStyle().contains("green") &&
                        !gridPane.getChildren().get(j + 11).getStyle().contains("green") &&
                        !gridPane.getChildren().get(j - 9).getStyle().contains("green"))) {
                    return false;
                }
            }
        }
        return true;
    }


    private void addCells() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Pane pane = new Pane();
                GridPane.setColumnIndex(pane, i);
                GridPane.setRowIndex(pane, j);
                gridPane.getChildren().add(pane);
            }
        }
    }

    private boolean allShipsSet() {
        return true;
//        return battleship1Label.getText().equals("0x") &&
//                battleship2Label.getText().equals("0x") &&
//                battleship3Label.getText().equals("0x") &&
//                battleship4Label.getText().equals("0x");
    }

    private void setReadyButtonOnClick() {
        readyButton.setOnMouseClicked(e -> {
            Main.window = window.getScene().getWindow();
            String allReady = null;
            String field = "";
            for (int i = 1; i < gridPane.getChildren().size(); i++) {
                Node node = gridPane.getChildren().get(i);
                if (node.getStyle().contains("green")) {
                    field = field.concat("1");
                }
                else {
                    field = field.concat("0");
                }
            }
            Main.field = field;
            try {
                allReady = (String) Main.sendReturnableMessage((new NewFieldSubmission(Main.name, field)));
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                ex.printStackTrace();
            }
            if (allReady.equals("not")) {
                readyButton.setDisable(true);
                readyButton.setText("Ждем, оппонента...");
            }
        });
    }
}
