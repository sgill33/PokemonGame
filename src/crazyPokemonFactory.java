import java.util.Random;

public class crazyPokemonFactory extends PokemonFactory{

    public Pokemon createPokemon(String id, Point position, ImageStore imageStore, int animationPeriod) {
        Random rand = new Random();
        int num = rand.nextInt(10);

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
        else if (num == 4){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("jesse"));
        }
        else if (num == 5){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("james"));
        }
        else if (num == 6){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("dude"));
        }
        else if (num == 7){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("pokeClose"));
        }
        else if (num == 8){
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("gate"));
        }
        else{
            return Pokemon.create(id,position,animationPeriod,imageStore.getImageList("meowth"));
        }
    }
}
