package game.view;

import game.model.Block;
import game.model.Entity;
import game.model.GameEngine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

class Runner {

    private GameEngine model;
    private Pane pane;
    private List<EntityView> entityViews;
    private BackgroundDrawer backgroundDrawer;
    private ImageView[] health = new ImageView[3];
    private Text lives;
    private Text time;
    private Text levelId;
    private Text score;
    private Timeline timeline;
    private double xViewportOffset = 0.0;
    private double width;
    private double height;
    private int scoreCounter = 0;
    private int healthCounter = 0;

    Runner(GameEngine model, Pane pane, double width, double height) {
        this.model = model;
        this.pane = pane;
        this.width = width;
        this.height = height;
        this.entityViews = new ArrayList<>();
        this.health[0] = new ImageView(new Image("heart.png"));
        this.health[1] = new ImageView(new Image("heart.png"));
        this.health[2] = new ImageView(new Image("heart.png"));
        for (int i = health.length - 1; i >= 0; i--) {
            this.health[i].setFitHeight(30);
            this.health[i].setFitWidth(30);
            this.health[i].setY(10);
            this.health[i].setX(width - 40 - i * 40);
        }

        this.lives = new Text(10, 20, "lives remaining: " + model.getLives());
        this.lives.setFont(Font.font ("Chalkboard SE", FontPosture.ITALIC, 20));
        this.lives.setFill(Paint.valueOf("BLACK"));
        this.lives.setX(width -  this.lives.getLayoutBounds().getWidth() - 150);
        this.lives.setY(30);
        this.pane.getChildren().add(lives);
        this.backgroundDrawer = new ParallaxBackground();
        this.backgroundDrawer.draw(model, pane);
        addHealth();

        time = new Text(10, 20, "Time: " + 0);
        time.setFont(Font.font ("Chalkboard SE", FontPosture.ITALIC, 20));
        time.setFill(Paint.valueOf("BLACK"));
        time.setLayoutX((width - time.getLayoutBounds().getWidth()) -530);
        time.setLayoutY(7);
        pane.getChildren().add(time);

        Score();
        Level();


    }

    void run() {
        timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void draw() {
        model.tick();


        scoreCounter = model.getScore();
        healthCounter = model.getCurrentLevel().getHeroHealth();

        displayLevel();
        displayScore();
        displayTime();
        countTime();

        if(model.isFinished()&&model.getCurrentLevelId()<3){
            nextLevel();
        }else if(model.isFinished()){
            drawScreen("Winner!\n");
            return;
        }

        if(model.gameOver()) {
            drawScreen("You lose!");
            return;
        }

        drawLives();

        if (model.getCurrentLevel().getHeroHealth() == 2) {
            pane.getChildren().remove(health[2]);
        } else if (model.getCurrentLevel().getHeroHealth() == 1) {
            pane.getChildren().remove(health[1]);
        } else if (model.getCurrentLevel().getHeroHealth() == 0) {
            pane.getChildren().remove(health[0]);
            addHealth();
            model.resetCurrentLevel();
            model.setScore(scoreCounter);
        }

        List<Entity> entities = model.getCurrentLevel().getEntities();

        for (EntityView entityView: entityViews) {
            entityView.markForDelete();
        }

        double heroXPos = model.getCurrentLevel().getHeroX();
        heroXPos -= xViewportOffset;

        if (heroXPos < GameWindow.getViewportMargin()) {
            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
                xViewportOffset -= GameWindow.getViewportMargin() - heroXPos;
                if (xViewportOffset < 0) {
                    xViewportOffset = 0;
                }
            }
        } else if (heroXPos > width -  GameWindow.getViewportMargin()) {
            xViewportOffset += heroXPos - (width - GameWindow.getViewportMargin());
        }

        backgroundDrawer.update(xViewportOffset);

        for (Entity entity: entities) {
            boolean notFound = true;
            for (EntityView view: entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView: entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }
        entityViews.removeIf(EntityView::isMarkedForDelete);
    }

    private void nextLevel(){

        model.nextLevel();
        model.setScore(scoreCounter);
        model.getCurrentLevel().setHeroHealth(healthCounter);
    }
    /**
     * Draws the finish screen with the specified message in the center
     * @param message The message to be displayed in the center
     */
    private void drawScreen(String message) {
        for (EntityView entityView: entityViews) {
            pane.getChildren().remove(entityView.getNode());
        }
        for (ImageView life: this.health) {
            pane.getChildren().remove(life);
        }
        pane.getChildren().remove(lives);
        Text t = new Text(10, 20, message);
        t.setFont(Font.font ("Chalkboard SE", FontPosture.ITALIC, 60));
        t.setFill(Paint.valueOf("BLACK"));
        t.setLayoutX((width - t.getLayoutBounds().getWidth()) / 2.0);
        t.setLayoutY((height - t.getLayoutBounds().getHeight()) / 2.0);
        t.setTextAlignment(TextAlignment.CENTER);
        pane.getChildren().add(t);
        timeline.stop();

    }

    /**
     * Adds the hero's health to the view
     */
    private void addHealth() {
        for (ImageView life: health) {
            pane.getChildren().add(life);
        }
    }

    /**
     * Adds the number of lives the hero has to the view
     */
    private void drawLives() {
        this.lives.setText("lives remaining: " + model.getLives());

    }

    private void Level(){
        levelId = new Text(10, 20, "Level: " + 0);
        levelId.setFont(Font.font ("Chalkboard SE", FontPosture.ITALIC, 20));
        levelId.setFill(Paint.valueOf("BLACK"));
        levelId.setLayoutX((width - levelId.getLayoutBounds().getWidth()) -880);
        levelId.setLayoutY(5);
        pane.getChildren().add(levelId);
    }

    private void Score(){
        score = new Text(10, 20, "Score: " + 0);
        score.setFont(Font.font ("Chalkboard SE", FontPosture.ITALIC, 20));
        score.setFill(Paint.valueOf("BLACK"));
        score.setLayoutX((width - score.getLayoutBounds().getWidth()) -700);
        score.setLayoutY(7);
        pane.getChildren().add(score);
    }
    private void displayTime(){
        time.setText("Time: " + model.getDuration().toSeconds() + "s    Target: "+ model.getTarget()+"s");
    }

    private void displayScore(){
        if(model.getDuration().toSeconds()<=model.getTarget()){
            if(countTime()){
                model.setScore(test);
            }
            score.setText("Score: "+ model.getScore());
        }else{
            if(countTime()){
                if(model.getScore()<= 0){
                    model.setScore(0);
                }else{
                    model.setScore(-test);
                }
            }
            score.setText("Score: "+ model.getScore());
        }

    }
    private void displayLevel(){
        levelId.setText("Level: "+ model.getCurrentLevelId());

    }
    private int test = 1;
    private boolean countTime(){
        if(test<(59)){
            test++;
            return false;
        }else{
            test = 1;
            return true;
        }
    }


}
