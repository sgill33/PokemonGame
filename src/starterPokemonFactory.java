import java.util.Random;

public class starterPokemonFactory extends PokemonFactory{

    public Pokemon createPokemon(String id, Point position, ImageStore imageStore, int animationPeriod) {
        Random rand = new Random();
        int num = rand.nextInt(3);

        if (num == 0){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("bulbasaur"));
        }
        else if (num == 1){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("charmander"));
        }
        else{
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("squirtle"));
        }
    }
}
