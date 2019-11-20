package battleship;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BattleshipBoardController implements Initializable {

    @FXML private GridPane gridPane;
    @FXML private ImageView battleship1ImageView;
    @FXML private ImageView battleship2ImageView;
    @FXML private ImageView battleship3ImageView;
    @FXML private ImageView battleship4ImageView;
    @FXML private Label battleship1Label;
    @FXML private Label battleship2Label;
    @FXML private Label battleship3Label;
    @FXML private Label battleship4Label;
    @FXML private AnchorPane window;
    private double startDragX;
    private double startDragY;
    private ArrayList<Rectangle> rectangles = new ArrayList<>();
    private ArrayList<Battleship> battleships = new ArrayList<>();
    private ArrayList<Label> labels = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCells();
        setGridActions();
        initializeBattleships();
        setArrayOfLabels();
        for (Battleship battleship: battleships) {
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
                        System.out.println(e.getSceneX() + " " + e.getSceneY());
                        i = rectangles.indexOf(rect);
                        System.out.println(rect.getX() + " " + rect.getY());
                    }
                }
                if (i != -1) {
                    if (i + (battleship.getLength() - 1) * 10 <= 100) {
                        for (int j = 0; j < battleship.getLength(); j++) {
                            gridPane.getChildren().get(i + 1 + j * 10).setStyle("-fx-background-color: green");
                        }
                    }

                    battleship.used();
                    labels.get(battleships.indexOf(battleship)).setText(battleship.getLeft() + "x");
                }
                window.getChildren().remove(battleshipCopy);
            }
        });
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
}
