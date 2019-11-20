package battleship;

import javafx.scene.image.ImageView;

public class Battleship {
    private ImageView imageView;
    private int left;
    private int length;

    public Battleship(ImageView imageView, int left, int length) {
        this.imageView = imageView;
        this.left = left;
        this.length = length;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getLeft() {
        return left;
    }

    public int getLength() {
        return length;
    }

    public void used() {
        left--;
    }
}
