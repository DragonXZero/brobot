package brobot.eggthemall.encounter;

import brobot.ResponseObject;
import brobot.eggthemall.EggMessages;
import brobot.eggthemall.EggUtils;
import brobot.eggthemall.monster.Monster;
import net.dv8tion.jda.core.entities.User;

import java.util.Map;

public class Encounter {
    private final Monster monster;
    private long duration;

    public Encounter (final Monster monster, long duration) {
        this.monster = monster;
        this.duration = duration;
    }

    public Monster getMonster() {
        return monster;
    }

    public BattleResult processAttack(final ResponseObject responseObject, final User attacker, final long damage) {
        final long damageDealt = monster.resolveAttack(attacker, damage);
        if (damageDealt == 0) {
            responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ENCOUNTER_FIGHT_FAIL_NO_DAMAGE, attacker.getName()));
        } else {
            responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ENCOUNTER_FIGHT_SUCCESS, attacker.getName(), damageDealt,
                    monster.getName(), monster.getName(), monster.getCurrentHealth()));
        }

        final BattleResult battleResult;
        if (monster.isAlive()) {
            long damageDealtToAttacker = monster.attack(responseObject, attacker);
            battleResult = new BattleResult(true, damageDealtToAttacker);
        } else {
            battleResult = new BattleResult(false, -1);
            final double totalEggRewards = monster.getEggRewardAmount();
            for (Map.Entry<User, Long> entry : monster.getDamageReceivedMap().entrySet()) {
                final long eggReward = (long) ((((double) entry.getValue()) / monster.getHealth()) * totalEggRewards);
                battleResult.addEggReward(entry.getKey(), eggReward);
            }
        }

        return battleResult;
    }
}
