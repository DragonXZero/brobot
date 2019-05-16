package brobot.eggthemall.encounter.monster;

public class Monster {
    private final String name;
    private final long health;
    private final long attack;
    private final long defense;

    public Monster(final String name, final long health, final long attack, final long defense) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
    }

    public String getName() {
        return name;
    }

    public long getHealth() {
        return health;
    }

    public long getAttack() {
        return attack;
    }

    public long getDefense() {
        return defense;
    }

    public void die() {
    }

    @Override
    public String toString() {
        return "\t**" + this.name + "** \n\t\t:heart: - " + health + ":crossed_swords: - " + attack + ":shield: - " + defense + "\n";
    }
}
