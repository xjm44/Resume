package game.view;

import javafx.scene.Node;
import game.model.Entity;

public interface EntityView {
    void update(double xViewportOffset);

    boolean matchesEntity(Entity entity);

    void markForDelete();

    Node getNode();

    boolean isMarkedForDelete();
}
