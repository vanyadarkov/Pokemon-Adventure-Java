package Adventure.Pokemons.PokemonUtils;

/**
 * Item Builder.
 */
public class ItemBuilder {
    private String name;
    private int hp;
    private int attack;
    private int specialAttack;
    private int defense;
    private int specialDefense;

    public static ItemBuilder builder;

    private ItemBuilder() {
    }

    /**
     * Return the only builder instance, because it's Singleton.
     * @return ItemBuilder instant
     */
    public static ItemBuilder getBuilder(){
        if(builder == null) builder = new ItemBuilder();
        return builder;
    }

    /**
     * Build an item named name
     * @param name the name of the item required for construction
     * @return Item Requested.
     */
    public Item buildItem(String name){
        return switch (name) {
            case "Shield" ->  this.setName("Shield")
                            .setDefense(2)
                            .setSpecialDefense(2)
                            .build();
            case "Vest" -> this.setName("Vest")
                            .setHp(10)
                            .build();
            case "Sword" ->   this.setName("Sword")
                                .setAttack(3)
                                .build();
            case "Magic wand" ->    this.setName("Magic wand")
                                        .setSpecialAttack(3)
                                        .build();
            case "Vitamins" ->  this.setName("Vitamins")
                                .setHp(2)
                                .setAttack(2)
                                .setSpecialAttack(2)
                                .build();
            case "Christmas tree" ->   this.setName("Christmas tree")
                                        .setAttack(3)
                                        .setDefense(1)
                                        .build();
            case "Cape" ->  this.setName("Cape")
                                .setSpecialDefense(3)
                                .build();
            default -> null;
        };
    }

    /*
     * Here are the settings for the required fields. All of them return to ItemBuilder in order to be able to make a
     * series of sets, necessary to keep the Builder concept.
     */

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder setHp(int hp) {
        this.hp = hp;
        return this;
    }

    public ItemBuilder setAttack(int attack) {
        this.attack = attack;
        return this;
    }

    public ItemBuilder setSpecialAttack(int specialAttack) {
        this.specialAttack = specialAttack;
        return this;
    }

    public ItemBuilder setDefense(int defense) {
        this.defense = defense;
        return this;
    }

    public ItemBuilder setSpecialDefense(int specialDefense) {
        this.specialDefense = specialDefense;
        return this;
    }

    /**
     * Constructs an object from the set fields. After construction, the fields are reset to 0 so that another object
     * can be built further
     * @return created Item
     */
    public Item build(){
        Item item = new Item(this);
        this.name = null;
        this.hp = 0;
        this.attack = 0;
        this.specialAttack = 0;
        this.defense = 0;
        this.specialDefense = 0;
        return item;
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
