package brobot;

import brobot.command.CommandType;
import brobot.mudae.MudaeConstants;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageParser {
    public RequestObject parseMessage(final ResponseObject responseObject, final Message discordMessage) {
        final String content = discordMessage.getContentDisplay();
        final User author = discordMessage.getAuthor();

        final List<String> commandParts = new ArrayList<>(Arrays.asList(content.substring(1).split(" ")));

        final String commandPrefix = commandParts.get(0);
        final CommandType commandType;
        switch (commandPrefix.charAt(0)+"") {
            case BrobotConstants.MARKOV_COMMAND_PREFIX:
            case BrobotConstants.MARKOV_COMMAND_PREFIX_SHORTCUT: {
                commandType = CommandType.MARKOV;
                commandParts.remove(0);
                break;
            }
            case BrobotConstants.MUDAE_PREFIX: {
                commandType = CommandType.MUDAE;
                commandParts.remove(0);
                break;
            }
            default: {
                commandType = CommandType.EGG_THEM_ALL;
            }
        }

        return new RequestObject(commandType, commandParts, discordMessage.getMentionedMembers(), author, content.toLowerCase(),
                discordMessage.getChannel().getId(), discordMessage);
    }

    public RequestObject createMudaeMessage(final ResponseObject responseObject, final Message discordMessage) {
        return new RequestObject(CommandType.MUDAE, new ArrayList<>(Arrays.asList(new String[] {MudaeConstants.CMD_SPECIAL})),
                null, null, null, discordMessage.getChannel().getId(), discordMessage);
    }
}