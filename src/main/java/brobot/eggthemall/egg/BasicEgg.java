package brobot.eggthemall.egg;

import brobot.eggthemall.kid.Kid;
import brobot.eggthemall.kid.NormalKid;

public class BasicEgg extends Egg {

    public Kid hatch() {
        return new NormalKid();
    }
}
