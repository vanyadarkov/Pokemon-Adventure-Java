package Adventure.Pokemons.PokemonUtils;

import Adventure.Pokemons.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The pokemon factory represented as Singleton.
 */
public class PokemonFactory {

    private static PokemonFactory factory;

    private PokemonFactory() {}

    public static PokemonFactory getFactory(){
        if(factory == null) factory = new PokemonFactory();
        return factory;
    }

    /**
     * Creating the pokemon. It gets the name of the pokemon, and depending on that it creates me with the necessary
     * specifications
     *
     * @param pokemonType tipul pokemonului cerut
     * @return Obiect de tip pokemon
     */
    public Pokemon getPokemon(@NotNull String pokemonType){

        switch (pokemonType){
            case "Neutrel1":{
                Attack attack = new Attack("Normal Attack", 3);
                List<Ability> abilities = Collections.emptyList();
                return new Neutrel1("Neutrel1", 10, attack, 1, 1, abilities);
            }
            case "Neutrel2":{
                Attack attack = new Attack("Normal Attack", 4);
                List<Ability> abilities = Collections.emptyList();
                return new Neutrel2("Neutrel2", 20, attack, 1, 1, abilities);
            }
            case "Pikachu":{
                Attack attack = new Attack("Special Attack", 4);
                List<Ability> abilities = new ArrayList<>();
                abilities.add(new Ability(6, false, false, 4));
                abilities.add(new Ability(4, true, true, 5));
                return new Pikachu("Pikachu", 35, attack, 2, 3, abilities);
            }
            case "Bulbasaur":{
                Attack attack = new Attack("Special Attack", 5);
                List<Ability> abilities = new ArrayList<>();
                abilities.add(new Ability(6, false, false, 4));
                abilities.add(new Ability(5, false, false, 3));
                return new Bulbasaur("Bulbasaur", 42, attack, 3, 1, abilities);
            }
            case "Charmander":{
                Attack attack = new Attack("Normal Attack", 4);
                List<Ability> abilities = new ArrayList<>();
                abilities.add(new Ability(4, true, false, 4));
                abilities.add(new Ability(7, false, false, 6));
                return new Charmander("Charmander", 50, attack, 3, 2, abilities);
            }
            case "Squirtle":{
                Attack attack = new Attack("Special Attack", 3);
                List<Ability> abilities = new ArrayList<>();
                abilities.add(new Ability(4, false, false, 3));
                abilities.add(new Ability(2, true, false, 2));
                return new Squirtle("Squirtle", 60, attack, 5, 5, abilities);
            }
            case "Snorlax":{
                Attack attack = new Attack("Normal Attack", 3);
                List<Ability> abilities = new ArrayList<>();
                abilities.add(new Ability(4, true, false, 5));
                abilities.add(new Ability(0, false, true, 5));
                return new Snorlax("Snorlax", 62, attack, 6, 4, abilities);
            }
            case "Vulpix":{
                Attack attack = new Attack("Normal Attack", 5);
                List<Ability> abilities = new ArrayList<>();
                abilities.add(new Ability(8, true, false, 6));
                abilities.add(new Ability(2, false, true, 7));
                return new Vulpix("Vulpix", 36, attack, 2, 4, abilities);
            }
            case "Eevee":{
                Attack attack = new Attack("Special Attack", 4);
                List<Ability> abilities = new ArrayList<>();
                abilities.add(new Ability(5, false, false, 3));
                abilities.add(new Ability(3, true, false, 3));
                return new Eevee("Eevee", 39, attack, 3, 3, abilities);
            }
            case "Jigglypuff":{
                Attack attack = new Attack("Normal Attack", 4);
                List<Ability> abilities = new ArrayList<>();
                abilities.add(new Ability(4, true, false, 4));
                abilities.add(new Ability(3, true, false, 4));
                return new Jigglypuff("Jigglypuff", 34, attack, 2, 3, abilities);
            }
            case "Meowth":{
                Attack attack = new Attack("Normal Attack", 3);
                List<Ability> abilities = new ArrayList<>();
                abilities.add(new Ability(5, false, true, 4));
                abilities.add(new Ability(1, false, true, 3));
                return new Meowth("Meowth", 41, attack, 4, 2, abilities);
            }
            case "Psyduck":{
                Attack attack = new Attack("Normal Attack", 3);
                List<Ability> abilities = new ArrayList<>();
                abilities.add(new Ability(2, false, false, 4));
                abilities.add(new Ability(2, true, false, 5));
                return new Psyduck("Psyduck", 43, attack, 3, 3, abilities);
            }
            default: return null;
        }

    }
}
