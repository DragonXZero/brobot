package brobot.command;

import java.util.ArrayList;
import java.util.List;

public class BrobotCommandExecutor {
    private final List<Command> commands = new ArrayList<>();

    public void executeCommand() {
        for (Command command : commands) {
            command.execute();
        }
    }


}
