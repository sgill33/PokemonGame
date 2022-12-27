import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 630;
    private static final int VIEW_HEIGHT = 462;
    private static final int TILE_WIDTH = 21;
    private static final int TILE_HEIGHT = 21;
    private static final int WORLD_WIDTH_SCALE = 1;
    private static final int WORLD_HEIGHT_SCALE = 1;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;

    private Player player;
    private Pikachu pika;

    private boolean start = false;
    private boolean wild = false;
    private boolean diff = false;

    private static int end = 0;

    private static int pikaSpeed = 0;
    private Point exit = new Point(15,11);

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    public static void setEnd(int end) {
        VirtualWorld.end = end;
    }

    /*
           Processing entry point for "sketch" setup.
        */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new Parser(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT,imageStore);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        player = new Player("dude1",exit,imageStore.getImageList("dude"),this);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime( time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        if (!start){
            view.drawStart();
        }
        else{
            if(!wild){
                view.drawSelect();
            }
            else{
                if(!diff){
                    view.drawDiff();
                }
                else{
                    if(end == 0){
                        view.drawViewport();
                    }
                    else if(end == 1){
                        view.drawViewport();
                        view.drawEnd1();
                        world.removeEntity(player);
                    }
                    else{
                        view.drawEnd2();
                    }
                }
            }
        }

    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        pika = Pikachu.create("pika",new Point(pressed.getX(),pressed.getY()),imageStore.getImageList("pikUp"),pikaSpeed,pikaSpeed);
        if(pika.getCount() < 1 && end == 0 && diff && !world.isOccupied(pressed)){
            world.tryAddEntity(pika);
            pika.incCount();

            Point p = new Point(pressed.getX() + 1, pressed.getY());
            if(!world.isOccupied(p)){ world.setBackground(p,new Background ("flowers",imageStore.getImageList("flower")));}
            p = new Point(pressed.getX() - 1, pressed.getY());
            if(!world.isOccupied(p)){ world.setBackground(p,new Background ("flowers",imageStore.getImageList("flower")));}
            p = new Point(pressed.getX(), pressed.getY() + 1);
            if(!world.isOccupied(p)){ world.setBackground(p,new Background ("flowers",imageStore.getImageList("flower")));}
            p =new Point(pressed.getX(), pressed.getY() - 1);
            if(!world.isOccupied(p)){ world.setBackground(p,new Background ("flowers",imageStore.getImageList("flower")));}

            Optional<Entity> target = world.findNearest( pressed, new ArrayList<>(Arrays.asList(James.class)));

            if(target.isPresent()){
                    Meowth entity = Meowth.create(target.get().getId(),
                            target.get().getPosition(),
                            ((Enemy)target.get()).getActionPeriod(),
                            ((Enemy)target.get()).getAnimationPeriod(),
                            imageStore.getImageList("meowDown"),
                            ((Enemy)target.get()).getMovement());

                    world.removeEntity( target.get());
                    scheduler.unscheduleAllEvents(target.get());
                    world.addEntity( entity);

                scheduler.scheduleEvent(entity,entity.createActivityAction(world,imageStore), entity.getActionPeriod());
                scheduler.scheduleEvent(entity,entity.createAnimationAction(entity.getAnimationPeriod()),entity.getAnimationPeriod());
            }


            scheduler.scheduleEvent(pika,pika.createActivityAction(world,imageStore), pika.getActionPeriod());
            scheduler.scheduleEvent(pika,pika.createAnimationAction(pika.getAnimationPeriod()),pika.getAnimationPeriod());
        }
    }

    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld( mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }

    public void keyPressed() {
        Optional<Entity> target = world.findNearest( new Point(0,0), new ArrayList<>(Arrays.asList(Player.class)));
        if (key == CODED && target.isPresent()) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            player.move(dx,dy,world,imageStore,scheduler);
        }
        else if (key == 32) {
            start = true;
        }
        else if(!wild && key<= 52 && key >= 49){
            PokemonFactory PF = null;

            if (key == 49  ) {
                PF = new starterPokemonFactory();
            }
            else if (key == 50  ) {
                PF = new allPokemonFactory();
            }
            else if (key == 51  ) {
                PF = new pikachuPokemonFactory();
            }
            else if (key == 52) {
                PF = new crazyPokemonFactory();
            }


            Pokemon entity1 = PF.createPokemon("topL",new Point(2,2),imageStore,2000);
            Pokemon entity2 = PF.createPokemon("topR",new Point(27,2),imageStore,1800);
            Pokemon entity3 = PF.createPokemon("botR",new Point(27,19),imageStore,1500);

            world.tryAddEntity(entity1);
            world.tryAddEntity(entity2);
            world.addEntity(entity3);

            scheduler.scheduleEvent(entity1,entity1.createAnimationAction(entity1.getAnimationPeriod()),entity1.getAnimationPeriod());
            scheduler.scheduleEvent(entity2,entity2.createAnimationAction(entity2.getAnimationPeriod()),entity2.getAnimationPeriod());
            scheduler.scheduleEvent(entity3,entity3.createAnimationAction(entity3.getAnimationPeriod()),entity3.getAnimationPeriod());

            wild = true;

        }
        else if(!diff && key >= 49 && key <= 51){

            Function<Point, Stream<Point>> movement = null;
            int action1 = 0;
            int action2 = 0;
            if (key == 49  ) {
                movement = PathingStrategy.CARDINAL_NEIGHBORS;
                action1 = 500;
                action2 = 650;
                pikaSpeed = 50;
            }
            else if (key == 50  ) {
                movement = PathingStrategy.CARDINAL_NEIGHBORS;
                action1 = 350;
                action2 = 500;
                pikaSpeed = 50;
            }
            else if (key == 51) {
                movement = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS;
                action1 = 325;
                action2 = 425;
                pikaSpeed = 75;
            }

            Jesse jess = Jesse.create("jesse",new Point(2,18),action1,action1-100,imageStore.getImageList("jessDown"),movement);
            James james = James.create("james",new Point(28,12),action2,action2-150,imageStore.getImageList("jamesDown"),movement);

            scheduler.scheduleEvent(jess,jess.createAnimationAction(jess.getAnimationPeriod()),jess.getAnimationPeriod());
            scheduler.scheduleEvent(james,james.createAnimationAction(james.getAnimationPeriod()),james.getAnimationPeriod());

            scheduler.scheduleEvent(jess,jess.createActivityAction(world,imageStore), jess.getActionPeriod());
            scheduler.scheduleEvent(james,james.createActivityAction(world,imageStore), james.getActionPeriod());

            world.tryAddEntity(jess);
            world.tryAddEntity(james);
            world.tryAddEntity(player);

            diff = true;
        }

    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                            imageStore.getImageList(
                                                     DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ActiveEntity){
                ((ActiveEntity)entity).scheduleActions( scheduler, world, imageStore);      //type cast
            }
            else if(entity instanceof Pokemon){
                ((AnimatedEntity)entity).scheduleActions( scheduler, world, imageStore);      //type cast
            }
        }
    }

    public static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
