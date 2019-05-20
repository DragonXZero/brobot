package brobot.eggthemall.monster;

public class Pokemon extends Monster {
    private final int specialAttack;
    private final int specialDefense;
    private final int speed;

    public Pokemon(final String name, final String spritePath, long health, long attack, long defense,
                   final int specialAttack, final int specialDefense, final int speed, long eggRewardAmount) {
        super(name, spritePath, health, attack, defense, eggRewardAmount);
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
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

    @Override
    public String toString() {
        return "**" + this.name + "** \n\t:heart: - " + currentHealth + " :crossed_swords: - " + attack + " :shield: - " + defense +
                " :dizzy: - " + specialAttack + " :star2: - " + specialDefense + " :comet: - " + speed + "\n";
    }
}
