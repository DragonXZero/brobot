package brobot;

import brobot.eggthemall.EggThemAllCommandExecutorImpl;
import brobot.markov.MarkovCommandExecutorImpl;

public class CommandExecutorManager {
    public static final BrobotCommandExecutorImpl BROBOT_COMMAND_EXECUTOR = new BrobotCommandExecutorImpl();
    public static final EggThemAllCommandExecutorImpl EGG_THEM_ALL_EXECUTOR = new EggThemAllCommandExecutorImpl();
    public static final MarkovCommandExecutorImpl MARKOV = new MarkovCommandExecutorImpl();
}
