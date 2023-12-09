package com.example.apgame;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

import static javafx.scene.paint.Color.BLACK;

public class Stick implements Serializable {

    private int breadth;
    private int startAngle;

    private int length;
    private static Rectangle rectangle;
    private double positionX;
    private double positionY;
    private int rotation;
    private double endX;
    private double playerPosX;
    private double playerPosY;
    private int stretchingSpeed;
    private int walkingSpeed;
    private String scorecherry,score;
    static Image sp,spflip;
    private boolean flip,firsttime,cherryvisible;
    private boolean stretching,turning,walking,transitioning,falling;
    public boolean getCherryVisible(){
        return cherryvisible;
    }
    public String getScore() {
        return score;
    }
    public String getScoreCherry(){return scorecherry;}
    private static Stick instance; // Singleton class
    public Stick(double x, double y ) {
        sp=new Image(String.valueOf(getClass().getResource("stckchar.png")));
        spflip=new Image(String.valueOf(getClass().getResource("stckcharflip.png")));
        this.flip=true;
        this.firsttime=true;
        this.breadth=4;
        this.length=10;
        this.positionX=x;
        this.positionY=y;
        this.startAngle=180;
        this.rotation=startAngle;
        this.rectangle=new Rectangle(this.positionX,this.positionY,this.breadth,this.length);
        this.rectangle.setFill(BLACK);
        this.playerPosX=x;
        this.playerPosY=y;
        this.stretchingSpeed=1;
        this.walkingSpeed=1;
        this.score="0";
        this.scorecherry="0";
        this.cherryvisible=true;

    }
    public void set(double x,double y){
        this.flip=true;
        this.firsttime=true;
        this.positionX=x;
        this.positionY=y;
        this.length=10;
        this.playerPosX=x;
        this.playerPosY=y;
        this.rotation=startAngle;
        this.cherryvisible=true;

    }
    public static Stick getInstance(double x, double y) {       //singleton class
            instance = new Stick(x, y);
        return instance;
    }
    public boolean flipChar(){
        flip=!flip;
        return flip;
    }
    public boolean revive(){
        int tempscore=Integer.valueOf(scorecherry)-3;
        if(tempscore<0) {

            return false;
        }
            scorecherry = Integer.toString(tempscore);
        return true;
    }
    public void startStretching() {
        stretching = true;
    }

    public void stopStretching() {
        stretching = false;
    }

    public void updateStretching() {
        if (stretching) {

            length += stretchingSpeed;
        }
    }

    public void startTurning() {
        turning = true;
    }

    public void updateTurning(int sss) {
        if (turning) {
            rotation += 1;

            if (rotation >= startAngle+90) {
                turning = false;
                walking = true;
                endX = positionX + length;
                if(Math.abs(endX-sss)<10){
                    score=Integer.toString(Integer.valueOf(score)+1);
                }
            }
        }
    }

    public boolean isTurningComplete() {
        return !turning;
    }

    public void updateWalking(Platform plat1,int cherrypositionX, boolean cherrypositionY) {

        if (walking) {
            if(!StickHero.walking.isPlaying()){
                StickHero.walking.play();
            }
            playerPosX += walkingSpeed;
            if(firsttime && cherrypositionY==flip && Math.abs(playerPosX-cherrypositionX)<5){
                firsttime=false;
                scorecherry=Integer.toString(Integer.valueOf(scorecherry)+1);
                StickHero.eating.play();
                cherryvisible=false;
            }
            if(playerPosX>=endX){
                walking = false;
                Point2D p2d=plat1.getEndPoint();
                if(StickHero.walking.isPlaying()){
                    StickHero.walking.stop();
                }
                if (p2d.getX()>endX || p2d.getX() + p2d.getY()<endX){
                    falling=true;
                }
                else{
                    score=Integer.toString(Integer.valueOf(score)+1);
                    transitioning=true;
                    rotation = startAngle;
                }
            }
        }
    }

    public boolean isWalkingComplete() {
        return !walking;
    }

    public void updateTransitioning() {
        if (transitioning) {
            transitioning = false;
        }
    }

    public boolean isTransitioningComplete() {
        return !transitioning;
    }

    public void updateFalling(double check) {
        if (falling) {
            playerPosY+=walkingSpeed;
            if(playerPosY>780 && !StickHero.falling.isPlaying()){
                StickHero.falling.play();
            }
            if(playerPosY>check + 30){
                falling=false;
                rotation=startAngle;
            }
        }
    }

    public boolean isFallingComplete() {
        return !falling;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
//        gc.fillRect(playerPosX,playerPosY-30,30,30);
        if(flip){
            gc.drawImage(sp,playerPosX,playerPosY-75,50,75);
        }
        else{
            gc.drawImage(spflip,playerPosX,playerPosY,50,75);
        }
        gc.translate(positionX,positionY);
        gc.rotate(rotation);
        gc.translate(-positionX,-positionY);
        gc.fillRect(positionX, positionY, breadth,length);
        gc.restore();
    }
}

