package brobot.eggthemall.encounter;

import brobot.Brobot;
import brobot.Utils;
import brobot.eggthemall.monster.Monster;
import brobot.eggthemall.monster.Pokemon;
import brobot.pokemon.PokemonInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomEncounterGenerator {
    public Encounter generateRandomEncounter() {
        final Random random = new Random();
        final String imagesPathMac = "src/main/java/brobot/eggthemall/encounter/monster/images/pokemon/full";

        Object[] monsters = new File[] {};
        try {
            monsters = Files.walk(Paths.get(imagesPathMac)).toArray();
        } catch (IOException e) {
            //fail silently
        }

        final File monsterFile = ((Path) monsters[random.nextInt(monsters.length)]).toFile();
        String[] parts = monsterFile.getAbsolutePath().split("\\\\");
        String monsterImageName = parts[parts.length-1];
        PokemonInfo pokemonInfo = Brobot.pokedex.get(
                monsterImageName.substring(monsterImageName.lastIndexOf(".") - 3).split("\\.")[0]);

        final Monster monster;
        if (pokemonInfo == null) {
            String[] pathParts = monsterImageName.split("/");
            String[] nameParts = pathParts[pathParts.length-1].split("\\.")[0].split("_");
            StringBuilder nameBldr = new StringBuilder();
            Utils.concatAndCamelCaseStrings(nameBldr, nameParts);
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
