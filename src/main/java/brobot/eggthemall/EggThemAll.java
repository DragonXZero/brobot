package brobot.eggthemall;

import brobot.BrobotConstants;
import brobot.eggthemall.building.Hatchery;
import brobot.eggthemall.castle.Castle;
import brobot.eggthemall.egg.EggType;
import brobot.eggthemall.kid.KidType;
import net.dv8tion.jda.core.entities.User;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EggThemAll {
    private final Map<User, Castle> castles = new HashMap<>();
    private final EggTimer eggTimer;

    public EggThemAll() {
        eggTimer = new EggTimer(EggConstants.EGG_TIMER_UPDATE_FREQUENCY, EggConstants.EGG_TIMER_BLESSING_INCREMENT);
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
        messageToSend.append(constructFormattedString(EggMessages.OVULATE_SUCCESS, user.getName()));
    }

    /*
        Steals (1-20) eggs from the mentioned user.
     */
    public void stealEggs(final User attacker, final User defender, final StringBuilder messageToSend) {
        if (attacker.equals(defender)) {
            messageToSend.append(constructFormattedString(EggMessages.STEAL_EGGS_FAIL_SELF, attacker.getName()));
        } else {
            final Hatchery attackersHatchery = castles.get(attacker).getHatchery();
            final Hatchery defendersHatchery = castles.get(defender).getHatchery();
            long defenderEggCount = defendersHatchery.getEggCount(EggType.BASIC);

            if (defenderEggCount == 0) {
                messageToSend.append(constructFormattedString(EggMessages.STEAL_EGGS_FAIL_NO_EGGS_TO_STEAL, defender.getName()));
            } else {
                // Keep generating a number until it is less than or equal to the defender's egg count
                long eggsToSteal = ThreadLocalRandom.current().nextLong(EggConstants.EGG_STEAL_MIN, EggConstants.EGG_STEAL_MAX + 1);
                while (eggsToSteal > defenderEggCount) {
                    eggsToSteal = ThreadLocalRandom.current().nextLong(EggConstants.EGG_STEAL_MIN,
                            defenderEggCount < EggConstants.EGG_STEAL_MAX ? defenderEggCount + 1 : EggConstants.EGG_STEAL_MAX + 1);
                }

                attackersHatchery.updateEggCount(EggType.BASIC, eggsToSteal);
                defendersHatchery.updateEggCount(EggType.BASIC, -eggsToSteal);

                messageToSend.append(constructFormattedString(EggMessages.STEAL_EGGS_SUCCESS, attacker.getName(), eggsToSteal, defender.getName(),
                        attackersHatchery.getEggCount(EggType.BASIC), defendersHatchery.getEggCount(EggType.BASIC)));
            }
        }
    }

    /*
        Fertilize the specified number of eggs and turn them into kids.
     */
    public void fertilize(final User user, final StringBuilder messageToSend, final String msg) {
        final Hatchery hatchery = castles.get(user).getHatchery();
        final long kidsToMake = Long.parseLong(msg.substring(msg.indexOf("[")+1, msg.indexOf("]")));
        final long currentEggCount = hatchery.getEggCount(EggType.BASIC);

        if (kidsToMake < 0) {
            messageToSend.append(EggMessages.INVALID_COMMAND);
        } else if (kidsToMake > currentEggCount) {
            messageToSend.append(EggMessages.FERTILIZE_EGGS_FAIL_NOT_ENOUGH_EGGS);
        } else {
            hatchery.updateKidCount(KidType.NORMAL, kidsToMake);
            hatchery.updateEggCount(EggType.BASIC, -kidsToMake);

            messageToSend.append(constructFormattedString(EggMessages.FERTILIZE_EGGS_SUCCESS, user.getName(), kidsToMake,
                    hatchery.getKidCount(KidType.NORMAL), hatchery.getEggCount(EggType.BASIC)));
        }
    }

    /*
        Gives an amount of kids to the mentioned user.
     */
    public void giveKids(final User attacker, final User defender, final StringBuilder messageToSend, final String msg) {
        final Hatchery attackersHatchery = castles.get(attacker).getHatchery();
        final Hatchery defendersHatchery = castles.get(defender).getHatchery();

        if (attacker.equals(defender)) {
            messageToSend.append(EggMessages.ABANDON_KIDS_FAIL_SELF);
        } else {
            final long numKidsToGive = Long.parseLong(msg.substring(msg.indexOf("[")+1, msg.indexOf("]")));
            final long attackerNumKids = attackersHatchery.getKidCount(KidType.NORMAL);

            if (numKidsToGive > attackerNumKids) {
                messageToSend.append(EggMessages.ABANDON_KIDS_FAIL_NOT_ENOUGH_KIDS_TO_ABANDON);
            } else {
                attackersHatchery.updateKidCount(KidType.NORMAL, -numKidsToGive);
                defendersHatchery.updateKidCount(KidType.NORMAL, numKidsToGive);

                messageToSend.append(constructFormattedString(EggMessages.ABANDON_KIDS_SUCCESS, attacker.getName(), numKidsToGive, defender.getName(),
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
                messageToSend.append(constructFormattedString(EggMessages.COPULATE_FAIL_NO_KIDS, castle.getNameOfOwner()));
            } else {
                final long numEggsCreated = hatchery.getKidCount(KidType.NORMAL) / 2 + 1;
                hatchery.updateEggCount(EggType.BASIC, numEggsCreated);

                messageToSend.append(constructFormattedString(EggMessages.COPULATE_SUCCESS, castle.getNameOfOwner(), hatchery.getEggCount(EggType.BASIC)));
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

                messageToSend.append(constructFormattedString(EggMessages.THANOS_ENOUGH_EGGS, castle.getNameOfOwner(), numKids,
                        hatchery.getEggCount(EggType.BASIC)));
            } else {
                final long numKidsLost = numKids - numEggs;
                hatchery.updateKidCount(KidType.NORMAL, -numKidsLost);
                hatchery.updateEggCount(EggType.BASIC, -hatchery.getEggCount(EggType.BASIC));

                messageToSend.append(constructFormattedString(EggMessages.THANOS_NOT_ENOUGH_EGGS, castle.getNameOfOwner(), numKidsLost, numEggs));
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
        messageToSend.append(constructFormattedString(EggMessages.RESOURCES_GET_SELF, user.getName(), hatchery.getKidCount(KidType.NORMAL),
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

        final String attackerFmt = EggUtils.bold(attackersCastle.getNameOfOwner());
        final String defenderFmt = EggUtils.bold(defendersCastle.getNameOfOwner());

        final long attackersAttackPower = attackersCastle.getAttackValue();
        final long defendersDefensePower = defendersCastle.getDefenseValue();

        messageToSend.append("Your army clashes with ")
                .append(defenderFmt)
                .append("'s army in an epic battle!\n");

        Random rand = new Random();
        final int kidsLost;
        final int kidsDefeated;
        final double powerGapRating = (double) (attackersAttackPower - defendersDefensePower) / (double) defendersDefensePower;

        if (Math.abs(powerGapRating) <= .1) {
            kidsLost = rand.nextInt((int) (attackersHatchery.getKidCount(KidType.NORMAL) * .1));
            kidsDefeated = rand.nextInt((int) (defendersHatchery.getKidCount(KidType.NORMAL) * .1));

            messageToSend.append("Both sides are too evenly matched! You retreat before your army suffers unnecessary casualties!\n")
                    .append("\tBattle Summary")
                    .append("\n\t\t")
                    .append(attackerFmt)
                    .append("'s :baby:'s defeated in battle: ")
                    .append(kidsLost)
                    .append("\n\t\t")
                    .append(defenderFmt)
                    .append("'s :baby:'s defeated in battle: ")
                    .append(kidsDefeated);
        } else if (powerGapRating > 0) {
            if (powerGapRating < .9) {
                kidsLost = rand.nextInt((int) (attackersCastle.getHatchery().getKidCount(KidType.NORMAL) * .05));
                kidsDefeated = rand.nextInt((int) (defendersCastle.getHatchery().getKidCount(KidType.NORMAL) * .2));

                messageToSend.append("Your army easily overpowers the opposing force!\n")
                        .append("\tBattle Summary")
                        .append("\n\t\t")
                        .append(attackerFmt)
                        .append("'s :baby:'s defeated in battle: ")
                        .append(kidsLost)
                        .append("\n\t\t")
                        .append(defenderFmt)
                        .append("'s :baby:'s defeated in battle: ")
                        .append(kidsDefeated);
            } else {
                kidsLost = rand.nextInt((int) (attackersCastle.getHatchery().getKidCount(KidType.NORMAL) * .01));
                kidsDefeated = rand.nextInt((int) (defendersCastle.getHatchery().getKidCount(KidType.NORMAL) * .5));

                messageToSend.append("Your army absolutely obliterates the enemy. You proceed to burn down their crops and obliterate their people from existence!\n")
                        .append("\tBattle Summary")
                        .append("\n\t\t")
                        .append(attackerFmt)
                        .append("'s :baby:'s defeated in battle: ")
                        .append(kidsLost)
                        .append("\n\t\t")
                        .append(defenderFmt)
                        .append("'s :baby:'s defeated in battle: ")
                        .append(kidsDefeated);
            }
        } else {
            if (powerGapRating > -.9) {
                kidsLost = rand.nextInt((int) (attackersCastle.getHatchery().getKidCount(KidType.NORMAL) * .2));
                kidsDefeated = rand.nextInt((int) (defendersCastle.getHatchery().getKidCount(KidType.NORMAL) * .05));

                messageToSend.append("Your army had no chance against the opposing force! Why did you even bother attacking?\n")
                        .append("\tBattle Summary")
                        .append("\n\t\t")
                        .append(attackerFmt)
                        .append("'s :baby:'s defeated in battle: ")
                        .append(kidsLost)
                        .append("\n\t\t")
                        .append(defenderFmt)
                        .append("'s :baby:'s defeated in battle: ")
                        .append(kidsDefeated);
            } else {
                kidsLost = rand.nextInt((int) (attackersCastle.getHatchery().getKidCount(KidType.NORMAL) * .5));
                kidsDefeated = rand.nextInt((int) (defendersCastle.getHatchery().getKidCount(KidType.NORMAL) * .01));

                messageToSend.append("You sent your poor kids into a 300 type scenario. They had no chance... Say hello to your new daddy ")
                        .append(defenderFmt)
                        .append(".\n\tBattle Summary")
                        .append("\n\t\t")
                        .append(attackerFmt)
                        .append("'s :baby:'s defeated in battle: ")
                        .append(kidsLost)
                        .append("\n\t\t")
                        .append(defenderFmt)
                        .append("'s :baby:'s defeated in battle: ")
                        .append(kidsDefeated);
            }
        }
        attackersHatchery.updateKidCount(KidType.NORMAL, -kidsLost);
        defendersHatchery.updateKidCount(KidType.NORMAL, -kidsDefeated);
    }

    public void updateResources() {
        eggTimer.updateResoures(castles);
    }

    private String constructFormattedString(final String message, final Object... args) {
        return String.format(message, args);
    }
}
