package brobot.eggthemall.castle;

import brobot.eggthemall.building.Hatchery;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;

public class Castle {
    private final User owner;
    private final String ownerName;
    private final String castleName;
    private final Hatchery hatchery;
    private final Map<String, Map<String, Long>> resourceCounts = new HashMap<>();

    public Castle (final User owner) {
        this.owner = owner;
        this.ownerName = owner.getName();
        this.castleName = ownerName + "'s Castle";
        this.hatchery = new Hatchery();
    }

    public User getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCastleName() {
        return this.castleName;
    }

    public Hatchery getHatchery() {
        return this.hatchery;
    }
}