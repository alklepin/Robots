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

    public GameObject(double x, double y, ImageView picture)
    {
        Picture = picture;
        X_Position = x;
        Y_Position = y;
    }

    public void draw()
    {

    }

    public void move(String[] args)
    {

    }
}
