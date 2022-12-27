import processing.core.PImage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class Enemy extends Movable{

    private static int count = 0;
    private PathingStrategy strategy = new AStarPathingStrategy();
    private List<Point> path;

    private Function<Point, Stream<Point>> movement;

    public static int getCount() {
        return count;
    }

    public static void incCount() {
        Enemy.count += 1;
    }

    public Enemy(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, Function<Point, Stream<Point>> movement) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.movement = movement;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target1 = world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Pikachu.class)));
        Optional<Entity> target2 = world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Player.class)));
        if(target1.isPresent()){
            if(moveTo(world,target1.get(),scheduler,imageStore)){
                scheduler.unscheduleAllEvents(target1.get());
                world.removeEntity(target1.get());
            }
        }
        else if(target2.isPresent()){
            if(moveTo(world,target2.get(),scheduler,imageStore)){
                scheduler.unscheduleAllEvents(target2.get());
                world.removeEntity(target2.get());

                for (Entity entity : world.getEntities()) {
                    scheduler.unscheduleAllEvents(entity);
                }
                VirtualWorld.setEnd(2);
            }
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction( world, imageStore),
                this.getActionPeriod());
    }

    public List<Point> nextPos(WorldModel world, EventScheduler scheduler,Entity target) {

        List<Point> points;
        path = new LinkedList<>();

        points = strategy.computePath(this.getPosition(), target.getPosition(),
                p ->  world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> p1.adjacent(p2),
                this.movement);
        if (!(points.size() == 0))
        {
            path.addAll(points);
        }

        return path;
    }

    public Function<Point, Stream<Point>> getMovement() {
        return movement;
    }
}
