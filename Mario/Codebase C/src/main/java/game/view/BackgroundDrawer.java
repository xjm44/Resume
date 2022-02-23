package game.view;

import javafx.scene.layout.Pane;
import game.model.GameEngine;

public interface BackgroundDrawer {
    void draw(GameEngine model, Pane pane);
    void update(double xViewportOffset);
}
