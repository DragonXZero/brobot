package brobot.eggthemall.encounter;

import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;

public class BattleResult {
    public final Map<User, Long> eggRewards;

    public BattleResult() {
        eggRewards = new HashMap<>();
    }

    public Map<User, Long> getEggRewards() {
        return eggRewards;
    }

    public void addEggReward (final User user, final long eggRewardCount) {
        eggRewards.put(user, eggRewardCount);
    }
}
