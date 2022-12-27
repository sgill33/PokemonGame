import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Player extends Entity{
    private PApplet screen;
    private int pokeCount = 0;
    private int goalCount = 0;
    public Player(String id, Point position, List<PImage> images,PApplet screen) {

        super(id, position, images);
        this.screen = screen;
    }

    public void move(int x, int y, WorldModel world,ImageStore imageStore, EventScheduler scheduler){
        Point nextPos = new Point(this.getPosition().getX() + x,this.getPosition().getY() + y);

        Optional<Entity> pkmn = world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Pokemon.class)));
        Optional<Entity> goal = world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(pokeBall.class)));
        Optional<Entity> exit = world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Gate.class)));//changed

        if (x>0){
            this.setImages(imageStore.getImageList("dudeR"));
        }
        else if(x<0){
            this.setImages(imageStore.getImageList("dudeL"));
        }
        else if(y>0){
            this.setImages(imageStore.getImageList("dude"));
        }
        else if (y<0){
            this.setImages(imageStore.getImageList("dudeU"));
        }

        if (!this.getPosition().equals(nextPos) && !world.isOccupied(nextPos)) {
            world.moveEntity( this, nextPos);
        }

        if (pkmn.isPresent() && pkmn.get().getPosition().adjacent(this.getPosition()) && pokeCount < 1) {
            pokeCount += 1;
            world.removeEntity(pkmn.get());
        }

        if (goal.isPresent() && goal.get().getPosition().adjacent(this.getPosition())  && !((pokeBall)goal.get()).isFull() && pokeCount >= 1) {
            pokeCount -= 1;
            goalCount += 1;
            goal.get().setImages(imageStore.getImageList("pokeMid"));
            goal.get().setImages(imageStore.getImageList("pokeClose"));
            ((pokeBall) goal.get()).setFull();
        }

        if (exit.isPresent() && exit.get().getPosition().adjacent(this.getPosition()) && goalCount == 3 ){
            for (Entity entity : world.getEntities()) {
                scheduler.unscheduleAllEvents(entity);
            }

            VirtualWorld.setEnd(1);
        }
    }
}



