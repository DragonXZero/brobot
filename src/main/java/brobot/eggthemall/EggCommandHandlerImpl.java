package brobot.eggthemall;

import brobot.BrobotCommand;
import brobot.CommandHandler;
import brobot.BrobotUtils;
import net.dv8tion.jda.core.entities.Member;

import java.util.List;

public class EggCommandHandlerImpl implements CommandHandler {
    private final EggThemAll eggThemAll;

    public EggCommandHandlerImpl () {
        eggThemAll = new EggThemAll();
    }

    public void executeCommand(final BrobotCommand brobotCommand, final StringBuilder response) {
        final String authorsName = brobotCommand.getAuthor().getName();

        String msg = brobotCommand.getRawCommand();

        List<Member> members = brobotCommand.getMentionedUsers();
        if (members != null && members.size() == 1) {
            // Here are user-specific commands
            eggThemAll.updateResources();

            Member mentionedUser = members.get(0);
            String mentionedUsersName = mentionedUser.getUser().getName();
            String attacker = authorsName;
            String defender = mentionedUsersName;

            if (mentionedUsersName.equals("brobot")) {
//                EggUtils.markov(channel, response);
                response.append("This is broken right now D:");
            } else if (msg.toLowerCase().contains("tickle")) {
                BrobotUtils.tickle(mentionedUsersName, response);
            } else if (msg.toLowerCase().contains("give eggs")) {
                eggThemAll.ovulate(mentionedUsersName, response);
            } else if (msg.toLowerCase().contains("steal eggs")) {
                eggThemAll.stealEggs(attacker, defender, response);
            } else if (msg.toLowerCase().contains("give kids")) {
                eggThemAll.giveKids(attacker, defender, response, msg);
            }
        } else {
            // Here are global commands that affects all users participating in the game
            eggThemAll.updateResources();

            if (msg.toLowerCase().contains("brobot who likes")) {
//                EggUtils.reverseLookup(channel, msg);
                response.append("This is broken right now D:");
            } else if (msg.toLowerCase().contains("fertilize")) {
                eggThemAll.fertilize(authorsName, response, msg);
            } else if (msg.toLowerCase().contains("copulate")) {
                eggThemAll.copulate(authorsName, response);
            } else if (msg.toLowerCase().contains("let them eat cake")) {
                eggThemAll.eatCake(response);
            } else if (msg.toLowerCase().contains("count my eggs")) {
                eggThemAll.getResourceCount(authorsName, response);
            } else if (msg.toLowerCase().contains("eggboard")) {
                response.append("This is broken right now D:");
//                List<StringBuilder> bldrs = eggThemAll.displayLeaderBoard();
//                for (StringBuilder bldr : bldrs) {
//                    channel.sendMessage(bldr.toString()).queue();
//                }
            }
        }
    }
}