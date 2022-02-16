package Adventure.Pokemons.PokemonUtils;

/**
 * The class that stores information about a pokemon's ability
 */
public class Ability {
    private final int damage;
    private final boolean hasStun;
    private final boolean hasDodge;
    private final int cooldown;
    private int currentCooldown;

    public Ability(int damage, boolean hasStun, boolean hasDodge, int cooldown) {
        this.damage = damage;
        this.hasStun = hasStun;
        this.hasDodge = hasDodge;
        this.cooldown = cooldown;
        currentCooldown = 0;
    }

    /**
     * Decreases the current ability cooldown. If it is different from zero, it decreases by 1.
     */
    public void decrementCoolDown(){
        if(this.getCurrentCooldown() != 0){
            this.setCurrentCooldown(this.getCurrentCooldown() - 1);
        }
    }

    /**
     * Sets the current cooldown of the ability.
     * @param currentCooldown the cooldown that needs to be set
     */
    public void setCurrentCooldown(int currentCooldown) {
        this.currentCooldown = currentCooldown;
    }

    public int getDamage() {
        return damage;
    }

    public boolean HasStun() {
        return hasStun;
    }

    public boolean HasDodge() {
        return hasDodge;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }
}
