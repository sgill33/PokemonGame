public class pikachuPokemonFactory extends PokemonFactory{

    public Pokemon createPokemon(String id, Point position, ImageStore imagestore, int animationPeriod) {
        return Pokemon.create(id,position,animationPeriod,imagestore.getImageList("pikachu"));
    }
}
