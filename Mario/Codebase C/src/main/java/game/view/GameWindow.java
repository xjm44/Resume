package game.view;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import game.model.GameEngine;

import java.util.ArrayList;
import java.util.List;

public class GameWindow {

    private Scene scene;
    private Runner runner;

    private static final double VIEWPORT_MARGIN = 280.0;
    private static final int TIMER = 17;

    KeyboardInputHandler keyboardInputHandler;

    GameEngine model;

    public GameWindow(GameEngine model, int width, int height) {
        this.model = model;
        Pane pane = new Pane();
        this.scene = new Scene(pane, width, height);
        this.runner = new Runner(model, pane, width, height);
        keyboardInputHandler = new KeyboardInputHandler(model);
        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

    }

    public Scene getScene() {
        return this.scene;
    }

    public static int ticksPerSecond() {
        return 1000 / TIMER;
    }

    public static int getTimer() {
        return TIMER;
    }

    public static double getViewportMargin() {
        return VIEWPORT_MARGIN;
    }

    public void run() {
        runner.run();

//        if(keyboardInputHandler.save){
//            memento = new Memento();
//            memento.saveLevel(model);
//            keyboardInputHandler.save = false;
//        }
//        if(keyboardInputHandler.load){
//            model.setCurrentLevel(memento.loadLevel());
//            model.createLevels();
//            model.startLevel();
//            System.out.println("test load");
//
//        }
    }
}
