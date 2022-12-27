import processing.core.PImage;

import java.util.List;

public abstract class PokemonFactory {

    public abstract Pokemon createPokemon(String id, Point position, ImageStore imagestore, int animationPeriod);


}
