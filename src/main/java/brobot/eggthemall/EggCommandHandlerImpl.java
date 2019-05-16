package brobot.eggthemall;

import brobot.BrobotCommand;
import brobot.CommandHandler;
import brobot.BrobotUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class EggCommandHandlerImpl implements CommandHandler {
    private final EggThemAll eggThemAll;

    public EggCommandHandlerImpl () {
        eggThemAll = new EggThemAll();
    }

    public void executeCommand(final BrobotCommand brobotCommand, final StringBuilder response) {
        final User attacker = brobotCommand.getAuthor();
        final String msg = brobotCommand.getRawCommand();

        List<Member> members = brobotCommand.getMentionedUsers();
        if (members != null && members.size() == 1) {
            // Commands that require a mentioned user
            eggThemAll.updateResources();

            final User defender = members.get(0).getUser();

            if (msg.toLowerCase().contains("tickle")) {
                BrobotUtils.tickle(defender, response);
            } else if (msg.toLowerCase().contains("give eggs")) {
                eggThemAll.ovulate(defender, response);
            } else if (msg.toLowerCase().contains("steal eggs")) {
                eggThemAll.stealEggs(attacker, defender, response);
            } else if (msg.toLowerCase().contains("give kids")) {
                eggThemAll.giveKids(attacker, defender, response, msg);
            }
        } else {
            // Commands that do not require a mentioned user, these might be global
            eggThemAll.updateResources();

            if (msg.toLowerCase().contains("brobot who likes")) {
//                EggUtils.reverseLookup(channel, msg);
                response.append("This is broken right now D:");
            } else if (msg.toLowerCase().contains("fertilize")) {
                eggThemAll.fertilize(attacker, response, msg);
            } else if (msg.toLowerCase().contains("copulate")) {
                eggThemAll.copulate(attacker, response);
            } else if (msg.toLowerCase().contains("let them eat cake")) {
                eggThemAll.eatCake(response);
            } else if (msg.toLowerCase().contains("count my eggs")) {
                eggThemAll.getResourceCount(attacker, response);
            } else if (msg.toLowerCase().contains("eggboard")) {
                response.append("This is broken right now D:");
//                List<StringBuilder> bldrs = eggThemAll.displayLeaderBoard();
//                for (StringBuilder bldr : bldrs) {
//                    channel.sendMessage(bldr.toString()).queue();
//                }
            } else if (msg.toLowerCase().contains("castle")) {
                eggThemAll.displayCastleInfo(attacker, response);
            }
        }
    }
}