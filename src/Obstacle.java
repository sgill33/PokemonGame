import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity{
    public Obstacle(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }

    public static Obstacle create(
            String id, Point position, List<PImage> images)
    {
        return new Obstacle( id, position, images);
    }

}
