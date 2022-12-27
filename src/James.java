import processing.core.PImage;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class James extends Enemy{
    public James(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, Function<Point, Stream<Point>> movement) {
        super(id, position, images, actionPeriod, animationPeriod,movement);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler, ImageStore imageStore) {

        List<Point> lst = nextPos(world,scheduler,target);

        if (this.getPosition().adjacent(target.getPosition())) {
            return true;
        }
        else if (lst.size() > 0){
            Point nextPos = lst.get(0);

            if (nextPos.getX()>this.getPosition().getX()){
                this.setImages(imageStore.getImageList("jamesRight"));
            }
            else if(nextPos.getX()<this.getPosition().getX()){
                this.setImages(imageStore.getImageList("jamesLeft"));
            }
            else if(nextPos.getY()>this.getPosition().getY()){
                this.setImages(imageStore.getImageList("jamesDown"));
            }
            else if (nextPos.getY()<this.getPosition().getY()){
                this.setImages(imageStore.getImageList("jamesUp"));
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

    public static James create(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images,Function<Point, Stream<Point>> movement)
    {
        return new James( id, position, images,
                actionPeriod, animationPeriod,movement);
    }

}
