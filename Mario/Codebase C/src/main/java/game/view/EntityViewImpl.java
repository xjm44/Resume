package game.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import game.model.Entity;

public class EntityViewImpl implements EntityView {
    private Entity entity;
    private boolean delete = false;
    private ImageView node;
    private String imagePath;

    EntityViewImpl(Entity entity) {
        this.entity = entity;
        this.imagePath = entity.getImagePath();
        this.node = new ImageView(imagePath);
        this.node.setViewOrder(getViewOrder(entity.getLayer()));
        update(0);
    }

    private double getViewOrder(Entity.Layer layer) {
        switch (layer) {
            case BACKGROUND: return 100.0;
            case FOREGROUND: return 50.0;
            case EFFECT: return 25.0;
            default: throw new IllegalStateException("Javac doesn't understand how enums work so now I have to exist");
        }
    }

    @Override
    public void update(double xViewportOffset) {
        String newPath = entity.getImagePath();
        if (!imagePath.equals(newPath)) {
            imagePath = newPath;
            node.setImage(new Image(imagePath));
        }
        node.setX(entity.getXPos() - xViewportOffset);
        node.setY(entity.getYPos());
        node.setFitHeight(entity.getHeight());
        node.setFitWidth(entity.getWidth());
        node.setPreserveRatio(true);
        delete = false;
    }

    @Override
    public boolean matchesEntity(Entity entity) {
        return this.entity.equals(entity);
    }

    @Override
    public void markForDelete() {
        this.delete = true;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public boolean isMarkedForDelete() {
        return this.delete;
    }
}
