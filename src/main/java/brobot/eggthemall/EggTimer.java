package brobot.eggthemall;

import java.util.Map;

public class EggTimer {
    private long lastUpdated = -1;
    private int updateFrequency; // milliseconds
    private int eggBlessingIncrement;

    public EggTimer(final int updateFrequency, final int eggBlessingIncrement) {
        this.updateFrequency = updateFrequency;
        this.eggBlessingIncrement = eggBlessingIncrement;
        this.lastUpdated = System.currentTimeMillis();
    }

    /*
        Gives all participants of EggTimer some amount of eggs. The exact calculation is pending.
    */
    public void updateResoures(final Map<String, Long> eggCount) {
        final long currentTime = System.currentTimeMillis();
        final int numEggBlessings = (int) (currentTime - lastUpdated) / updateFrequency * eggBlessingIncrement;
        final int eggBlessingAmount = numEggBlessings * eggBlessingIncrement;

        if (eggBlessingAmount > 0) {
            for (String user : eggCount.keySet()) {
                long userEggCount = eggCount.get(user) + eggBlessingAmount;
                eggCount.put(user, userEggCount);
            }

            lastUpdated = currentTime;
        }
    }
}
