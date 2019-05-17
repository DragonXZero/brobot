package brobot.eggthemall.encounter;

import brobot.eggthemall.encounter.monster.Monster;

import java.io.File;
import java.util.Random;

public class RandomEncounterGenerator {
    public Encounter generateRandomEncounter() {
        final Random random = new Random();
        final File[] monsters = new File("..\\brobot\\src\\main\\java\\brobot\\eggthemall\\encounter\\monster\\images\\ffi").listFiles(File::isFile);

        final File monsterFile = monsters[random.nextInt(monsters.length)];
        String[] parts = monsterFile.getAbsolutePath().split("\\\\");
        String monsterImageName = parts[parts.length-1];
        String name = monsterImageName.split("\\.")[0];
        String[] nameParts = name.split("_");

        StringBuilder nameBldr = new StringBuilder();
        for (String namePart : nameParts) {
            nameBldr.append(namePart.substring(0, 1).toUpperCase() + namePart.substring(1)).append(" ");
        }
        nameBldr.setLength(nameBldr.length()-1);

        final String monsterName = nameBldr.toString();
        final long monsterHealth = 100l;
        final long monsterAttack = 10l;
        final long monsterDefense = 10l;
        final long eggRewardCount = 1000l;

        final Monster monster = new Monster(monsterName, monsterFile.getAbsolutePath(), monsterHealth, monsterAttack, monsterDefense, eggRewardCount);
        final Encounter encounter = new Encounter(monster, EncounterConstants.ENCOUNTER_DURATION_DEFAULT);

        return encounter;
    }
}
