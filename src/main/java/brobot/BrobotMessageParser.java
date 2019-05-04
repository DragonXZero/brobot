package brobot;

import brobot.eggthemall.EggCommandHandlerImpl;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrobotMessageParser {
    private EggCommandHandlerImpl eggCommandHandlerImpl;

    public BrobotMessageParser() {
        eggCommandHandlerImpl = new EggCommandHandlerImpl();
    }

    public StringBuilder parseMessage(final Message discordMessage) {
        final StringBuilder response = new StringBuilder();
        final String content = discordMessage.getContentDisplay();
        final User author = discordMessage.getAuthor();

        final String[] parts = content.substring(1).split(" ");
        final String commandType = parts[0];

        final List<String> commandParts = new ArrayList<>(Arrays.asList(parts));
        commandParts.remove(0);

        BrobotCommand command = new BrobotCommand(commandType, commandParts, discordMessage.getMentionedMembers(), author, content);
        switch (command.getCommandType()) {
            case "egg" :
                eggCommandHandlerImpl.executeCommand(command, response);
                break;
            default :
                response.append("This aint not no invalid command yo");
        }

        return response;
    }
}