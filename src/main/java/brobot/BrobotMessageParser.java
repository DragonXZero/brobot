package brobot;

import brobot.command.CommandType;
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

    public void parseMessage(final ResponseObject responseObject, final Message discordMessage) {
        final String content = discordMessage.getContentDisplay();
        final User author = discordMessage.getAuthor();

        final String[] parts = content.substring(1).split(" ");
        final List<String> commandParts = new ArrayList<>(Arrays.asList(parts));

        final CommandType commandType = commandParts.get(0).equals(BrobotConstants.MARKOV_COMMAND_PREFIX) || commandParts.get(0).equals(BrobotConstants.MARKOV_COMMAND_PREFIX_SHORTCUT)
                ? CommandType.MARKOV : CommandType.EGG_THEM_ALL;
        RequestObject requestObject = new RequestObject(commandType, commandParts, discordMessage.getMentionedMembers(), author, content.toLowerCase());
        requestObject.executeCommand();
        eggCommandHandlerImpl.executeCommand(requestObject, responseObject);
    }
}