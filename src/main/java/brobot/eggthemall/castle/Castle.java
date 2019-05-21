package brobot.eggthemall.castle;

import brobot.BrobotConstants;
import brobot.ResponseObject;
import brobot.eggthemall.EggConstants;
import brobot.eggthemall.EggMessages;
import brobot.eggthemall.EggUtils;
import brobot.eggthemall.castle.building.Hatchery;
import brobot.eggthemall.egg.EggType;
import brobot.eggthemall.kid.KidType;
import net.dv8tion.jda.core.entities.User;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Castle {
    private final User owner;
    private final String nameOfOwner;
    private final String castleName;
    private final Hatchery hatchery;

    private long level;
    private long maximumHealth;
    private long currentHealth;

    public Castle (final User owner) {
        this.owner = owner;
        this.nameOfOwner = owner.getName();
        this.castleName = nameOfOwner + "'s Castle";
        this.hatchery = new Hatchery();
        this.level = 1;
        this.maximumHealth = this.currentHealth = 100;
    }

    public User getOwner() {
        return owner;
    }

    public String getNameOfOwner() {
        return nameOfOwner;
    }

    public String getCastleName() {
        return this.castleName;
    }

    public Hatchery getHatchery() {
        return this.hatchery;
    }

    public long getAttackValue() {
        return hatchery.getKidCount(KidType.NORMAL);
    }

    public long getDefenseValue() {
        return hatchery.getKidCount(KidType.NORMAL);
    }

    public long getCurrentHealth() {
        return currentHealth;
    }

    public void fertilize(final ResponseObject responseObject, final long numKidsToMake) {
        final long currentEggCount = hatchery.getEggCount(EggType.BASIC);

        if (numKidsToMake < 0) {
            responseObject.addMessage(EggMessages.INVALID_COMMAND);
        } else if (numKidsToMake > currentEggCount) {
            responseObject.addMessage(EggMessages.FERTILIZE_EGGS_FAIL_NOT_ENOUGH_EGGS);
        } else {
            hatchery.updateKidCount(KidType.NORMAL, numKidsToMake);
            hatchery.updateEggCount(EggType.BASIC, -numKidsToMake);

            responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.FERTILIZE_EGGS_SUCCESS, nameOfOwner, numKidsToMake,
                    hatchery.getKidCount(KidType.NORMAL), hatchery.getEggCount(EggType.BASIC)));
        }
    }

    public void stealFrom(final ResponseObject responseObject, final Castle defendingCastle) {
        if (this.equals(defendingCastle)) {
            responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.STEAL_EGGS_FAIL_SELF, nameOfOwner));
        } else {
            final Hatchery attackersHatchery = hatchery;
            final Hatchery defendersHatchery = defendingCastle.getHatchery();
            long defenderEggCount = defendersHatchery.getEggCount(EggType.BASIC);

            if (defenderEggCount == 0) {
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.STEAL_EGGS_FAIL_NO_EGGS_TO_STEAL, defendingCastle.getNameOfOwner()));
            } else {
                // Keep generating a number until it is less than or equal to the defender's egg count
                long eggsToSteal = ThreadLocalRandom.current().nextLong(EggConstants.EGG_STEAL_MIN, EggConstants.EGG_STEAL_MAX + 1);
                while (eggsToSteal > defenderEggCount) {
                    eggsToSteal = ThreadLocalRandom.current().nextLong(EggConstants.EGG_STEAL_MIN,
                            defenderEggCount < EggConstants.EGG_STEAL_MAX ? defenderEggCount + 1 : EggConstants.EGG_STEAL_MAX + 1);
                }

                attackersHatchery.updateEggCount(EggType.BASIC, eggsToSteal);
                defendersHatchery.updateEggCount(EggType.BASIC, -eggsToSteal);

                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.STEAL_EGGS_SUCCESS, nameOfOwner, eggsToSteal, defendingCastle.getNameOfOwner(),
                        attackersHatchery.getEggCount(EggType.BASIC), defendersHatchery.getEggCount(EggType.BASIC)));
            }
        }

    }

    public void giveKidsTo(final ResponseObject responseObject, final Castle defendingCastle, final long numKidsToGive) {
        if (this.equals(defendingCastle)) {
            responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ABANDON_KIDS_FAIL_SELF, nameOfOwner));
        } else {
            final Hatchery attackersHatchery = hatchery;
            final Hatchery defendersHatchery = defendingCastle.getHatchery();

            final long attackerNumKids = attackersHatchery.getKidCount(KidType.NORMAL);
            if (numKidsToGive > attackerNumKids) {
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ABANDON_KIDS_FAIL_NOT_ENOUGH_KIDS_TO_ABANDON, nameOfOwner));
            } else {
                attackersHatchery.updateKidCount(KidType.NORMAL, -numKidsToGive);
                defendersHatchery.updateKidCount(KidType.NORMAL, numKidsToGive);

                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ABANDON_KIDS_SUCCESS, nameOfOwner, numKidsToGive,
                        defendingCastle.getNameOfOwner(), attackersHatchery.getKidCount(KidType.NORMAL), defendersHatchery.getKidCount(KidType.NORMAL)));
            }
        }
    }

    public void attackCastle(final ResponseObject responseObject, final Castle defendingCastle) {
        final Hatchery attackersHatchery = this.getHatchery();
        final Hatchery defendersHatchery = defendingCastle.getHatchery();

        final long attackersAttackPower = this.getAttackValue();
        final long defendersDefensePower = defendingCastle.getDefenseValue();

        responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ATTACK_INTRO, nameOfOwner, defendingCastle.getNameOfOwner()));

        final Random rand = new Random();
        final int kidsLost;
        final int kidsDefeated;
        final double powerGapRating = (double) (attackersAttackPower - defendersDefensePower) / (double) defendersDefensePower;
        final double attackerUpperBoundLoss = attackersHatchery.getKidCount(KidType.NORMAL);
        final double defenderUpperBoundLoss = defendersHatchery.getKidCount(KidType.NORMAL);

        if (Math.abs(powerGapRating) <= .1) {
            kidsLost = rand.nextInt((int) (attackerUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_DRAW) + 1);
            kidsDefeated = rand.nextInt((int) (defenderUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_DRAW) + 1);
            responseObject.addMessage(EggMessages.ATTACK_DRAW);
        } else if (powerGapRating > 0) {
            if (powerGapRating < .9) {
                kidsLost = rand.nextInt((int) (attackerUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_WINNER) + 1);
                kidsDefeated = rand.nextInt((int) (defenderUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_LOSER) + 1);
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ATTACK_VICTORY));
            } else {
                kidsLost = rand.nextInt((int) (attackerUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_WINNER_OVERWHELMING) + 1);
                kidsDefeated = rand.nextInt((int) (defenderUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_LOSER_OVERWHELMING) + 1);
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ATTACK_VICTORY_OVERWHELMING));
            }
        } else {
            if (powerGapRating > -.9) {
                kidsLost = rand.nextInt((int) (attackerUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_LOSER) + 1);
                kidsDefeated = rand.nextInt((int) (defenderUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_WINNER) + 1);
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ATTACK_DEFEAT));
            } else {
                kidsLost = rand.nextInt((int) (attackerUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_LOSER_OVERWHELMING) + 1);
                kidsDefeated = rand.nextInt((int) (defenderUpperBoundLoss * EggConstants.ATTACK_LOSS_MULTIPLIER_WINNER_OVERWHELMING) + 1);
                responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ATTACK_DEFEAT_OVERWHELMING, defendingCastle.getNameOfOwner()));
            }
        }
        responseObject.addMessage(EggUtils.constructFormattedString(EggMessages.ATTACK_BATTLE_SUMMARY, nameOfOwner, kidsLost, defendingCastle.getNameOfOwner(), kidsDefeated));
        attackersHatchery.updateKidCount(KidType.NORMAL, -kidsLost);
        defendersHatchery.updateKidCount(KidType.NORMAL, -kidsDefeated);
    }

    public void takeDamage(final long damageTaken) {
        this.currentHealth = currentHealth - damageTaken <= 0 ? 0 : currentHealth - damageTaken;
    }

    @Override
    public String toString() {
        return castleName + "\n :heart: : " + currentHealth + "/" + maximumHealth + " :crossed_swords: : " + getAttackValue() + " :shield: : " + getDefenseValue() + "\n" +
                BrobotConstants.SEPARATOR + "\n\t:egg: : " + hatchery.getEggCount(EggType.BASIC) + "\n\t:baby: : " + hatchery.getKidCount(KidType.NORMAL);
    }

}