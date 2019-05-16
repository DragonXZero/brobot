package brobot.eggthemall;

import brobot.BrobotConstants;
import brobot.eggthemall.building.Hatchery;
import brobot.eggthemall.castle.Castle;
import brobot.eggthemall.egg.EggType;
import brobot.eggthemall.kid.KidType;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class EggThemAll {
    private final Map<User, Castle> castles = new HashMap<>();
    private final EggTimer eggTimer;

    public EggThemAll() {
        eggTimer = new EggTimer(EggConstants.EGG_TIMER_UPDATE_FREQUENCY, EggConstants.EGG_TIMER_BLESSING_INCREMENT);
    }

    private void initializeUsersCastle(final User user) {
        final Castle castle = new Castle(user);
        castles.put(user, castle);
    }

    /*
        Gives the mentioned user 100 eggs.
     */
    public void ovulate(final User user, final StringBuilder messageToSend) {
        if (castles.containsKey(user)) {
            messageToSend.append("I already gave you eggs, go away!!!");
        } else {
            initializeUsersCastle(user);
            messageToSend.append(EggUtils.bold(user.getName())).append(", you now have 100 eggs.");
        }
    }

    /*
        Steals (1-20) eggs from the mentioned user.
     */
    public void stealEggs(final User attacker, final User defender, final StringBuilder messageToSend) {
        if (!castles.containsKey(attacker)) {
            initializeUsersCastle(attacker);
        }

        if (!castles.containsKey(defender)) {
            initializeUsersCastle(defender);
        }

        final String attackerFmt = EggUtils.bold(attacker.getName());
        final String defenderFmt = EggUtils.bold(defender.getName());

        if (attacker.equals(defender)) {
            messageToSend.append("You can't steal from yourself dummy. HEY EVERYONE, ")
                .append(attackerFmt)
                .append(" IS A DUM DUM!!");
        } else {
            final Hatchery attackersHatchery = castles.get(attacker).getHatchery();
            final Hatchery defendersHatchery = castles.get(defender).getHatchery();
            long defenderEggCount = defendersHatchery.getEggCount(EggType.BASIC);

            if (defenderEggCount == 0) {
                messageToSend.append("This person has no eggs. :( Nooooo, dōshite?????");
            } else {
                long eggsToSteal = ThreadLocalRandom.current().nextLong(EggConstants.EGG_STEAL_MIN, EggConstants.EGG_STEAL_MAX + 1);

                while (eggsToSteal > defenderEggCount) {
                    eggsToSteal = ThreadLocalRandom.current().nextLong(EggConstants.EGG_STEAL_MIN,
                            defenderEggCount < EggConstants.EGG_STEAL_MAX ? defenderEggCount + 1 : EggConstants.EGG_STEAL_MAX + 1);
                }

                attackersHatchery.updateEggCount(EggType.BASIC, eggsToSteal);
                defendersHatchery.updateEggCount(EggType.BASIC, -eggsToSteal);

                messageToSend.append(attackerFmt)
                    .append(", you stole ")
                    .append(eggsToSteal)
                    .append(" eggs from ")
                    .append(defenderFmt)
                    .append(". You now have ")
                    .append(attackersHatchery.getEggCount(EggType.BASIC))
                    .append(" eggs and they have ")
                    .append(defendersHatchery.getEggCount(EggType.BASIC))
                    .append(" eggs.");
            }
        }
    }


    /*
        Fertilize the specified number of eggs and turn them into kids.
     */
    public void fertilize(final User user, final StringBuilder messageToSend, final String msg) {
        if (!castles.containsKey(user)) {
            initializeUsersCastle(user);
        }

        final Hatchery hatchery = castles.get(user).getHatchery();
        final String userFmt = EggUtils.bold(user.getName());
        final long kidsToMake = Long.parseLong(msg.substring(msg.indexOf("[")+1, msg.indexOf("]")));
        final long numEggs = hatchery.getEggCount(EggType.BASIC);

        if (kidsToMake < 0) {
            messageToSend.append("What are you trying to do here? o_o??");
        } else if (kidsToMake > numEggs) {
            messageToSend.append("You don't have enough eggs :( Go steal some more!");
        } else {
            hatchery.updateKidCount(KidType.NORMAL, kidsToMake);
            hatchery.updateEggCount(EggType.BASIC, -kidsToMake);

            messageToSend.append("Congratulations ")
                .append(userFmt)
                .append(", you made ")
                .append(kidsToMake)
                .append(" kids! You now have ")
                .append(hatchery.getKidCount(KidType.NORMAL))
                .append(" kids and ")
                .append(hatchery.getEggCount(EggType.BASIC))
                .append(" eggs.");
        }
    }

    /*
        Gives an amount of kids to the mentioned user.
     */
    public void giveKids(final User attacker, final User defender, final StringBuilder messageToSend, final String msg) {
        if (!castles.containsKey(attacker)) {
            initializeUsersCastle(attacker);
        }

        if (!castles.containsKey(defender)) {
            initializeUsersCastle(defender);
        }

        final Hatchery attackersHatchery = castles.get(attacker).getHatchery();
        final Hatchery defendersHatchery = castles.get(defender).getHatchery();
        final String attackerFmt = EggUtils.bold(attacker.getName());
        final String defenderFmt = EggUtils.bold(defender.getName());

        if (attacker.equals(defender)) {
            messageToSend.append("They're already your kids!");
        } else {
            final long numKidsToGive = Long.parseLong(msg.substring(msg.indexOf("[")+1, msg.indexOf("]")));
            final long attackerNumKids = attackersHatchery.getKidCount(KidType.NORMAL);

            if (numKidsToGive > attackerNumKids) {
                messageToSend.append("You don't have enough kids to give away. :( Go make more! o:");
            } else {
                attackersHatchery.updateKidCount(KidType.NORMAL, -numKidsToGive);
                defendersHatchery.updateKidCount(KidType.NORMAL, numKidsToGive);

                messageToSend.append("Congratulations ")
                    .append(attackerFmt)
                    .append(", you got rid of ")
                    .append(numKidsToGive)
                    .append(" and gave them to ")
                    .append(defenderFmt)
                    .append(". You now have ")
                    .append(attackersHatchery.getKidCount(KidType.NORMAL))
                    .append(" kids and they have ")
                    .append(defendersHatchery.getKidCount(KidType.NORMAL))
                    .append(" kids.");
            }
        }
    }

    /*
        All the users in the server who have kids gain a number of kids equal to (basic kid count/2).
     */
    public void copulate(final User user, final StringBuilder messageToSend) {
        if (!castles.containsKey(user)) {
            initializeUsersCastle(user);
        }

        for (final Castle castle : castles.values()) {
            final Hatchery hatchery = castle.getHatchery();
            final String ownerFmt = EggUtils.bold(castle.getOwnerName());

            final long numKids = hatchery.getKidCount(KidType.NORMAL);
            if (numKids == 0) {
                messageToSend.append(ownerFmt)
                        .append(", you don't have any kids. o.o So sad, you'll probably die alone too.. lul\n");
            } else {
                final long numEggsCreated = hatchery.getKidCount(KidType.NORMAL) / 2 + 1;
                hatchery.updateEggCount(EggType.BASIC, numEggsCreated);

                messageToSend.append("Congratulations ")
                        .append(ownerFmt)
                        .append("! Some of your kids lay eggs!")
                        .append(" You now have ")
                        .append(hatchery.getEggCount(EggType.BASIC))
                        .append(" eggs!! :D\n");
            }
        }
    }

    /*
        All users kids eat their eggs. ???
     */
    public void eatCake(final StringBuilder messageToSend) {
        for (final Castle castle : castles.values()) {
            final Hatchery hatchery = castle.getHatchery();
            final String ownerFmt = EggUtils.bold(castle.getOwnerName());

            final long numEggs = hatchery.getEggCount(EggType.BASIC);
            final long numKids = hatchery.getKidCount(KidType.NORMAL);

            if (numKids <= numEggs) {
                hatchery.updateEggCount(EggType.BASIC, -numKids);

                messageToSend.append(ownerFmt)
                        .append(", your kids ate ")
                        .append(numKids)
                        .append(" of your eggs!! Those bastards!")
                        .append(" You now have ")
                        .append(hatchery.getEggCount(EggType.BASIC))
                        .append(" eggs.\n");
            } else {
                final long numKidsLost = numKids - numEggs;
                hatchery.updateKidCount(KidType.NORMAL, -numKidsLost);
                hatchery.updateEggCount(EggType.BASIC, -hatchery.getEggCount(EggType.BASIC));

                messageToSend.append(ownerFmt)
                        .append(", you didn't have enough eggs to feed your kids... ")
                        .append(numKidsLost)
                        .append(" of your kids ran away. You now have ")
                        .append(numEggs)
                        .append(" kids and 0 eggs. :(\n");
            }
        }
    }

    /*
        Displays the mentioned users resources.
            * eggs
            * kids
     */
    public void getResourceCount(final User user, final StringBuilder messageToSend) {
        if (!castles.containsKey(user)) {
            initializeUsersCastle(user);
        }

        final Hatchery hatchery = castles.get(user).getHatchery();
        final String userFmt = EggUtils.bold(user.getName());

        messageToSend.append(userFmt)
            .append(", you have ")
            .append(hatchery.getEggCount(EggType.BASIC))
            .append(" eggs and ")
            .append(hatchery.getKidCount(KidType.NORMAL))
            .append(" kids!");
    }

    public void updateResources() {
        eggTimer.updateResoures(castles);
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
        if (!castles.containsKey(user)) {
            initializeUsersCastle(user);
        }

        final Castle castle = castles.get(user);

        messageToSend.append(castle.getCastleName())
                .append("\n")
                .append(BrobotConstants.SEPARATOR);
    }

    /*
        Hatches eggs equal to the amount specified and return the results to the user.
     */
    public void hatchEggs(User user, final StringBuilder messageToSend, int numEggsToHatch) {
        if (!castles.containsKey(user)) {
            initializeUsersCastle(user);
        }

        final Castle castle = castles.get(user);
        final Hatchery hatchery = castle.getHatchery();
    }

}
