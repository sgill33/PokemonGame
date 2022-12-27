import java.util.Random;

public class allPokemonFactory extends PokemonFactory{

    public Pokemon createPokemon(String id, Point position, ImageStore imageStore, int animationPeriod) {
        Random rand = new Random();
        int num = rand.nextInt(5);

        if (num == 0){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("bulbasaur"));
        }
        else if (num == 1){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("charmander"));
        }
        else if (num == 2){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("squirtle"));
        }
        else if (num == 3){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("pikachu"));
        }
        else{
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("meowth"));
        }
    }
}
