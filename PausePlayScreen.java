package com.example.apgame;
import javafx.scene.Parent;
import javafx.scene.Scene;
import static com.example.apgame.StickHero.stage;

public class PausePlayScreen {
    Parent root;
    Scene scene;
    public PausePlayScreen(double width,double height,Parent rr) {
        root=rr;
        scene = new Scene(root, width,height);
    }
    public void setScreen(){
        stage.setTitle("StickHero");
        stage.setScene(scene);
        stage.show();
    }
}
