package brobot.eggthemall.monster;

import brobot.ResponseObject;
import brobot.eggthemall.castle.Castle;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Monster {
    protected final String name;
    protected final String spritePath;
    protected final long health;
    protected final long attack;
    protected final long defense;
    protected final long eggRewardAmount;

    protected transient Map<User, Long> damageReceivedMap;
    protected transient long currentHealth;
    protected transient boolean alive;

    public Monster(final String name, final String spritePath, final long health, final long attack, final long defense, final long eggRewardAmount) {
        this.name = name;
        this.spritePath = spritePath;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.eggRewardAmount = eggRewardAmount;

        this.damageReceivedMap = new HashMap<>();
        this.currentHealth = health;
        this.alive = true;
    }

    public String getName() {
        return name;
    }

    public String getSpritePath() {
        return spritePath;
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

    public long getEggRewardAmount() {
        return eggRewardAmount;
    }

    public Map<User, Long> getDamageReceivedMap() {
        return damageReceivedMap;
    }

    public long getCurrentHealth() {
        return currentHealth;
    }

    public boolean isAlive() {
        return alive;
    }

    public long calculateDamageTaken(final User attacker, final long attackValue) {
        final long adjustedDamage = attackValue - defense;
        final long damageTaken = adjustedDamage > 0 ? (adjustedDamage > currentHealth ? currentHealth : adjustedDamage) : 0;
        final long totalDamageDealt = damageReceivedMap.getOrDefault(attacker, 0l) + damageTaken;
        damageReceivedMap.put(attacker, totalDamageDealt);
        currentHealth -= damageTaken;

        if (currentHealth == 0) {
            die();
        }
        return damageTaken;
    }

    public long calculateDamageDealt(final long defenseValue) {
        Random random = new Random();
        long damageDealt = attack * random.nextInt(10) - defenseValue * random.nextInt(5);
        return damageDealt;
    }

    public void die() {
        alive = false;
    }

    @Override
    public String toString() {
        return "\t**" + this.name + "** \n\t\t:heart: - " + currentHealth + " :crossed_swords: - " + attack + " :shield: - " + defense + "\n";
    }
}
