package brobot.eggthemall.encounter;

import brobot.eggthemall.encounter.monster.Monster;

public class RandomEncounterGenerator {
    public Encounter generateRandomEncounter() {
        final String monsterName = "Goblin";
        final long monsterHealth = 100l;
        final long monsterAttack = 10l;
        final long monsterDefense = 10l;

        final Monster monster = new Monster(monsterName, monsterHealth, monsterAttack, monsterDefense);
        final Encounter encounter = new Encounter(monster, EncounterConstants.ENCOUNTER_DURATION_DEFAULT);

        return encounter;
    }
}
