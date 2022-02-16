package Adventure.Pokemons;

import Adventure.Pokemons.PokemonUtils.Ability;
import Adventure.Pokemons.PokemonUtils.Attack;
import Adventure.Pokemons.PokemonUtils.Pokemon;

import java.util.List;

public class Charmander extends Pokemon {
    public Charmander(String name, int hp, Attack attack, int def, int specialDef, List<Ability> abilities) {
        super(name, hp, attack, def, specialDef, abilities);
    }
}
