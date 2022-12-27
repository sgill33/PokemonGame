import processing.core.PImage;

import java.util.*;

public class Pikachu extends Movable{

    private static int count = 0;
    private PathingStrategy strategy = new DijkstraPathingStrategy();
    private List<Point> path;

    public static int getCount() {
        return count;
    }

    public static void incCount() {
        Pikachu.count += 1;
    }


    public Pikachu(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Gate.class)));

        if(target.isPresent()){
            moveTo(world,target.get(),scheduler,imageStore);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction( world, imageStore),
                this.getActionPeriod());
    }

    public List<Point> nextPos(WorldModel world, EventScheduler scheduler,Entity target) {

        List<Point> points;
        path = new LinkedList<>();

        //Optional<Entity> target = world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Gate.class)));  //changed
            points = strategy.computePath(this.getPosition(), target.getPosition(),
                    p ->  world.withinBounds(p) && !world.isOccupied(p),
                    (p1, p2) -> p1.adjacent(p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);
            //DIAGONAL_NEIGHBORS);
            //DIAGONAL_CARDINAL_NEIGHBORS);
            if (!(points.size() == 0))
            {
                path.addAll(points);
            }

            return path;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore) {

        List<Point> lst = nextPos(world,scheduler,target);

        if (this.getPosition().adjacent(target.getPosition())) {
            scheduler.unscheduleAllEvents(this);
            world.removeEntity(this);
            return true;
        }
        else if (lst.size() > 0){
            Point nextPos = lst.get(0);

            if (nextPos.getX()>this.getPosition().getX()){
                this.setImages(imageStore.getImageList("pikRight"));
            }
            else if(nextPos.getX()<this.getPosition().getX()){
                this.setImages(imageStore.getImageList("pikLeft"));
            }
            else if(nextPos.getY()>this.getPosition().getY()){
                this.setImages(imageStore.getImageList("pikDown"));
            }
            else if (nextPos.getY()<this.getPosition().getY()){
                this.setImages(imageStore.getImageList("pikUp"));
            }


            if(!world.isOccupied(nextPos)){
                world.moveEntity(this,nextPos);
            }
            return false;
        }
        else{
            return false;
        }

    }


    public static Pikachu create(
            String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        return new Pikachu( id, position, images,
                actionPeriod, animationPeriod);
    }
}
