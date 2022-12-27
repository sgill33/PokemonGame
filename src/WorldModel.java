import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public abstract class WorldModel
{
    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public int getNumRows(){return this.numRows;}
    public int getNumCols(){return this.numCols;}
    public Set<Entity> getEntities(){return this.entities;}


    public boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0
                && pos.getX() < this.numCols;
    }


    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.getY()][pos.getX()];
    }

    public boolean isOccupied(Point pos) {
        return this.withinBounds( pos) && this.getOccupancyCell( pos) != null;
    }

    public Optional<Entity> findNearest(
            Point pos, List<Class> kinds)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind: kinds)
        {
            for (Entity entity : this.entities) {
                if (kind.isInstance(entity)) {
                    ofType.add(entity);
                }
            }
        }
        return pos.nearestEntity(ofType);


    }


    private  void setOccupancyCell(
             Point pos, Entity entity)
    {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }

    public  void addEntity(Entity entity) {
        if (this.withinBounds( entity.getPosition())) {
            this.setOccupancyCell( entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    private void removeEntityAt(Point pos) {
        if (this.withinBounds( pos) && this.getOccupancyCell( pos) != null) {
            Entity entity = this.getOccupancyCell( pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell( pos, null);
        }
    }

    public void removeEntity(Entity entity) {
        this.removeEntityAt( entity.getPosition());
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (this.withinBounds( pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell( oldPos, null);
            this.removeEntityAt( pos);
            this.setOccupancyCell( pos, entity);
            entity.setPosition(pos);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    private Background getBackgroundCell( Point pos) {
        return this.background[pos.getY()][pos.getX()];
    }

    private void setBackgroundCell(
            Point pos, Background background)
    {
        this.background[pos.getY()][pos.getX()] = background;
    }

    void setBackground(
            Point pos, Background background)
    {
        if (this.withinBounds( pos)) {
            this.setBackgroundCell( pos, background);
        }
    }


    void tryAddEntity(Entity entity) {
        if (this.isOccupied( entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity( entity);
    }



    public abstract boolean processLine(
            String line, ImageStore imageStore);


    public void load(
            Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!this.processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }


    public  Optional<PImage> getBackgroundImage(
             Point pos)
    {

        if (this.withinBounds( pos)) {
            return Optional.of(this.getBackgroundCell( pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }
}
