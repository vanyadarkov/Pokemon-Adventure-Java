package Adventure.Pokemons.PokemonUtils;

import java.util.List;
import java.util.Objects;

/**
 * The abstract class that carries the meaning of a peaceful pokemon that is not in battle.
 */
public abstract class Pokemon {
    private final String name;
    private int hp;
    private final Attack attack;
    private int def;
    private int specialDef;
    private final List<Ability> abilities;

    public Pokemon(String name, int hp, Attack attack, int def, int specialDef, List<Ability> abilities) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.def = def;
        this.specialDef = specialDef;
        this.abilities = abilities;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public Attack getAttack() {
        return attack;
    }

    public int getDef() {
        return def;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public void setSpecialDef(int specialDef) {
        this.specialDef = specialDef;
    }

    public int getSpecialDef() {
        return specialDef;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    /**
     * Check the equality with a pokemon. Only the name field is checked.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return Objects.equals(name, pokemon.name);
    }

    @Override
    public String toString() {
        return "\t\t\tPokemon{" + "\n\t\t\tname='" + name + '\'' +
                "\n\t\t\thp=" + hp +
                "\n\t\t\tattack=" + attack +
                "\n\t\t\tdef=" + def +
                "\n\t\t\tspecialDef=" + specialDef +
                "\n\t\t\tabilities=" + abilities +
                "\n\t\t\t}";
    }
}
