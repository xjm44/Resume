package game.view;

import game.model.Entity;
import game.model.LevelImplementation;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import game.model.GameEngine;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class KeyboardInputHandler{
    private final GameEngine model;
    private boolean left = false;
    private boolean right = false;
    private Set<KeyCode> pressedKeys = new HashSet<>();

    private Map<String, MediaPlayer> sounds = new HashMap<>();

    public boolean save ;
    public boolean load ;


    KeyboardInputHandler(GameEngine model) {
        this.model = model;


        URL mediaUrl = getClass().getResource("/jump.wav");
        String jumpURL = mediaUrl.toExternalForm();

        Media sound = new Media(jumpURL);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        sounds.put("jump", mediaPlayer);
    }

    void handlePressed(KeyEvent keyEvent) {

        if (pressedKeys.contains(keyEvent.getCode())) {
            return;
        }
        pressedKeys.add(keyEvent.getCode());

        if(keyEvent.getCode().equals(KeyCode.S)){
            System.out.println("save");


//            System.out.println(model.getCurrentLevelId());

//            for(Entity e: model.getCurrentLevel().getEntities()){
//                System.out.println(e.getImagePath()+"\t"+e.getXPos()+"\t"+e.getYPos());
//            }
//            for(Entity e: model.getCurrentLevel().getEntities()){
//                System.out.println(e.getClass().getName());
//            }


        }
        if(keyEvent.getCode().equals(KeyCode.L)){
            System.out.println("load");

//            System.out.println(model.getCurrentLevelId());
//            for(Entity e: model.getCurrentLevel().getEntities()){
//                System.out.println(e.getImagePath()+e.getXPos());
//            }

        }

        if (keyEvent.getCode().equals(KeyCode.UP)) {
            if (model.jump()) {
                MediaPlayer jumpPlayer = sounds.get("jump");
                jumpPlayer.stop();
                jumpPlayer.play();
            }
        }

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = true;

        }
        else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = true;
        } else {
            return;
        }

        if (left) {
            if (right) {
                model.stopMoving();
            } else {
                model.moveLeft();
            }
        } else {
            model.moveRight();
        }




    }

    void handleReleased(KeyEvent keyEvent) {
        pressedKeys.remove(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = false;
        }
        else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = false;
        } else {
            return;
        }

        if (!(right || left)) {
            model.stopMoving();
        } else if (right) {
            model.moveRight();
        } else {
            model.moveLeft();
        }
    }
}
