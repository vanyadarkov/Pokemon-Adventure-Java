package Adventure.Pokemons.PokemonUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The expansion of the Pokemon class which means a pokemon in battle, which compared to a peaceful one,
 * needs isStunned, willBeStunned fields (will stun in the next round), isDead, usedItems (whether or not he used his
 * skills), items (list of items he has), currentHP (current health) and score.
 */
public class PokemonForBattle extends Pokemon {

    private final Lock lock = new ReentrantLock();

    private int currentHP;

    private boolean isStunned = false;
    private boolean willBeStunned = false;
    private boolean isDead = false;
    private final List<Item> items;

    private boolean usedItems = false;

    private int score;

    /**
     * He receives a peaceful pokemon, calls the super builder and prepares him for battle.
     * @param pokemon pokemon for battle.
     */
    public PokemonForBattle(@NotNull Pokemon pokemon){
        super(pokemon.getName(), pokemon.getHp(), pokemon.getAttack(), pokemon.getDef(), pokemon.getSpecialDef(), pokemon.getAbilities());
        this.currentHP = pokemon.getHp();
        this.items = new ArrayList<>();
        this.score = 0;
    }

    /**
     * Attack a pokemon with the required skill.
     * @param pokemonToAttack the pokemon we want to attack.
     * @param thisPokemonAttack the attack with which we will attack
     * @param enemyPokemonAttack the attack of the opposing pokemon
     * @param sb the stringBuilder to which we will write the attack log.
     */
    public void attackPokemon(PokemonForBattle pokemonToAttack, Object thisPokemonAttack, Object enemyPokemonAttack, StringBuilder sb){
        lock.lock();
        /*
         * If it is stunned -> resets the stun and signals that it is stunned.
         */
        if(this.isStunned()) {
            this.setWillBeStunned(false);
            this.setStunned(false);
            sb.append(this.getName()).append(" is stunned this round");
            lock.unlock();
            return;
        }
        int damage = 0;
        /*
         * Check if the attack is ability
         */
        if(thisPokemonAttack instanceof Ability ability){
            sb.append(this.getName()) .append(" used ability").append(this.getAbilities().indexOf(ability) + 1).append("\n");
            /*
             * Set stun for the opposing pokemon with the stun of the ability used.
             */
            pokemonToAttack.setWillBeStunned(ability.HasStun());
            damage = ability.getDamage();
            /*
             * Set the current coolDown with the ability cooldown +1 (because at the end of the round it will decrease
             * and return to normal)
             */
            ability.setCurrentCooldown(ability.getCooldown() + 1);
        }
        /*
         * If it is a regular attack (Normal / Special)
         */
        else if(thisPokemonAttack instanceof Attack attack1){
            sb.append(this.getName()).append(" used ").append(attack1.getName()).append("\n");
            damage = attack1.getDamage();
            int def;
            if(attack1.getName().equals("Normal Attack")) {
                def = pokemonToAttack.getDef();
            }
            else {
                def = pokemonToAttack.getSpecialDef();
            }
            /*
             * Check that the opponent's defense for this type of attack is not too big
             */
            if(def > damage){
                damage = 0;
            }
            else {
                damage -= def;
            }
        }
        /*
         * If the opponent's pokemon ability has dodge, and he is not stunned at the moment -> reset the willBeStunned
         * field (because we tried to use the ability above).
         */
        if(enemyPokemonAttack instanceof Ability enemyAbility){
            if(enemyAbility.HasDodge() && !pokemonToAttack.isStunned()){
                pokemonToAttack.setWillBeStunned(false);
                sb.append(pokemonToAttack.getName()).append(" has dodged this attack");
                lock.unlock();
                return;
            }
        }
        /*
         * We show how much damage the opponent received and set the new hp for him.
         */
        sb.append(pokemonToAttack.getName()).append(" ").append(pokemonToAttack.getCurrentHP()).append("-").append(damage);
        pokemonToAttack.setCurrentHP(pokemonToAttack.getCurrentHP() - damage);
        /*
         * If we killed him, we set him as dead (setDead (true))
         */
        if(pokemonToAttack.getCurrentHP() <= 0) {
            pokemonToAttack.setCurrentHP(0);
            pokemonToAttack.setDead(true);
        }

        lock.unlock();
    }

