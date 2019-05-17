package brobot.eggthemall.encounter;

import brobot.BrobotConstants;
import brobot.BrobotListener;
import brobot.BrobotUtils;
import brobot.eggthemall.encounter.monster.Monster;
import brobot.eggthemall.encounter.monster.Pokemon;
import brobot.pokemon.PokemonInfo;

import java.io.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomEncounterGenerator {
    public Encounter generateRandomEncounter() {
        final Random random = new Random();
        final String imagesPathMac = "src/main/java/brobot/eggthemall/encounter/monster/images/pokemon/full";
//        final String imagesPathWindows = "\"..\\\\brobot\\\\src\\\\main\\\\java\\\\brobot\\\\eggthemall\\\\encounter\\\\monster\\\\images\\\\pokemon\\\\full\";"
        final File[] monsters = new File(imagesPathMac).listFiles(File::isFile);
        final File monsterFile = monsters[random.nextInt(monsters.length)];
        String[] parts = monsterFile.getAbsolutePath().split("\\\\");
        String monsterImageName = parts[parts.length-1];
        PokemonInfo pokemonInfo = BrobotListener.pokedex.get(
                monsterImageName.substring(monsterImageName.lastIndexOf(".") - 3).split("\\.")[0]);

        final Monster monster;
        if (pokemonInfo == null) {
            String[] nameParts = monsterImageName.split("\\.")[0].split("_");
            StringBuilder nameBldr = new StringBuilder();
            BrobotUtils.concatAndCamelCaseStrings(nameBldr, nameParts);
            final String monsterName = nameBldr.toString();
            final long monsterHealth = 100l;
            final long monsterAttack = 10l;
            final long monsterDefense = 10l;
            final long eggRewardCount = 1000l;
            monster = new Monster(monsterName, monsterFile.getAbsolutePath(), monsterHealth, monsterAttack, monsterDefense, eggRewardCount);
        } else {
            final int eggRewardAmount = pokemonInfo.getHp() + ThreadLocalRandom.current().nextInt(0, pokemonInfo.getAttack() + pokemonInfo.getDefense());
            monster = new Pokemon(pokemonInfo.getName(), monsterFile.getAbsolutePath(), (long) pokemonInfo.getHp(), (long) pokemonInfo.getAttack(),
                    (long) pokemonInfo.getDefense(), pokemonInfo.getSpecialAttack(), pokemonInfo.getSpecialDefense(), pokemonInfo.getSpeed(), eggRewardAmount);
        }

        return new Encounter(monster, EncounterConstants.ENCOUNTER_DURATION_DEFAULT);
    }
}
