import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */

public class Entity
{

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;


    public String getId(){return this.id;}
    public Point getPosition(){return this.position;}
    public List<PImage> getImages(){return this.images;}
    public int getImageIndex(){return this.imageIndex;}

    public void setImages(List<PImage> images) {
        this.images = images;
    }

    public void setPosition(Point point){this.position = point;}
    public Entity(
            String id,
            Point position,
            List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public PImage getCurrentImage() {
            return this.images.get(this.imageIndex);
    }

}

