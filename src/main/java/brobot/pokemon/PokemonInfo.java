package brobot.pokemon;

public class PokemonInfo {
    private final String pokedexNumber;
    private final String name;
    private final int hp;
    private final int attack;
    private final int defense;
    private final int specialAttack;
    private final int specialDefense;
    private final int speed;

    public PokemonInfo(final String pokedexNumber, final String name, final int hp, final int attack, final int defense,
                       final int specialAttack, final int specialDefense, final int speed) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
    }

    public String getPokedexNumber() {
        return pokedexNumber;
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

    public int getDefense() {
        return defense;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public int getSpeed() {
        return speed;
    }
}
