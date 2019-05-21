package brobot.eggthemall.encounter;

import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;

public class FightResult {
    private final Map<User, Long> eggRewards;
    private final Boolean monsterAlive;
    private long damageDealt;

    public FightResult(final boolean monsterAlive, final long damageDealt) {
        this.monsterAlive = monsterAlive;
        this.eggRewards = new HashMap<>();
        this.damageDealt = damageDealt;
    }

    public Map<User, Long> getEggRewards() {
        return eggRewards;
    }

    public void addEggReward (final User user, final long eggRewardCount) {
        eggRewards.put(user, eggRewardCount);
    }

    public boolean isMonsterAlive() {
        return monsterAlive;
    }

    public long getDamageDealt() {
        return damageDealt;
    }
}
