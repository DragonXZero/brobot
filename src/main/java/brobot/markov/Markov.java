package brobot.markov;

import brobot.ResponseObject;
import net.dv8tion.jda.core.entities.User;

public class Markov {

    public Markov() {
    }

    //these could return string
    public String getMostFrequentlyUsedWord(final ResponseObject responseObject, final User user){
        return "";
    }

    //these shouldnt return string
    public String getNMostFrequentlyUsedWord(final ResponseObject responseObject, final User user){
        return "";
    }
}
