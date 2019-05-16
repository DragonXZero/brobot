package brobot.eggthemall.castle;

import brobot.BrobotConstants;
import brobot.eggthemall.building.Hatchery;
import brobot.eggthemall.egg.EggType;
import brobot.eggthemall.kid.KidType;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;

public class Castle {
    private final User owner;
    private final String nameOfOwner;
    private final String castleName;
    private final Hatchery hatchery;
    private final Map<String, Map<String, Long>> resourceCounts = new HashMap<>();

    public Castle (final User owner) {
        this.owner = owner;
        this.nameOfOwner = owner.getName();
        this.castleName = nameOfOwner + "'s Castle";
        this.hatchery = new Hatchery();
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

    @Override
    public String toString() {
        return castleName + "\t :crossed_swords: : " + getAttackValue() + " :shield: : " + getDefenseValue() + "\n" +
                BrobotConstants.SEPARATOR + "\n\t:egg: : " + hatchery.getEggCount(EggType.BASIC) + "\n\t:baby: : " + hatchery.getKidCount(KidType.NORMAL);
    }

}