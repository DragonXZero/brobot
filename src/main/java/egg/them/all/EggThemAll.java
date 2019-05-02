package egg.them.all;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class EggThemAll {
    private static final Map<String, Integer> eggCount = new HashMap<>();
    private static final Map<String, Integer> kidCount = new HashMap<>();
    private final EggTimer eggTimer;

    public EggThemAll() {
        eggTimer = new EggTimer(EggConstants.EGG_TIMER_UPDATE_FREQUENCY, EggConstants.EGG_TIMER_BLESSING_INCREMENT);
    }

    /*
        Gives the requesting user 100 eggs.
     */
    public void ovulate(final String user, final StringBuilder messageToSend) {
        if(eggCount.containsKey(user)) {
            messageToSend.append("I already gave you eggs, go away!!!");
        } else {
            eggCount.put(user, EggConstants.EGG_OVULATION_COUNT);
            messageToSend.append(EggUtils.bold(user)).append(", you now have 100 eggs.");
        }
    }

    /*
        Steals (1-20) eggs from the specified user.
     */
    public void stealEggs(final String attacker, final String defender, StringBuilder messageToSend) {
        final String attackerFmt = EggUtils.bold(attacker);
        final String defenderFmt = EggUtils.bold(defender);

        if (attacker.equals(defender)) {
            messageToSend.append("You can't steal from yourself dummy. HEY EVERYONE, ")
                .append(attackerFmt)
                .append(" IS A DUM DUM!!");
        }
        else if (!eggCount.containsKey(attacker)) {
            messageToSend.append("You aren't part of the eggame. Ask me for some eggs.");
        } else {
            if (!eggCount.containsKey(defender)) {
                messageToSend.append("This person isn't part of the eggame.");
            } else {
                int attackerEggCount = eggCount.get(attacker);
                int defenderEggCount = eggCount.get(defender);

                if (defenderEggCount == 0) {
                    messageToSend.append("This person has no eggs. :( Nooooo, dōshite?????");
                } else {
                    int min = 1;
                    int max = 20;
                    int eggsToSteal = ThreadLocalRandom.current().nextInt(min, max + 1);

                    while (eggsToSteal > defenderEggCount) {
                        eggsToSteal = ThreadLocalRandom.current().nextInt(min, defenderEggCount <= 20 ? defenderEggCount + 1 : max + 1);
                    }

                    attackerEggCount += eggsToSteal;
                    defenderEggCount -= eggsToSteal;

                    messageToSend.append(attackerFmt)
                        .append(", you stole ")
                        .append(eggsToSteal)
                        .append(" eggs from ")
                        .append(defenderFmt)
                        .append(". You now have ")
                        .append(attackerEggCount)
                        .append(" eggs and they have ")
                        .append(defenderEggCount)
                        .append(" eggs.");
                    eggCount.put(attacker, attackerEggCount);
                    eggCount.put(defender, defenderEggCount);
                }
            }
        }
    }


    /*
        Fertilize the specified number of eggs and turn them into kids.
     */
    public void fertilize(final String user, final StringBuilder messageToSend, final String msg) {
        final String userFmt= EggUtils.bold(user);

        final int kidsToMake = Integer.parseInt(msg.substring(msg.indexOf("[")+1, msg.indexOf("]")));
        int numEggs = eggCount.getOrDefault(user, 0);

        if (kidsToMake > numEggs) {
            messageToSend.append("You don't have enough eggs :( Go steal some more!");
        } else {
            numEggs -= kidsToMake;
            int numKids = kidCount.getOrDefault(user, 0) + kidsToMake;
            messageToSend.append("Congratulations ")
                .append(userFmt)
                .append(", you made ")
                .append(kidsToMake)
                .append(" kids! You now have ")
                .append(numKids)
                .append(" kids and ")
                .append(numEggs)
                .append(" eggs.");
            kidCount.put(user, numKids);
            eggCount.put(user, numEggs);
        }
    }

    /*
        Gives kids to the specified user.
     */
    public void giveKids(final String attacker, final String defender, final StringBuilder messageToSend, final String msg) {
        final String attackerFmt = EggUtils.bold(attacker);
        final String defenderFmt = EggUtils.bold(defender);

        if (attacker.equals(defender)) {
            messageToSend.append("They're already your kids!");
        } else {
            int numKidsToGive = Integer.parseInt(msg.substring(msg.indexOf("[")+1, msg.indexOf("]")));
            int attackerNumKids = kidCount.getOrDefault(attacker, 0);
            int defenderNumKids = kidCount.getOrDefault(defender, 0);

            if (numKidsToGive > attackerNumKids) {
                messageToSend.append("You don't have enough kids to give away. :( Go make more! o:");
            } else {
                attackerNumKids -= numKidsToGive;
                defenderNumKids += numKidsToGive;

                messageToSend.append("Congratulations ")
                    .append(attackerFmt)
                    .append(", you got rid of ")
                    .append(numKidsToGive)
                    .append(" and gave them to ")
                    .append(defenderFmt)
                    .append(". You now have ")
                    .append(attackerNumKids)
                    .append(" kids and they have ")
                    .append(defenderNumKids)
                    .append(" kids.");
            }
            kidCount.put(attacker, attackerNumKids);
            kidCount.put(defender, defenderNumKids);
        }
    }

    /*
        All the users in the server who have kids gain kids/2 eggs.
     */
    public void copulate(final String user, final StringBuilder messageToSend) {
        if (!kidCount.containsKey(user)) {
            kidCount.put(user, 0);
        }
        for (Map.Entry<String, Integer> entry : kidCount.entrySet()) {
            final String parent = entry.getKey();
            final String parentFmt = EggUtils.bold(parent);

            final int numKids = entry.getValue();
            int numEggs = eggCount.getOrDefault(parent, 0);
            if (numKids > 0) {
                int newEggCount = numEggs + numKids / 2;
                messageToSend.append("Congratulations ")
                    .append(parentFmt)
                    .append("! Some of your kids lay eggs!")
                    .append(" You now have ")
                    .append(newEggCount)
                    .append(" eggs!! :D\n");
                eggCount.put(parent, newEggCount);
            } else {
                messageToSend.append(parentFmt)
                    .append(", you don't have any kids. o.o So sad, you'll probably die alone too.. lul\n");
            }
        }
    }

    /*
        All users kids eat their eggs. ???
     */
    public void eatCake(final StringBuilder messageToSend) {
        for (Map.Entry<String, Integer> entry : kidCount.entrySet()) {
            String parent = entry.getKey();
            String parentFmt = EggUtils.bold(parent);

            int numKids = entry.getValue();
            int numEggs = eggCount.getOrDefault(parent, 0);

            if (numKids > 0 && numKids <= numEggs) {
                numEggs -= numKids;
                messageToSend.append(parentFmt)
                    .append(", your kids ate ")
                    .append(numKids)
                    .append(" of your eggs!! Those bastards!")
                    .append(" You now have ")
                    .append(numEggs)
                    .append(" eggs.\n");
                eggCount.put(parent, numEggs);
            } else if (numKids > numEggs) {
                int kidsGone = numKids - numEggs;
                messageToSend.append(parentFmt)
                    .append(", you didn't have enough eggs to feed your kids... ")
                    .append(kidsGone)
                    .append(" of your kids ran away. You now have ")
                    .append(numEggs)
                    .append(" kids and 0 eggs. :(\n");
                kidCount.put(parent, numEggs);
                eggCount.put(parent, 0);
            }
        }
    }

    /*
        Displays the requesting users resources.
            * eggs
            * kids
     */
    public void getResourceCount(final String user, final StringBuilder messageToSend) {
        final String userFmt = EggUtils.bold(user);
        final int numEggs = eggCount.getOrDefault(user, 0);
        final int numKids = kidCount.getOrDefault(user, 0);
        messageToSend.append(userFmt)
            .append(", you have ")
            .append(numEggs)
            .append(" eggs and ")
            .append(numKids)
            .append(" kids!");
    }

    public void updateResources() {
        eggTimer.updateResoures(eggCount);
    }
    /*
        For every resource, list the top 5 users with the highest count.
     */
    public void displayLeaderBoard() {
//    private void displayLeaderBoard() {
//        StringBuilder bldr = new StringBuilder();
//
//
//        Map<String, Integer> leaders = new LinkedHashMap<>();
//        eggCount.entrySet().stream()
//            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//            .forEachOrdered(x -> leaders.put(x.getKey(), x.getValue()));
//    }
    }

}
