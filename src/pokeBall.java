import processing.core.PImage;

import java.util.List;

public class pokeBall extends Entity{
    private boolean full = false;

    public void setFull() {
        this.full = true;
    }

    public boolean isFull() {
        return full;
    }

    public pokeBall(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }

    public static pokeBall create(
            String id, Point position, List<PImage> images)
    {
        return new pokeBall( id, position, images);
    }

}
