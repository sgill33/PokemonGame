import processing.core.PApplet;
import processing.core.PImage;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public final class WorldView
{
    private PApplet screen;
    private WorldModel world;
    private int tileWidth;
    private int tileHeight;
    private Viewport viewport;
    private int numRows;
    private int numCols;
    private ImageStore imageStore;

    public Viewport getViewport(){return this.viewport;}
    public WorldView(
            int numRows,
            int numCols,
            PApplet screen,
            WorldModel world,
            int tileWidth,
            int tileHeight, ImageStore imageStore)
    {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
        this.numRows = numRows;
        this.numCols = numCols;
        this.imageStore = imageStore;

    }

    private static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public void shiftView( int colDelta, int rowDelta) {
        int newCol = this.clamp(this.viewport.getCol() + colDelta, 0,
                this.world.getNumCols() - this.viewport.getNumCols());
        int newRow = this.clamp(this.viewport.getRow() + rowDelta, 0,
                this.world.getNumRows() - this.viewport.getNumRows());

        this.viewport.shift(newCol, newRow);
    }

    private void drawBackground() {
        for (int row = 0; row < this.viewport.getNumRows(); row++) {
            for (int col = 0; col < this.viewport.getNumCols(); col++) {
                Point worldPoint = this.viewport.viewportToWorld( col, row);
                Optional<PImage> image =
                        this.world.getBackgroundImage( worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth,
                            row * this.tileHeight);
                }
            }
        }
    }

    private void drawEntities() {
        for (Entity entity : this.world.getEntities()) {
            Point pos = entity.getPosition();

            if (this.viewport.contains( pos)) {
                Point viewPoint = this.viewport.worldToViewport( pos.getX(), pos.getY());
                this.screen.image(entity.getCurrentImage(),
                        viewPoint.getX() * this.tileWidth,
                        viewPoint.getY() * this.tileHeight);
            }
        }
    }

    public void drawStart() {
        List<PImage> imgs = imageStore.getImageList("start");
        PImage img = imgs.get(0);
        screen.image(img,-60,0);

        screen.textSize(30);
        screen.fill(0, 0, 0);
        screen.text("Press Space to Start", 185, 225);

    }

    public void drawEnd1() {
        screen.textSize(50);
        screen.fill(0, 255, 0);
        screen.text("YOU WIN!!! ", 180, 250);
    }

    public void drawEnd2() {
        screen.textSize(50);
        screen.fill(255, 0, 0);
        screen.text("YOU LOSE :(", 180, 250);
    }

    public void drawSelect() {
        PImage img = screen.createImage(800, 800, screen.RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = screen.color(195, 206, 160);
        }
        img.updatePixels();
        screen.image(img, 0, 0);

        screen.textSize(25);
        screen.fill(0, 0, 0);
        screen.text("Press Key(1-4) to pick Wild Pokemon Looks", 30, 50);
        screen.text("1. Starter Pokemon", 30, 100);
        screen.text("2. All Pokemon", 30, 150);
        screen.text("3. Just Pikachu", 30, 200);
        screen.text("4. Any Entity", 30, 250);
    }

    public void drawDiff() {
        PImage img = screen.createImage(800, 800, screen.RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = screen.color(195, 206, 160);
        }
        img.updatePixels();
        screen.image(img, 0, 0);

        screen.textSize(25);
        screen.fill(0, 0, 0);
        screen.text("Press Key(1-3) to pick Difficulty", 30, 50);
        screen.text("1. Easy Difficulty", 30, 100);
        screen.text("2. Medium Difficulty", 30, 150);
        screen.text("3. Hard Difficulty", 30, 200);
    }

    public void drawViewport() {
        this.drawBackground();
        this.drawEntities();
    }
}
