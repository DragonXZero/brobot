package brobot;

import brobot.eggthemall.EggUtils;
import net.dv8tion.jda.core.entities.User;

public class BrobotUtils {

    public static void tickle(final User user, final StringBuilder messageToSend) {
        messageToSend.append("Did someone say tickle??? You tickle ")
            .append(EggUtils.bold(user.getName()))
            .append(" until they poo their pants a little bit. You regret it.");
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null ? true : s.isEmpty();
    }
}