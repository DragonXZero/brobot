package brobot;

import java.util.List;

import brobot.command.CommandType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;


public class RequestObject {
    private final CommandType commandType;
    private final List<String> commandParts;
    private final List<Member> mentionedUsers;
    private final User author;
    private final String rawCommand;

    public RequestObject(final CommandType commandType, final List<String> commandParts, final List<Member> mentionedUsers,
                         final User author, final String rawCommand) {
        this.commandType = commandType;
        this.commandParts = commandParts;
        this.mentionedUsers = mentionedUsers;
        this.author = author;
        this.rawCommand = rawCommand;
    }

    public CommandType getCommandType() {
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

    public void executeCommand(ResponseObject responseObject) {
        if (commandType == CommandType.EGG_THEM_ALL) {
            CommandExecutorManager.EGG_THEM_ALL_EXECUTOR.executeCommand(this, responseObject);
        } else if (commandType == CommandType.MARKOV) {
            CommandExecutorManager.MARKOV.executeCommand(this, responseObject);
        }
    }
}