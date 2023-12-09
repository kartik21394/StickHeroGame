package com.example.apgame;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.Random;

public class PlayGameScreen implements Serializable {
    private enum Phase {
        WAITING, STRETCHING, TURNING, WALKING, TRANSITIONING, FALLING
    }
    Stick player;
    static Canvas canvas;
    static Scene scene;
    private int platformWidth,stickmaxlength,platformwidth;
    private Phase phase;
    Platform platform1;
    Platform platform2;

    static Random rnd;
    static Button revive,cancel,pause,flip;
    int cherrypositionX,platformpos,cherrypositionY,flipposition[];
    private static GraphicsContext graphic;
    private static Image img,bg;
    private boolean playing;
    public PlayGameScreen(int width ,int  height) {
        bg=new Image(String.valueOf(getClass().getResource("bg.jpg")));
        img=new Image(String.valueOf(getClass().getResource("chery.png")));
        rnd=new Random();
        platformWidth=100;
        canvas=new Canvas(width,height);
        flip=new Button("Flip");
        flip.setOnAction(event -> {
            this.player.flipChar();
        });
        flipposition=new int[]{height-150,height-100};
        graphic=canvas.getGraphicsContext2D();
        graphic.setFont(new Font(20));
        revive=new Button("Revive");
        revive.setVisible(false);
        revive.setOnAction(event -> {
            if(player.revive()) {
                nextGame();
//                phase = Phase.WAITING;
            }
            else {
                playing=false;
            }
        });
        pause=new Button("Pause");
        pause.setOnAction(event -> {
            StickHero.setpauseScreen();
        });
        pause.setLayoutY(30);
        pause.setLayoutX(40);
        pause.setScaleY(2);
        pause.setScaleX(2);
        pause.setBorder(Border.stroke(Color.BLACK));
        pause.setBackground(Background.fill(Color.TRANSPARENT));
        cancel=new Button("Cancel");
        cancel.setVisible(false);
        cancel.setOnAction(event -> {
            StickHero.writeSave();
            resetGame();
            StickHero.setScreen();
        });
        Group stackPane=new Group();
        cancel.setLayoutX(380);
        cancel.setLayoutY(400);
        cancel.setScaleX(2);
        cancel.setScaleY(2);
        cancel.setBackground(Background.fill(Color.RED));
        cancel.setBorder(Border.stroke(Color.TRANSPARENT));
        cancel.setFont(Font.font("Verdana"));
        cancel.setTextFill(Color.WHITE);
        revive.setScaleY(2);
        revive.setScaleX(2);
        revive.setLayoutX(380);
        revive.setLayoutY(310);
        revive.setBackground(Background.fill(Color.RED));
        revive.setBorder(Border.stroke(Color.TRANSPARENT));
        revive.setFont(Font.font("Verdana"));
        revive.setTextFill(Color.WHITE);
        flip.setLayoutX(500);
        flip.setLayoutY(310);
        flip.setScaleY(2);
        flip.setScaleX(2);
        flip.setBackground(Background.fill(Color.RED));
        flip.setBorder(Border.stroke(Color.TRANSPARENT));
        flip.setFont(Font.font("Verdana"));
        flip.setTextFill(Color.WHITE);
        stackPane.getChildren().add(canvas);
        stackPane.getChildren().add(revive);
        stackPane.getChildren().add(pause);
        stackPane.getChildren().add(cancel);
        stackPane.getChildren().add(flip);

        scene = new Scene(stackPane, width,height);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if(ke.getCode()==KeyCode.SPACE){
                player.flipChar();
            }}
        });
        resetGame();

    }
    public void resumeGame(Stage stage){
        stage.setTitle("StickHero");
        stage.setScene(scene);
        stage.show();
    }
    public void setScene(Stage stage){
        stage.setTitle("StickHero");
        stage.setScene(scene);
        stage.show();
        canvas.setOnMousePressed(event -> handleMousePress());
        canvas.setOnMouseReleased(event -> handleMouseRelease());

        new AnimationTimer() {
            long last=0;
            @Override
            public void handle(long now) {
                if(now-last>16000000){
                update();
                draw(graphic);
            }}

        }.start();
    }
    private void handleMousePress() {
        if (phase == Phase.WAITING) {
            phase = Phase.STRETCHING;
            player.startStretching();
        }
    }

    private void handleMouseRelease() {
        if (phase == Phase.STRETCHING) {
            phase = Phase.TURNING;
            player.stopStretching();
            player.startTurning();
        }
    }
    private void draw(GraphicsContext con) {

        con.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
        con.drawImage(bg,0,0,canvas.getWidth(), canvas.getHeight());
        if(player.getCherryVisible()){
            con.drawImage(img,cherrypositionX, flipposition[cherrypositionY],50,50);
        }
        con.drawImage(img,750,0,50,50);
        con.setFont(new Font(40));
        con.fillText("Score: "+player.getScore(), 350,100);
        con.fillText("Highest Score: "+ StickHero.getHighestscore(),310,150);
        con.setFont(new Font(20));
        con.fillText("Cherries: "+player.getScoreCherry(), 620,30);

        if(!playing){
            con.fillText("Not Enough Cherries!",310,250);
        }
        platform1.draw(con);
        platform2.draw(con);
        player.draw(con);
    }

    private void update() {
        switch (phase) {
            case WAITING:
                break;
            case STRETCHING:
                player.updateStretching();
                break;
            case TURNING:
                player.updateTurning(stickmaxlength);
                if (player.isTurningComplete()){
                    phase=Phase.WALKING;
                }
                break;
            case WALKING:
                player.updateWalking(platform2,cherrypositionX,cherrypositionY==0?true:false);
                if (player.isWalkingComplete()) {
                    if (!player.isTransitioningComplete()){
                        if(Integer.parseInt(StickHero.getHighestscore())<Integer.parseInt(player.getScore())){
                            StickHero.setHighestscore(player.getScore());
                        }
                        phase = Phase.TRANSITIONING;
                    }
                    if(!player.isFallingComplete()){
                        phase=Phase.FALLING;
                    }
                }
                break;
            case TRANSITIONING:
                player.updateTransitioning();
                if (player.isTransitioningComplete()) {
                    nextGame();
                    phase = Phase.WAITING;
                }
                break;
            case FALLING:
                player.updateFalling(canvas.getHeight());
                if (player.isFallingComplete()) {
                    revive.setVisible(true);
                    cancel.setVisible(true);
                }
                break;
        }
    }

    void resetGame(){
        platform1=new Platform(100,canvas.getHeight()-100,platformWidth);
        platformpos=rnd.nextInt(400,700);
        platformwidth= rnd.nextInt(20,100);
        stickmaxlength=platformpos+platformwidth/2;
        platform2=new Platform(platformpos,canvas.getHeight()-100, platformwidth);
        player=Stick.getInstance(100 + platformWidth/2,canvas.getHeight()-100); //singleton class
        phase=Phase.WAITING;
        revive.setVisible(false);
        cancel.setVisible(false);
        playing=true;
        cherrypositionX=rnd.nextInt(200,platformpos-50);
        cherrypositionY=rnd.nextInt(0,2);
    }
    private void nextGame(){
        platform1.set(100,canvas.getHeight()-100,platformWidth);
        platformpos=rnd.nextInt(400,700);
        platformwidth= rnd.nextInt(20,100);
        platform2.set(platformpos,canvas.getHeight()-100,platformwidth);
        stickmaxlength=platformpos+platformwidth/2;
        player.set(100 + platformWidth/2,canvas.getHeight()-100);
        phase=Phase.WAITING;
        revive.setVisible(false);
        cancel.setVisible(false);
//        playing=true;
        cherrypositionX=rnd.nextInt(300,platformpos-50);
        cherrypositionY=rnd.nextInt(0,2);
    }

}
