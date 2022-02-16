package Adventure.Pokemons.PokemonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The class of a pokemon trainer
 */
public class Coach {
    private final Lock lock = new ReentrantLock();
    private final String name;
    private final int age;
    private final List<PokemonForBattle> pokemons;

    private PokemonForBattle bestPokemon;

    public Coach(String name, int age) {
        this.name = name;
        this.age = age;
        pokemons = new ArrayList<>();
        bestPokemon = null;
    }

    public PokemonForBattle getBestPokemon() {
        return bestPokemon;
    }

    /**
     * Set a coach's best pokemon.
     * @param bestPokemon the potential pokemon to set as best for Coach
     */
    public void setBestPokemon(PokemonForBattle bestPokemon) {
        lock.lock();
        /*
         * If you don't have a best pokemon yet -> set it to the required one
         */
        if(this.bestPokemon == null){
            this.bestPokemon = bestPokemon;
        }
        /*
         * Otherwise -> check score. If this current has a lower score -> exchange it with the one received as a parameter.
         * If they have an equal score -> put the one that appears first in the lexicography.
         */
        else {
            if(this.getBestPokemon().getScore() < bestPokemon.getScore())
                this.bestPokemon = bestPokemon;
            else if(this.getBestPokemon().getScore() == bestPokemon.getScore()){
                int res = this.getName().compareTo(bestPokemon.getName());
                if(res >= 0 ) this.bestPokemon = bestPokemon;
            }
        }
        lock.unlock();
    }

    public void addPokemon(PokemonForBattle pokemon){
        pokemons.add(pokemon);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<PokemonForBattle> getPokemons() {
        return pokemons;
    }

    /**
     * Add the item to the pokemon. Check if the trainer has this pokemon, and if so check if it contains / does not
     * contain an item asked for. If the pokemon does not have an item, add it.
     * @param pokemon the pokemon we add
     * @param item the necessary item to add
     */
    public void addItemForPokemon(PokemonForBattle pokemon, Item item){
        if(pokemons.contains(pokemon)) {
            List<Item> items = pokemon.getItems();
            if(!items.contains(item)) items.add(item);
        }
    }

    /**
     * Returns a list of possible pokemon attacks for this trainer.
     * If you want to add an ability, check if the currentCooldown is 0.
     * @param pokemon the pokemon for which we want to get the possible attacks.
     * @return the list of objects that can actually be Attack / Ability.
     */
    public List<Object> getPossibleAttacks(PokemonForBattle pokemon){
        lock.lock();
        List<Object> attacks = new ArrayList<>();
        attacks.add(pokemon.getAttack());
        for(Ability ability : pokemon.getAbilities()){
            if(ability.getCurrentCooldown() == 0){
                attacks.add(ability);
            }
        }
        lock.unlock();
        return attacks;
    }

    @Override
    public String toString() {
        String sb = "\nCoach{" + "\n\tname='" + name + '\'' +
                "\n\tage=" + age +
                "\n\tpokemons=" + pokemons +
                "\n\tbestPokemon=" + bestPokemon +
                "\n\t}";
        return sb;
    }
}
