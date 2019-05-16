package brobot.eggthemall;

import brobot.eggthemall.building.Hatchery;
import brobot.eggthemall.castle.Castle;
import brobot.eggthemall.egg.EggType;
import net.dv8tion.jda.core.entities.User;

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
        Gives all participants of EggThemAll some amount of eggs. The exact calculation is pending.
    */
    public void updateResoures(final Map<User, Castle> castles) {
        final long currentTime = System.currentTimeMillis();
        final int numEggBlessings = (int) (currentTime - lastUpdated) / updateFrequency * eggBlessingIncrement;
        final int eggBlessingAmount = numEggBlessings * eggBlessingIncrement;

        if (eggBlessingAmount > 0) {
            for (final Castle castle : castles.values()) {
                final Hatchery hatchery = castle.getHatchery();
                hatchery.updateEggCount(EggType.BASIC, eggBlessingAmount);
            }
            lastUpdated = currentTime;
        }
    }
}