    /**
     * Apply pokemon win bonus (+1 for HP, Attack, Def, SDef)
     */
    public void applyWinBonus(){
        this.setHp(this.getHp() + 1);
        this.getAttack().setDamage(this.getAttack().getDamage() + 1);
        this.setDef(this.getDef() + 1);
        this.setSpecialDef(this.getSpecialDef() + 1);
    }

    /**
     * Calculating the score for pokemon
     */
    public void calculateScore(){
        this.score += this.getCurrentHP();
        this.score += this.getAttack().getDamage();
        this.score += this.getDef();
        this.score += this.getSpecialDef();
    }

    /**
     * Extract and write to stringBuilder features of the pokemon
     * @param sb the stringBuilder where it is written.
     */
    public void getStats(@NotNull StringBuilder sb){
        sb.append("(HP [").append(this.getCurrentHP()).append("]");
        if(this.getAttack().getName().equals("Normal Attack")){
            sb.append(" NA [").append(this.getAttack().getDamage()).append("]");
        }
        else{
            sb.append(" SA [").append(this.getAttack().getDamage()).append("]");
        }
        sb.append(" DEF [").append(this.getDef()).append("]");
        sb.append(" SDEF [").append(this.getSpecialDef()).append("]");
    }

    /**
     * Use the items for the pokemon that will increase its characteristics and write in the string what each item has
     * increased.
     * @return the string that contains the information about the modified fields.
     */
    public String useItems(){
        this.setUsedItems(true);
        lock.lock();
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for(Item i : items){
            if(j != 0) sb.append("\n");
            sb.append(i.getItemBuff());
            if(i.getHp() != 0){
                this.setHp(this.getHp() + i.getHp());
                this.setCurrentHP(this.getHp());
            }
            if(i.getDefense() != 0){
                this.setDef(this.getDef() + i.getDefense());
            }
            if(i.getSpecialDefense() != 0){
                this.setSpecialDef(this.getSpecialDef() + i.getSpecialDefense());
            }
            if(i.getAttack() != 0){
                if(this.getAttack().getName().equals("Normal Attack")){
                    this.getAttack().setDamage(this.getAttack().getDamage() + i.getAttack());
                }
            }
            if(i.getSpecialAttack() != 0){
                if(this.getAttack().getName().equals("Special Attack")){
                    this.getAttack().setDamage(this.getAttack().getDamage() + i.getSpecialAttack());
                }
            }
            j++;
        }

        lock.unlock();
        return sb.toString();
    }

    /**
     * Reset the pokemon to its original state (currentHP, isStunned, isDead, willBeStunned and also the cooldown
     * of the abilities)
     */
    public void resetState(){
        lock.lock();
        this.currentHP = this.getHp();
        this.isStunned = false;
        this.isDead = false;
        this.willBeStunned = false;
        if(!this.getAbilities().isEmpty()) {
            this.getAbilities().get(0).setCurrentCooldown(0);
            this.getAbilities().get(1).setCurrentCooldown(0);
        }
        lock.unlock();
    }

    /**
     * Refreshes the skills cooldown, meaning it decreases it
     */
    public void refreshAbilities(){
        for(Ability a : getAbilities()){
            a.decrementCoolDown();
        }
    }

    /**
     * Write to the string builder the current skills CD
     * @param sb StringBuilder to write to
     */
    public void getAbilitiesCd(StringBuilder sb){
        lock.lock();
        if(this.getAbilities().isEmpty()) {
            sb.append("No abilities found");
            lock.unlock();
            return;
        }

        sb.append("CDs :");
        Ability ab = this.getAbilities().get(0);
        sb.append(" Ab1 [").append(ab.getCurrentCooldown()).append("]");

        ab = this.getAbilities().get(1);
        sb.append(" Ab2 [").append(ab.getCurrentCooldown()).append("]");

        lock.unlock();
    }

    /*
     * Getters and setters
     */
    public boolean getWillBeStunned() {
        return willBeStunned;
    }

    public void setWillBeStunned(boolean willBeStunned) {
        this.willBeStunned = willBeStunned;
    }

    public boolean usedItems() {
        return usedItems;
    }

    public void setUsedItems(boolean usedItems) {
        this.usedItems = usedItems;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public void setStunned(boolean stunned) {
        isStunned = stunned;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public boolean isStunned() {
        return isStunned;
    }

    public boolean isDead() {
        return isDead;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

}
