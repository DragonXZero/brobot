package brobot;

import brobot.eggthemall.EggUtils;

public class BrobotUtils {

    public static void tickle(final String user, final StringBuilder messageToSend) {
        messageToSend.append("Did someone say tickle??? You tickle")
            .append(EggUtils.bold(user))
            .append("until they poo their pants a little bit. You regret it.");
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null ? true : s.isEmpty();
    }
}