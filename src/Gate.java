import processing.core.PImage;

import java.util.List;

public class Gate extends Entity{
    public Gate( String id, Point position, List<PImage> images) {
        super( id, position, images);
    }

    public static Gate create(
            String id, Point position, List<PImage> images)
    {
        return new Gate( id, position, images);
    }




}
