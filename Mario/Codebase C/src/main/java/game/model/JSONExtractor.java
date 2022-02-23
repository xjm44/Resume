package game.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to extract all the necessary entities and attributes
 * associated with a level
 */
public class JSONExtractor {

    /**
     * The extracted entities
     */
    private List<Entity> entities = new ArrayList<>();

    /**
     * The extracted movables entities
     */
    private List<MovableEntity> movableEntities = new ArrayList<>();

    /**
     * The extracted enemies
     */
    private List<Enemy> enemies = new ArrayList<>();

    /**
     * The extracted controllable entity
     */
    private Controllable hero;

    /**
     * The extracted floor height
     */
    private double floorHeight;

    /**
     * The extracted level width
     */
    private double levelWidth;

    /**
     * The extracted level height
     */
    private double levelHeight;

    /**
     * JSON object used to extract all the necessary information
     */
    private JSONObject obj;

    private long target;

    /**
     * The factories used to create the different objects
     */
    private EntityFactoryInterface entityFactory = new EntityFactory();
    private MovableEntityFactoryInterface movableFactory = new MovableEntityFactory();
    private EnemyFactoryInterface enemyFactory = new EnemyFactory();

    /**
     * Takes the specified json path string and extracts all of the relevant information
     * @param jsonPath Path to the json file
     */
    public JSONExtractor(String jsonPath) {
        try {
            FileReader reader = new FileReader("src/main/resources/" + jsonPath);
            JSONParser parser = new JSONParser();
            this.obj = (JSONObject) parser.parse(reader);
            this.extract();
            reader.close();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the floor height associated with the level
     * @return The floor height of the level
     */
    public double getFloorHeight() {
        return this.floorHeight;
    }

    /**
     * Returns the associated levels width
     * @return The width of the level
     */
    public double getLevelWidth() {
        return this.levelWidth;
    }

    /**
     * Returns the associated levels height
     * @return The heigh of the level
     */
    public double getLevelHeight() {
        return this.levelHeight;
    }

    /**
     * Returns the controllable entity associated with the level
     * @return The controllable entity of the level
     */
    public Controllable getHero() {
        return this.hero;
    }

    /**
     * Returns entities associated with the level
     * @return The entities of the level
     */
    public List<Entity> getEntities() {
        return this.entities;
    }

    /**
     * Returns the movable entities associated with the level
     * @return The movables entities of the level
     */
    public List<MovableEntity> getMovableEntities() {
        return this.movableEntities;
    }

    /**
     * Returns the enemies associated with the level
     * @return The enemies of the level
     */
    public List<Enemy> getEnemies() {
        return this.enemies;
    }

    public long getTarget(){
        return target;
    }

    /**
     * Performs all extractions is a specified order
     */
    private void extract() {
        extractFloorHeight();
        extractLevelHeight();
        extractLevelWidth();
        extractHero();
        extractImmovableEntities();
        extractMovableEntities();
        extractEnemies();
        extractTarget();
    }

    /**
     * Extracts the floor height
     */
    private void extractFloorHeight() {
        this.floorHeight = (double) this.obj.get("floorHeight");
    }

    /**
     * Extracts the level width
     */
    private void extractLevelWidth() {
        this.levelWidth = (double) this.obj.get("levelWidth");
    }

    /**
     * Extracts the level height
     */

    private void extractLevelHeight() {
        this.levelHeight = (double) this.obj.get("levelHeight");
    }

    /**
     * Extracts the controllable entity
     */
    private void extractHero() {
        JSONObject heroObj = (JSONObject) this.obj.get("hero");
        double xPos = (double) heroObj.get("x");
        double xVel = (double) heroObj.get("xVelocity");
        double width = 30.0;
        double height = 51.0;
        String size = (String) heroObj.get("size");
        switch (size) {
            case "tiny":
                width *= 0.75;
                height *= 0.75;
                break;
            case "normal":
                width *= 1.0;
                height *= 1.0;
                break;
            case "large":
                width *= 1.25;
                height *= 1.25;
                break;
            case "giant":
                width *= 1.5;
                height *= 1.5;
                break;
        }
        this.hero = new Hero("ch_stand1.png", xPos, floorHeight - height, width, height, xVel);
        this.entities.add(this.hero);
    }

    /**
     * Extracts the immovable entities
     */
    private void extractImmovableEntities() {
        JSONArray array = (JSONArray) this.obj.get("immovableEntities");
        for (Object o : array) {
            JSONObject entityObj = (JSONObject) o;
            String type = (String) entityObj.get("type");
            double xPos = (double) entityObj.get("x");
            double yPos = (double) entityObj.get("y");
            Entity entity = entityFactory.makeEntity(type, xPos, yPos);
            this.entities.add(entity);
        }
    }

    /**
     * Extracts the movable entities
     */
    private void extractMovableEntities() {
        JSONArray array = (JSONArray) this.obj.get("movableEntities");
        for (Object o : array) {
            JSONObject movableEntityObj = (JSONObject) o;
            String type = (String) movableEntityObj.get("type");
            double xPos = (double) movableEntityObj.get("x");
            double yPos = (double) movableEntityObj.get("y");
            double xVel = (double) movableEntityObj.get("xVelocity");
            MovableEntity movableEntity = movableFactory.makeMovableEntity(type, xPos, yPos, xVel);
            this.entities.add(movableEntity);
            this.movableEntities.add(movableEntity);
        }
    }

    /**
     * Extracts the enemies
     */
    private void extractEnemies() {
        JSONArray array = (JSONArray) this.obj.get("enemies");
        for (Object o : array) {
            JSONObject enemyObj = (JSONObject) o;
            String type = (String) enemyObj.get("type");
            double xPos = (double) enemyObj.get("x");
            Enemy enemy = enemyFactory.makeEnemy(type, xPos, this.floorHeight);
            this.entities.add(enemy);
            this.enemies.add(enemy);
        }
    }

    private void extractTarget(){
        target = (Long) this.obj.get("target");


    }

}
