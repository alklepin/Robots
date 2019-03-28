package gui;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;

public abstract class GameObject {
    public volatile double X_Position;
    public volatile double Y_Position;

    public volatile boolean Alive;
    public ImageView Picture;

    public GameObject(double x, double y, String path, double width, double height)
    {
        loadImage(path, width, height);
        X_Position = x;
        Y_Position = y;
    }

    private void loadImage(String fileName, double width, double height){
        File file = new File(fileName);
        String localUrl = file.toURI().toString();
        Image image = new Image(localUrl, width, height, false, true);
        Picture = new ImageView(image);
    }

    public void draw()
    {

    }

    public void move(String[] args)
    {

    }
}
