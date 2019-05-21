package brobot.eggthemall;

import brobot.BrobotWorld;
import brobot.ResponseObject;
import brobot.eggthemall.castle.building.Hatchery;
import brobot.eggthemall.castle.Castle;
import brobot.eggthemall.egg.EggType;
import brobot.eggthemall.kid.KidType;
import net.dv8tion.jda.core.entities.User;

import java.util.*;

public class EggThemAll {
    // TODO - Change back to private. Changed to public static to test battle system.
    private final BrobotWorld world;
    private final EggTimer eggTimer;

    public EggThemAll() {
        this.world = new BrobotWorld();
        this.eggTimer = new EggTimer(EggConstants.EGG_TIMER_UPDATE_FREQUENCY, EggConstants.EGG_TIMER_BLESSING_INCREMENT);
    }

    public BrobotWorld getWorld() {
        return world;
    }

    /*
     */
    public void ovulate(final ResponseObject responseObject, final User user) {
        world.createCastleIfNotCreated(user);
    }

    /*
        Steals (1-20) eggs from the mentioned user.
     */
    public void stealEggs(final ResponseObject responseObject, final User attacker, final User defender) {
        final Castle attackingCastle = world.getCastle(attacker);
        final Castle defendingCastle = world.getCastle(defender);
        attackingCastle.stealFrom(responseObject, defendingCastle);
    }

    /*
        Fertilize the specified number of eggs and turn them into kids.
     */
    public void fertilize(final ResponseObject responseObject, final User user, final long quantity) {
        final Castle castle = world.getCastle(user);
        castle.fertilize(responseObject, quantity);
    }

    /*
        Gives an amount of kids to the mentioned user.
     */
    public void giveKids(final ResponseObject responseObject, final User attacker, final User defender, final long quantity) {
        final Castle attackingCastle = world.getCastle(attacker);
        final Castle defendingCastle = world.getCastle(defender);
        attackingCastle.giveKidsTo(responseObject, defendingCastle, quantity);
    }

    /*
        All the users in the server who have kids gain a number of kids equal to (basic kid count/2).
     */
    public void copulate(final ResponseObject responseObject) {
        for (final Castle castle : world.getCastles().values()) {
            final Hatchery hatchery = castle.getHatchery();
            final long numKids = hatchery.getKidCount(KidType.NORMAL);

            if (numKids == 0) {
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.COPULATE_FAIL_NO_KIDS, castle.getNameOfOwner()));
            } else {
                final long numEggsCreated = hatchery.getKidCount(KidType.NORMAL) / 2 + 1;
                hatchery.updateEggCount(EggType.BASIC, numEggsCreated);

                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.COPULATE_SUCCESS, castle.getNameOfOwner(),
                        hatchery.getEggCount(EggType.BASIC)));
            }
        }
    }

    /*
        All users kids eat eggs. Literally Thanos.
     */
    public void eatCake(final ResponseObject responseObject) {
        for (final Castle castle : world.getCastles().values()) {
            final Hatchery hatchery = castle.getHatchery();
            final long numEggs = hatchery.getEggCount(EggType.BASIC);
            final long numKids = hatchery.getKidCount(KidType.NORMAL);

            if (numKids <= numEggs) {
                hatchery.updateEggCount(EggType.BASIC, -numKids);

                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.THANOS_ENOUGH_EGGS, castle.getNameOfOwner(), numKids,
                        hatchery.getEggCount(EggType.BASIC)));
            } else {
                final long numKidsLost = numKids - numEggs;
                hatchery.updateKidCount(KidType.NORMAL, -numKidsLost);
                hatchery.updateEggCount(EggType.BASIC, -hatchery.getEggCount(EggType.BASIC));

                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.THANOS_NOT_ENOUGH_EGGS, castle.getNameOfOwner(), numKidsLost,
                        hatchery.getKidCount(KidType.NORMAL), hatchery.getEggCount(EggType.BASIC)));
            }
        }
    }

    /*
        Returns information about a users own castle.
     */
    public void displayCastleInfo(final ResponseObject responseObject, final User user) {
        responseObject.addMessage(world.getCastle(user).toString());
    }

    /*
        TODO - This method implementation is not complete.
        Hatches eggs equal to the amount specified and return the results to the user.
     */
    public void hatchEggs(final ResponseObject responseObject, final User user, int numEggsToHatch) {
        final Castle castle = world.getCastles().get(user);
        castle.getHatchery().hatchEggs(EggType.BASIC, numEggsToHatch);
    }

    /*
        TODO - This method implementation is just for the poc. Most of this code will be removed.
     */
    public void attack(final ResponseObject responseObject, final User attacker, final User defender) {
        final Castle attackersCastle = world.getCastle(attacker);
        final Castle defendersCastle = world.getCastle(defender);
        attackersCastle.attackCastle(responseObject, defendersCastle);
    }

    public void updateResources() {
        eggTimer.updateResoures(world.getCastles());
    }

    public void generateRandomEncounter(final ResponseObject responseObject) {
        world.startEncounter(responseObject);
    }

    public void fightEncounter(final ResponseObject responseObject, final User attacker) {
        world.processFight(responseObject, attacker);
    }

    public void fleeEncounter(final ResponseObject responseObject, final User attacker) {
        world.processFlee(responseObject, attacker);
    }

    public void displayHelpInformation(final ResponseObject responseObject) {
        responseObject.addMessage(EggMessages.HELP_MESSAGE);
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

}
