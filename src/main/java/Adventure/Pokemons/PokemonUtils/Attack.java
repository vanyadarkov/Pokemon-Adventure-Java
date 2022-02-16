package Adventure.Pokemons.PokemonUtils;

/**
 * The class that stores pokemon attack information.
 * name -> Special / Normal attack
 */
public class Attack{
    private final String name;
    private int damage;

    public Attack(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

}
