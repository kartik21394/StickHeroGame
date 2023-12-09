package com.example.apgame;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class Platform implements Serializable {
    private double posX;
    private double posY;
    private double width;

    public Platform(double x, double y, double width) {
        this.posX = x;
        this.posY = y;
        this.width = width;
    }
    public void set(double x, double y, double width) {
        this.posX = x;
        this.posY = y;
        this.width = width;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(posX+width/2-10,posY-5,20,10);
        gc.setFill(Color.BLACK);
        gc.fillRect(posX,posY, width, 100);
    }
    public Point2D getEndPoint(){
        Point2D p2d =new Point2D(posX,width);
        return p2d;
    }
}
