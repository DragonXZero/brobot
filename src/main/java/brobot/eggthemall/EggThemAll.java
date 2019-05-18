package brobot.eggthemall;

import brobot.BrobotUtils;
import brobot.ResponseObject;
import brobot.eggthemall.building.Hatchery;
import brobot.eggthemall.castle.Castle;
import brobot.eggthemall.egg.EggType;
import brobot.eggthemall.encounter.BattleResult;
import brobot.eggthemall.encounter.Encounter;
import brobot.eggthemall.encounter.EncounterResolver;
import brobot.eggthemall.encounter.RandomEncounterGenerator;
import brobot.eggthemall.encounter.monster.Monster;
import brobot.eggthemall.kid.KidType;
import net.dv8tion.jda.core.entities.User;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EggThemAll {
    // TODO - Change back to private. Changed to public static to test battle system.
    public static final Map<User, Castle> castles = new HashMap<>();
    private final EggTimer eggTimer;
    private final RandomEncounterGenerator randomEncounterGenerator;
    private final EncounterResolver encounterResolver;
    private Encounter currentEncounter;

    public EggThemAll() {
        eggTimer = new EggTimer(EggConstants.EGG_TIMER_UPDATE_FREQUENCY, EggConstants.EGG_TIMER_BLESSING_INCREMENT);
        randomEncounterGenerator = new RandomEncounterGenerator();
        encounterResolver = new EncounterResolver();
    }

    /* TODO - Need a more elegant way of doing this. Maybe we can pull castle initialization outside of EggThemAll. */
    public void initializeCastleIfNotInitialized(final User... users) {
        for (User user : users) {
            if (!castles.containsKey(user)) {
                castles.put(user, new Castle(user));
            }
        }
    }

    /*
        Gives the mentioned user 100 eggs.
     */
    public void ovulate(final User user, final StringBuilder messageToSend) {
        if (castles.containsKey(user)) {
            messageToSend.append(EggUtils.constructFormattedString(EggMessages.OVULATE_FAIL));
        } else {
            messageToSend.append(EggUtils.constructFormattedString(EggMessages.OVULATE_SUCCESS, user.getName()));
        }
    }

    /*
        Steals (1-20) eggs from the mentioned user.
     */
    public void stealEggs(final User attacker, final User defender, final StringBuilder messageToSend) {
        if (attacker.equals(defender)) {
            messageToSend.append(EggUtils.constructFormattedString(EggMessages.STEAL_EGGS_FAIL_SELF, attacker.getName()));
        } else {
            final Hatchery attackersHatchery = castles.get(attacker).getHatchery();
            final Hatchery defendersHatchery = castles.get(defender).getHatchery();
            long defenderEggCount = defendersHatchery.getEggCount(EggType.BASIC);

            if (defenderEggCount == 0) {
                messageToSend.append(EggUtils.constructFormattedString(EggMessages.STEAL_EGGS_FAIL_NO_EGGS_TO_STEAL, defender.getName()));
            } else {
                // Keep generating a number until it is less than or equal to the defender's egg count
                long eggsToSteal = ThreadLocalRandom.current().nextLong(EggConstants.EGG_STEAL_MIN, EggConstants.EGG_STEAL_MAX + 1);
                while (eggsToSteal > defenderEggCount) {
                    eggsToSteal = ThreadLocalRandom.current().nextLong(EggConstants.EGG_STEAL_MIN,
                            defenderEggCount < EggConstants.EGG_STEAL_MAX ? defenderEggCount + 1 : EggConstants.EGG_STEAL_MAX + 1);
                }

                attackersHatchery.updateEggCount(EggType.BASIC, eggsToSteal);
                defendersHatchery.updateEggCount(EggType.BASIC, -eggsToSteal);

                messageToSend.append(EggUtils.constructFormattedString(EggMessages.STEAL_EGGS_SUCCESS, attacker.getName(), eggsToSteal, defender.getName(),
                        attackersHatchery.getEggCount(EggType.BASIC), defendersHatchery.getEggCount(EggType.BASIC)));
            }
        }
    }

    /*
        Fertilize the specified number of eggs and turn them into kids.
     */
    public void fertilize(final User user, final StringBuilder messageToSend, final long quantity) {
        final Hatchery hatchery = castles.get(user).getHatchery();
        final long kidsToMake = quantity;
        final long currentEggCount = hatchery.getEggCount(EggType.BASIC);

        if (kidsToMake < 0) {
            messageToSend.append(EggMessages.INVALID_COMMAND);
        } else if (kidsToMake > currentEggCount) {
            messageToSend.append(EggMessages.FERTILIZE_EGGS_FAIL_NOT_ENOUGH_EGGS);
        } else {
            hatchery.updateKidCount(KidType.NORMAL, kidsToMake);
            hatchery.updateEggCount(EggType.BASIC, -kidsToMake);

            messageToSend.append(EggUtils.constructFormattedString(EggMessages.FERTILIZE_EGGS_SUCCESS, user.getName(), kidsToMake,
                    hatchery.getKidCount(KidType.NORMAL), hatchery.getEggCount(EggType.BASIC)));
        }
    }

    /*
        Gives an amount of kids to the mentioned user.
     */
    public void giveKids(final User attacker, final User defender, final StringBuilder messageToSend, final long quantity) {
        final Hatchery attackersHatchery = castles.get(attacker).getHatchery();
        final Hatchery defendersHatchery = castles.get(defender).getHatchery();

        if (attacker.equals(defender)) {
            messageToSend.append(EggMessages.ABANDON_KIDS_FAIL_SELF);
        } else {
            final long numKidsToGive = quantity;
            final long attackerNumKids = attackersHatchery.getKidCount(KidType.NORMAL);

            if (numKidsToGive > attackerNumKids) {
                messageToSend.append(EggMessages.ABANDON_KIDS_FAIL_NOT_ENOUGH_KIDS_TO_ABANDON);
            } else {
                attackersHatchery.updateKidCount(KidType.NORMAL, -numKidsToGive);
                defendersHatchery.updateKidCount(KidType.NORMAL, numKidsToGive);

                messageToSend.append(EggUtils.constructFormattedString(EggMessages.ABANDON_KIDS_SUCCESS, attacker.getName(), numKidsToGive, defender.getName(),
                        attackersHatchery.getKidCount(KidType.NORMAL), defendersHatchery.getKidCount(KidType.NORMAL)));
            }
        }
    }

    /*
        All the users in the server who have kids gain a number of kids equal to (basic kid count/2).
     */
    public void copulate(final User user, final StringBuilder messageToSend) {
        for (final Castle castle : castles.values()) {
            final Hatchery hatchery = castle.getHatchery();
            final long numKids = hatchery.getKidCount(KidType.NORMAL);

            if (numKids == 0) {
                messageToSend.append(EggUtils.constructFormattedString(EggMessages.COPULATE_FAIL_NO_KIDS, castle.getNameOfOwner()));
            } else {
                final long numEggsCreated = hatchery.getKidCount(KidType.NORMAL) / 2 + 1;
                hatchery.updateEggCount(EggType.BASIC, numEggsCreated);

                messageToSend.append(EggUtils.constructFormattedString(EggMessages.COPULATE_SUCCESS, castle.getNameOfOwner(), hatchery.getEggCount(EggType.BASIC)));
            }
        }
    }

    /*
        All users kids eat eggs. Literally Thanos.
     */
    public void eatCake(final StringBuilder messageToSend) {
        for (final Castle castle : castles.values()) {
            final Hatchery hatchery = castle.getHatchery();
            final long numEggs = hatchery.getEggCount(EggType.BASIC);
            final long numKids = hatchery.getKidCount(KidType.NORMAL);

            if (numKids <= numEggs) {
                hatchery.updateEggCount(EggType.BASIC, -numKids);

                messageToSend.append(EggUtils.constructFormattedString(EggMessages.THANOS_ENOUGH_EGGS, castle.getNameOfOwner(), numKids,
                        hatchery.getEggCount(EggType.BASIC)));
            } else {
                final long numKidsLost = numKids - numEggs;
                hatchery.updateKidCount(KidType.NORMAL, -numKidsLost);
                hatchery.updateEggCount(EggType.BASIC, -hatchery.getEggCount(EggType.BASIC));

                messageToSend.append(EggUtils.constructFormattedString(EggMessages.THANOS_NOT_ENOUGH_EGGS, castle.getNameOfOwner(), numKidsLost,
                        hatchery.getKidCount(KidType.NORMAL), hatchery.getEggCount(EggType.BASIC)));
            }
        }
    }

    /*
        Displays the mentioned users resources.
            * eggs
            * kids
     */
    public void getResourceCounts(final User user, final StringBuilder messageToSend) {
        final Hatchery hatchery = castles.get(user).getHatchery();
        messageToSend.append(EggUtils.constructFormattedString(EggMessages.RESOURCES_GET_SELF, user.getName(), hatchery.getKidCount(KidType.NORMAL),
                hatchery.getEggCount(EggType.BASIC)));
    }

    /*
        For every resource, list the top 5 users with the highest count.
     */
    public List<StringBuilder> displayLeaderBoard() {
//        List<StringBuilder> bldrs = new ArrayList<>();
//
//        for (Map.Entry<String, Map<String, Long>> resourceCount : resourceCounts.entrySet()) {
//            final String resourceName = resourceCount.getKey();
//            StringBuilder bldr = new StringBuilder(resourceName).append("\n");
//
//            int placement = 1;
//            Map<String, Long> sortedCounts = EggUtils.sortValuesDesc(resourceCount.getValue());
//            for (Map.Entry<String, Long> count : sortedCounts.entrySet()) {
//                bldr.append(placement++)
//                    .append(". ")
//                    .append(count.getKey())
//                    .append(" : " )
//                    .append(count.getValue())
//                    .append("\n");
//                if (placement > 5) {
//                    break;
//                }
//            }
//            bldrs.add(bldr);
//        }
//
//        return bldrs;
        return null;
    }

    /*
        Returns information about a users own castle.
     */
    public void displayCastleInfo(final User user, final StringBuilder messageToSend) {
        messageToSend.append(castles.get(user).toString());
    }

    /*
        TODO - This method implementation is not complete.
        Hatches eggs equal to the amount specified and return the results to the user.
     */
    public void hatchEggs(User user, final StringBuilder messageToSend, int numEggsToHatch) {
        final Castle castle = castles.get(user);
        final Hatchery hatchery = castle.getHatchery();
    }

    /*
        TODO - This method implementation is just for the poc. Most of this code will be removed.
     */
    public void attack(final User attacker, final User defender, final StringBuilder messageToSend) {
        final Castle attackersCastle = castles.get(attacker);
        final Castle defendersCastle = castles.get(defender);

        final Hatchery attackersHatchery = attackersCastle.getHatchery();
        final Hatchery defendersHatchery = defendersCastle.getHatchery();

        final long attackersAttackPower = attackersCastle.getAttackValue();
        final long defendersDefensePower = defendersCastle.getDefenseValue();

        messageToSend.append(EggUtils.constructFormattedString(EggMessages.ATTACK_INTRO, attacker.getName(), defender.getName()));

        final Random rand = new Random();
        final int kidsLost;
        final int kidsDefeated;
        final double powerGapRating = (double) (attackersAttackPower - defendersDefensePower) / (double) defendersDefensePower;
        final double attackerUpperBoundLoss = attackersHatchery.getKidCount(KidType.NORMAL);
        final double defenderUpperBoundLoss = defendersHatchery.getKidCount(KidType.NORMAL);

        if (Math.abs(powerGapRating) <= .1) {
            kidsLost = rand.nextInt((int) (attackerUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_DRAW) + 1);
            kidsDefeated = rand.nextInt((int) (defenderUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_DRAW) + 1);
            messageToSend.append(EggMessages.ATTACK_DRAW);
        } else if (powerGapRating > 0) {
            if (powerGapRating < .9) {
                kidsLost = rand.nextInt((int) (attackerUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_WINNER) + 1);
                kidsDefeated = rand.nextInt((int) (defenderUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_LOSER) + 1);
                messageToSend.append(EggUtils.constructFormattedString(EggMessages.ATTACK_VICTORY));
            } else {
                kidsLost = rand.nextInt((int) (attackerUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_WINNER_OVERWHELMING) + 1);
                kidsDefeated = rand.nextInt((int) (defenderUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_LOSER_OVERWHELMING) + 1);
                messageToSend.append(EggUtils.constructFormattedString(EggMessages.ATTACK_VICTORY_OVERWHELMING));
            }
        } else {
            if (powerGapRating > -.9) {
                kidsLost = rand.nextInt((int) (attackerUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_LOSER) + 1);
                kidsDefeated = rand.nextInt((int) (defenderUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_WINNER) + 1);
                messageToSend.append(EggUtils.constructFormattedString(EggMessages.ATTACK_DEFEAT));
            } else {
                kidsLost = rand.nextInt((int) (attackerUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_LOSER_OVERWHELMING) + 1);
                kidsDefeated = rand.nextInt((int) (defenderUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_WINNER_OVERWHELMING) + 1);
                messageToSend.append(EggUtils.constructFormattedString(EggMessages.ATTACK_DEFEAT_OVERWHELMING, defender.getName()));
            }
        }
        messageToSend.append(EggUtils.constructFormattedString(EggMessages.ATTACK_BATTLE_SUMMARY, attacker.getName(), kidsLost, defender.getName(), kidsDefeated));
        attackersHatchery.updateKidCount(KidType.NORMAL, -kidsLost);
        defendersHatchery.updateKidCount(KidType.NORMAL, -kidsDefeated);
    }

    public void updateResources() {
        eggTimer.updateResoures(castles);
    }

    public void generateRandomEncounter(final ResponseObject responseObject) {
        if (currentEncounter != null) {
            responseObject.addMessage(EggMessages.ENCOUNTER_ALREADY_IN_AN_ENCOUTER);
        } else {
            currentEncounter = randomEncounterGenerator.generateRandomEncounter();
            final Monster monster = currentEncounter.getMonster();
            final String encounterMessage = "A wild **" + monster.getName() + "** has appeared!\n" + monster.toString();
            responseObject.addMessage(encounterMessage);
            responseObject.addImage(monster.getSpritePath());
//            responseObject.addMessage("What will you do?\n\t**1.** Attempt to seduce. \n\t**2.** Unzip. \n\t**3. **Admire.\n");
        }
    }

    public void processEncounterAttack(final User attacker, final ResponseObject responseObject) {
        if (currentEncounter == null) {
            responseObject.addMessage(EggMessages.ENCOUNTER_NO_ENCOUNTER);
        } else {
            final Castle castle = castles.get(attacker);
            BattleResult battleResult = currentEncounter.processAttack(responseObject, attacker, castle.getAttackValue());
            if (!battleResult.isMonsterAlive()) {
                for (final Map.Entry<User, Long> eggReward : battleResult.getEggRewards().entrySet()) {
                    final User user = eggReward.getKey();
                    final long eggRewardAmount = eggReward.getValue();
                    castles.get(user).getHatchery().updateEggCount(EggType.BASIC, eggRewardAmount);

                    responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ENCOUNTER_BATTLE_REWARD, user.getName(), eggRewardAmount,
                            currentEncounter.getMonster().getName()));
                }
                currentEncounter = null;
            } else {
                castle.takeDamage(battleResult.getDamageDealt());
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ENCOUNTER_FIGHT_COUNTER_ATTACK, attacker.getName(),
                        currentEncounter.getMonster().getName(), battleResult.getDamageDealt()));
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.CASTLE_HEALTH_INFO, attacker.getName(), castle.getCurrentHealth()));
            }
        }
    }

    public void processEncounterFlee(final User user, final ResponseObject responseObject) {
        currentEncounter = null;
        responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ENCOUNTER_FLEE_SUCCESS, user.getName()));
    }

    public void displayHelpInformation(final ResponseObject responseObject) {
        responseObject.addMessage(EggMessages.HELP_MESSAGE);
    }

}
