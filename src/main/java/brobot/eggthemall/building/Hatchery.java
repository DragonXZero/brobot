package brobot.eggthemall.building;

import brobot.eggthemall.EggConstants;
import brobot.eggthemall.egg.EggType;
import brobot.eggthemall.kid.KidType;

import java.util.*;

public class Hatchery {
    private final Map<EggType, Long> eggTypeCounts = new HashMap<>();
    private final Map<KidType, Long> kidTypeCounts = new HashMap<>();

    public Hatchery () {
        for (final EggType eggType : EggType.values()) {
            eggTypeCounts.put(eggType, 0l);
        }

        eggTypeCounts.put(EggType.BASIC, EggConstants.EGG_OVULATION_AMOUNT);

        for (final KidType kidType : KidType.values()) {
            kidTypeCounts.put(kidType, 0l);
        }
    }

    public long getEggCount(final EggType eggType) {
        return eggTypeCounts.get(eggType);
    }

    // TODO - What should be the format for returning the counts of all types of eggs?
    public void getAllEggCounts() {
    }

    public long getKidCount(final KidType kidType) {
        return kidTypeCounts.get(kidType);
    }

    // TODO - What should be the format for returning the counts of all types of kids?
    public void getAllKidCounts() {
    }

    public HatchResults hatchEggs(final EggType eggType, final int numEggsToHatch) {
        final long eggCount = eggTypeCounts.get(eggType);
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

                // TODO - Use a PMF to determine what tyoe of kid is hatched.
                if (val <= EggConstants.HATCHERY_BASIC_EGG_ROLL_COMMON_THRESHOLD) {
                    hatchedKidType = KidType.NORMAL;
                } else if (val <= EggConstants.HATCHERY_BASIC_EGG_ROLL_UNCOMMON_THRESHOLD) {
                    hatchedKidType = KidType.UNCOMMON;
                } else if (val <= EggConstants.HATCHERY_BASIC_EGG_ROLL_RARE_THRESHOLD) {
                    hatchedKidType = KidType.RARE;
                } else {
                    hatchedKidType = KidType.LEGENDARY;
                }

                hatchResults.addResult(hatchedKidType);
                ++numHatchedEggs;
            }
            eggTypeCounts.put(eggType, eggCount - numEggsToHatch);

            return hatchResults;
        }
    }

    public void updateEggCount(EggType eggType, long eggCountDelta) {
        eggTypeCounts.put(eggType, eggTypeCounts.get(eggType) + eggCountDelta);
    }

    public void updateKidCount(KidType kidType, long kidCountDelta) {
        kidTypeCounts.put(kidType, kidTypeCounts.get(kidType) + kidCountDelta);
    }
}