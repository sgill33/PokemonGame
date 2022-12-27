import processing.core.PImage;

import java.util.List;

public abstract class ActiveEntity extends AnimatedEntity{
    private int actionPeriod;
    public ActiveEntity( String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super( id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {

        scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.actionPeriod);
        scheduler.scheduleEvent(this,
                        this.createAnimationAction(0),
                        this.getAnimationPeriod());
    }

    public int getActionPeriod(){return this.actionPeriod;}

    public abstract void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);




    public Action createActivityAction(
            WorldModel world, ImageStore imageStore)
    {
        return new Activity(this, world, imageStore);
    }

}
