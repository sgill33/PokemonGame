import processing.core.PImage;

import java.util.List;

public class AnimatedEntity extends Entity{
    private int animationPeriod;

    public AnimatedEntity( String id, Point position, List<PImage> images, int animationPeriod) {
        super( id, position, images);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod() {
                return this.animationPeriod;
    }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this,
                repeatCount);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(0),
                        this.animationPeriod);
    }
}
