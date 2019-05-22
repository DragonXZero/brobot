package brobot;

import brobot.eggthemall.EggConstants;
import brobot.eggthemall.EggMessages;
import brobot.eggthemall.EggUtils;
import brobot.eggthemall.castle.Castle;
import brobot.eggthemall.egg.EggType;
import brobot.eggthemall.encounter.Encounter;
import brobot.eggthemall.encounter.EncounterGenerator;
import brobot.eggthemall.encounter.FightResult;
import brobot.eggthemall.monster.Monster;
import net.dv8tion.jda.core.entities.User;

import java.util.LinkedHashMap;
import java.util.Map;

public class BrobotWorld {
    private final Map<User, Castle> castles;
    private Encounter currentEncounter;

    public BrobotWorld() {
        castles = new LinkedHashMap<>();
    }

    public Map<User, Castle> getCastles() {
        return castles;
    }

    public Castle getCastle(final User user) {
        return createCastleIfNotCreated(user);
    }

    public Castle createCastleIfNotCreated(final User user) {
        final Castle castle;
        if (castles.containsKey(user)) {
            castle = castles.get(user);
        } else {
            castle = new Castle(user);
            castles.put(user, castle);
        }
        return castle;
    }

    public void startEncounter(final ResponseObject responseObject) {
        if (currentEncounter != null) {
            responseObject.addMessage(EggMessages.ENCOUNTER_ALREADY_IN_AN_ENCOUTER);
        } else {
            currentEncounter = EncounterGenerator.generateRandomEncounter();
            final Monster monster = currentEncounter.getMonster();
            final String encounterMessage = "A wild **" + monster.getName() + "** has appeared!\n" + monster.toString();
            responseObject.addMessage(encounterMessage);
            responseObject.addImage(monster.getImagePath());
//            responseObject.addMessage("What will you do?\n\t**1.** Attempt to seduce. \n\t**2.** Unzip. \n\t**3. **Admire.\n");
        }
    }

    public Encounter getCurrentEncounter(final ResponseObject responseObject) {
        if (currentEncounter == null) {
            responseObject.addMessage(EggMessages.ENCOUNTER_NO_ENCOUNTER);
        }
        return currentEncounter;
    }

    public void processFight(final ResponseObject responseObject, final User attacker) {
        if (currentEncounter == null) {
            responseObject.addMessage(EggMessages.ENCOUNTER_NO_ENCOUNTER);
        } else {
            final Castle castle = getCastle(attacker);
            FightResult fightResult = currentEncounter.resolveFight(responseObject, attacker, castle);
            if (!fightResult.isMonsterAlive()) {
                for (final Map.Entry<User, Long> eggReward : fightResult.getEggRewards().entrySet()) {
                    final User user = eggReward.getKey();
                    final long eggRewardAmount = eggReward.getValue();
                    getCastle(attacker).getHatchery().updateEggCount(EggType.BASIC, eggRewardAmount);

                    responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ENCOUNTER_BATTLE_REWARD, user.getName(), eggRewardAmount,
                            currentEncounter.getMonster().getName()));
                }
                currentEncounter = null;
            } else {
                castle.takeDamage(fightResult.getDamageDealt());
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ENCOUNTER_FIGHT_COUNTER_ATTACK, attacker.getName(),
                        currentEncounter.getMonster().getName(), fightResult.getDamageDealt()));
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.CASTLE_HEALTH_INFO, attacker.getName(), castle.getCurrentHealth()));
            }
        }
    }

    public void processFlee(final ResponseObject responseObject, final User attacker) {
        currentEncounter = null;
        responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ENCOUNTER_FLEE_SUCCESS, attacker.getName()));
    }
}
