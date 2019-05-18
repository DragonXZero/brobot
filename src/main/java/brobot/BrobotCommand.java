package brobot;

import java.util.List;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;


public class BrobotCommand {
    private final String commandType;
    private final List<String> commandParts;
    private final List<Member> mentionedUsers;
    private final User author;
    private final String rawCommand;

    public BrobotCommand(final String commandType, final List<String> commandParts, final List<Member> mentionedUsers,
                         final User author, final String rawCommand) {
        this.commandType = commandType;
        this.commandParts = commandParts;
        this.mentionedUsers = mentionedUsers;
        this.author = author;
        this.rawCommand = rawCommand;
    }

    public String getCommandType() {
        return commandType;
    }

    public List<String> getCommandParts() {
        return commandParts;
    }

    public List<Member> getMentionedUsers() {
        return mentionedUsers;
    }

    public User getAuthor() {
        return author;
    }

    public String getRawCommand() {
        return rawCommand;
    }

    public String getCommand() {
        return commandParts.get(0);
    }

    public long getCommandVal() {
        if (commandParts.size() > 1 && !(commandParts.get(1).charAt(0) + "").equals("@")) {
            return Long.parseLong(commandParts.get(1));
        }
        return 0l;
    }
}