package brobot.eggthemall.encounter;

import brobot.eggthemall.encounter.monster.Monster;

public class Encounter {
    private final Monster monster;
    private long duration;

    public Encounter (final Monster monster, long duration) {
        this.monster = monster;
        this.duration = duration;
    }

    public Monster getMonster() {
        return this.monster;
    }
}
