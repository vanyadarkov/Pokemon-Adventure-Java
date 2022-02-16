package Adventure.Pokemons.PokemonUtils;

import java.util.Objects;

/**
 * The class that will contain the information about an item, ie what characteristics it will increase for a pokemon.
 */
public class Item {
    private final String name;
    private final int hp;
    private final int attack;
    private final int specialAttack;
    private final int defense;
    private final int specialDefense;

    /**
     * Item Constructor. Receive itemBuilder from where to extract the information.
     * @param itemBuilder
     */
    public Item(ItemBuilder itemBuilder) {
        this.name = itemBuilder.getName();
        this.hp = itemBuilder.getHp();
        this.attack = itemBuilder.getAttack();
        this.specialAttack = itemBuilder.getSpecialAttack();
        this.defense = itemBuilder.getDefense();
        this.specialDefense = itemBuilder.getSpecialDefense();
    }

    /**
     * Returns a string with the specifications that increase this item. Only fields are considered
     * which are not set to 0.
     * @return the string with item information
     */
    public String getItemBuff(){
        StringBuilder sb = new StringBuilder();
        sb.append("->").append(this.name);
        sb.append(" (");
        if(this.hp != 0) sb.append(" +").append(this.hp).append(" HP");
        if(this.attack != 0) sb.append(" +").append(this.attack).append(" Attack");
        if(this.specialAttack != 0) sb.append(" +").append(this.specialAttack).append(" SpecialAttack");
        if(this.defense != 0) sb.append(" +").append(this.defense).append(" Defense");
        if(this.specialDefense != 0) sb.append(" +").append(this.specialDefense).append(" SpecialDefense");
        sb.append(" )");
        return sb.toString();
    }

    /**
     * Check the equality of this item with the one received as a parameter. Only the name field is considered
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\n\t\t\tItem{");
        sb.append("\n\t\t\tname='").append(name).append('\'');
        if(hp != 0) sb.append("\n\t\t\thp=").append(hp);
        if(attack != 0) sb.append("\n\t\t\tattack=").append(attack);
        if(specialAttack != 0) sb.append("\n\t\t\tspecialAttack=").append(specialAttack);
        if(defense != 0) sb.append("\n\t\t\tdefense=").append(defense);
        if(specialDefense != 0) sb.append("\n\t\t\tspecialDefense=").append(specialDefense);
        if(hp != 0) sb.append("\n\t\t\t}");
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }
}
