import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Parser extends WorldModel {
    //private WorldModel world;

    private static final Random rand = new Random();

    private static final int PROPERTY_KEY = 0;

    private static final String BGND_KEY = "background";
    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

    private static final String pokeBall_KEY = "pokeball";
    private static final int pokeBall_NUM_PROPERTIES = 4;
    private static final int pokeBall_ID = 1;
    private static final int pokeBall_COL = 2;
    private static final int pokeBall_ROW = 3;

    private static final String Gate_KEY = "gate";
    private static final int Gate_NUM_PROPERTIES = 4;
    private static final int Gate_ID = 1;
    private static final int Gate_COL = 2;
    private static final int Gate_ROW = 3;

    public Parser(int numRows, int numCols, Background defaultBackground) {
        super(numRows, numCols, defaultBackground);
    }


    public boolean parseGate(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Parser.Gate_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Parser.Gate_COL]),
                    Integer.parseInt(properties[Parser.Gate_ROW]));
            Gate entity = Gate.create(properties[Parser.Gate_ID], pt,
                    imageStore.getImageList(
                            Parser.Gate_KEY));
            this.tryAddEntity( entity);
        }

        return properties.length == Parser.Gate_NUM_PROPERTIES;
    }


    private boolean parseObstacle(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Parser.OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Parser.OBSTACLE_COL]),
                    Integer.parseInt(properties[Parser.OBSTACLE_ROW]));
            Obstacle entity = Obstacle.create(properties[Parser.OBSTACLE_ID], pt,
                    imageStore.getImageList(
                            Parser.OBSTACLE_KEY));
            this.tryAddEntity( entity);
        }

        return properties.length == Parser.OBSTACLE_NUM_PROPERTIES;
    }

    private boolean parsepokeBall(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Parser.pokeBall_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Parser.pokeBall_COL]),
                    Integer.parseInt(properties[Parser.pokeBall_ROW]));
            pokeBall entity = pokeBall.create(properties[Parser.pokeBall_ID], pt,
                    imageStore.getImageList(
                            Parser.pokeBall_KEY));
            this.tryAddEntity( entity);
        }

        return properties.length == Parser.pokeBall_NUM_PROPERTIES;
    }


    private boolean parseBackground(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Parser.BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[this.BGND_COL]),
                    Integer.parseInt(properties[this.BGND_ROW]));
            String id = properties[Parser.BGND_ID];
            this.setBackground( pt,
                    new Background(id, imageStore.getImageList( id)));
        }

        return properties.length == Parser.BGND_NUM_PROPERTIES;
    }


    public boolean processLine(
            String line, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[Parser.PROPERTY_KEY]) {
                case Parser.BGND_KEY:
                    return this.parseBackground(properties, imageStore);
                case Parser.OBSTACLE_KEY:
                    return this.parseObstacle(properties, imageStore);
                case Parser.Gate_KEY:
                    return this.parseGate(properties, imageStore);
                case Parser.pokeBall_KEY:
                    return this.parsepokeBall(properties,imageStore);
            }
        }
        return false;
    }
}
