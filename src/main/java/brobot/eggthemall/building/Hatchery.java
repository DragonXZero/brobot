package brobot.eggthemall.building;

import brobot.eggthemall.EggConstants;
import brobot.eggthemall.egg.EggType;
import brobot.eggthemall.kid.KidType;

import java.util.*;

public class Hatchery {
    // TODO - Is an "Egg" class actually necessary? Some discussion needs to take place
    private final Map<EggType, Integer> eggTypeCounts = new HashMap<>();

    public Hatchery () {
        for (final EggType eggType : EggType.values()) {
            eggTypeCounts.put(eggType, 0);
        }
    }

    // TODO - What should be the format for returning the counts of all types of eggs?
    public void getEggCount() {
    }

    public int getEggCount(final EggType eggType) {
        return eggTypeCounts.get(eggType);
    }

    public HatchResults hatchEggs(final EggType eggType, final int numEggsToHatch) {
        final int eggCount = eggTypeCounts.get(eggType);
        if (eggCount < numEggsToHatch) {
            // TODO - Return an error
            return null;
        } else {
            final HatchResults hatchResults = new HatchResults();
            final Random rand = new Random();

            int numHatchedEggs = 0;
            while (numHatchedEggs < numEggsToHatch) {
                final int val = rand.nextInt(EggConstants.HATCHERY_BASIC_EGG_ROLL_RESULT_MAX);
                final KidType hatchedKidType;

                if (val < EggConstants.HATCHERY_BASIC_EGG_ROLL_COMMON_THRESHOLD) {
                    hatchedKidType = KidType.NORMAL;
                } else if (val < EggConstants.HATCHERY_BASIC_EGG_ROLL_UNCOMMON_THRESHOLD) {
                    hatchedKidType = KidType.UNCOMMON;
                } else if (val < EggConstants.HATCHERY_BASIC_EGG_ROLL_RARE_THRESHOLD) {
                    hatchedKidType = KidType.RARE;
                } else {
                    hatchedKidType = KidType.LEGENDARY;
                }

                hatchResults.addResult(hatchedKidType);
                ++numHatchedEggs;
            }

            return hatchResults;
        }
    }
}