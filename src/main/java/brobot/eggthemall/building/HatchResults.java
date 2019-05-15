package brobot.eggthemall.building;

import brobot.eggthemall.kid.KidType;

import java.util.LinkedHashMap;
import java.util.Map;

public class HatchResults {
    private final Map<KidType, Integer> results = new LinkedHashMap<>();

    public HatchResults() {
        for (final KidType kidType : KidType.values()) {
            this.results.put(kidType, 0);
        }
    }

    public Map<KidType, Integer> getResults() {
        return this.results;
    }

    public void addResult(final KidType kidType) {
        this.results.put(kidType, this.results.get(kidType) + 1);
    }
}
