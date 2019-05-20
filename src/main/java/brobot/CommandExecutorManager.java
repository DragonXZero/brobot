package brobot;

import brobot.eggthemall.EggThemAllCommandExecutor;
import brobot.markov.MarkovCommandExecutor;

public class CommandExecutorManager {
    public static final EggThemAllCommandExecutor EGG_THEM_ALL_EXECUTOR = new EggThemAllCommandExecutor();
    public static final MarkovCommandExecutor MARKOV = new MarkovCommandExecutor();
}
