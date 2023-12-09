package com.example.apgame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class StickHero extends Application {
    private int width;
    public static AudioClip eating,falling,walking;
    private static Media bgm;
    private static MediaPlayer bgmp;
    static SavedGames save;
    static PlayGameScreen playgamescreen;
    private int height;
    static Stage stage;
    static Scene scene;
    private static PausePlayScreen pausescreen;
    FXMLLoader root;
    public static String getHighestscore() {
        return save.getHighestscore();
    }

    public static void setHighestscore(String highestscore) {
        save.setHighestscore(highestscore);
    }
    @Override
    public void start(Stage stage) throws IOException {
        height=800;
        width=800;
        this.stage=stage;
        playgamescreen =new PlayGameScreen(width,height);
        root = new FXMLLoader(getClass().getResource("scene1.fxml"));
        pausescreen=new PausePlayScreen(width,height,FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scene4.fxml"))));
        scene = new Scene(root.load(), width,height);
        if(Files.exists(Path.of("main"))){
            save=(SavedGames) readGameObject("main");
        }
        else{
            save=new SavedGames();
        }
        eating=new AudioClip(getClass().getResource("eating.mp3").toExternalForm());
        walking=new AudioClip(getClass().getResource("walking.mp3").toExternalForm());
        falling=new AudioClip(getClass().getResource("falling.mp3").toExternalForm());
        bgm=new Media(getClass().getResource("bgm.mp3").toExternalForm());
        bgmp=new MediaPlayer(bgm);
        bgmp.setCycleCount(MediaPlayer.INDEFINITE);
        bgmp.setVolume(0.4);
        bgmp.play();
        walking.setVolume(200);
        eating.setVolume(200);
        falling.setVolume(200);
        stage.setTitle("StickHero");
        stage.setScene(scene);
        stage.show();
    }
    public static void eatingSound(){
        eating.play();
    }
    public static void stopfallingSound(){
        eating.stop();
    }
    public static void walkingSound(){
        walking.play();
    }
    public static void fallingSound(){
        falling.play();
    }
    public static void writeGameObject(Serializable sz, String func1){
        try{
            FileOutputStream fos=new FileOutputStream(func1);
            ObjectOutputStream os=new ObjectOutputStream(fos);
            os.writeObject(sz);
            os.close();
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Object readGameObject(String func1){
        try{
            FileInputStream fis=new FileInputStream(func1);
            ObjectInputStream os=new ObjectInputStream(fis);
            Object object = os.readObject();
            os.close();
            fis.close();
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeSave(){
        writeGameObject(save,"main");
    }
    public void setGame(){
        playgamescreen.resetGame();
        playgamescreen.setScene(stage);
    }
    public void resumeGame(){
        playgamescreen.resumeGame(stage);

    }
    public void loadGame(){
        playgamescreen=(PlayGameScreen) readGameObject("A1");
        playgamescreen.setScene(stage);
    }
    public void exitGame(){
        writeSave();
        stage.close();
    }
    public static void setScreen(){
        stage.setTitle("StickHero");
        stage.setScene(scene);
        stage.show();
    }
    public static void setpauseScreen(){
        pausescreen.setScreen();
    }
    public void savegame(){
        writeSave();
        writeGameObject(playgamescreen,"A1");
        StickHero.setScreen();
    }
    public void mainmenu(){
        writeSave();
        StickHero.setScreen();
    }
    public static void main(String[] args) {
        launch();
    }
}