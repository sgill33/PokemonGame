import processing.core.PImage;

import java.util.List;

public class Pokemon extends AnimatedEntity{
    public Pokemon(String id, Point position, List<PImage> images, int animationPeriod) {
        super(id, position, images, animationPeriod);
    }

    public static Pokemon create(
            String id,
            Point position,
            int animationPeriod,
            List<PImage> images)
    {
        return new Pokemon( id, position, images,
               animationPeriod);
    }

}
