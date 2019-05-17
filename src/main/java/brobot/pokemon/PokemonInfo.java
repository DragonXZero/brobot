package brobot.pokemon;

import java.util.*;

public class PokemonInfo {
    private static final Map<String, List<String>> pokedex;
    static {
        Map<String, List<String>> tmpMap = new HashMap<>();
        tmpMap.put("001", Arrays.asList(new String[] {"Bulbasaur"}));
        tmpMap.put("002", Arrays.asList(new String[] {"Ivysaur"}));
        pokedex = Collections.unmodifiableMap(tmpMap);
    }
}
