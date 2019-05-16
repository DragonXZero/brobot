package brobot.eggthemall;

import brobot.BrobotCommand;
import brobot.CommandHandler;
import brobot.BrobotUtils;
import brobot.ResponseObject;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class EggCommandHandlerImpl implements CommandHandler {
    private final EggThemAll eggThemAll;

    public EggCommandHandlerImpl () {
        eggThemAll = new EggThemAll();
    }

    public void executeCommand(final BrobotCommand brobotCommand, final ResponseObject responseObject) {
        eggThemAll.updateResources();

        final StringBuilder response = responseObject.getResponseBldr();
        final User attacker = brobotCommand.getAuthor();
        final String rawCommand = brobotCommand.getRawCommand();
        final List<Member> members = brobotCommand.getMentionedUsers();

        if (members != null && members.size() == 1) {
            // Commands that require a mentioned user referred to as the defender
            final User defender = members.get(0).getUser();
            eggThemAll.initializeCastleIfNotInitialized(attacker, defender);

            if (rawCommand.contains(EggConstants.CMD_TICKLE)) {
                BrobotUtils.tickle(defender, response);
            } else if (rawCommand.contains(EggConstants.CMD_OVULATE)) {
                eggThemAll.ovulate(defender, response);
            } else if (rawCommand.contains(EggConstants.CMD_STEAL_EGGS)) {
                eggThemAll.stealEggs(attacker, defender, response);
            } else if (rawCommand.contains(EggConstants.CMD_GIVE_KIDS)) {
                eggThemAll.giveKids(attacker, defender, response, rawCommand);
            } else if (rawCommand.contains(EggConstants.CMD_ATTACK)) {
                eggThemAll.attack(attacker, defender, response);
            }
        } else {
            // Commands that do not require a mentioned user, these might be global
            eggThemAll.initializeCastleIfNotInitialized(attacker);

            if (rawCommand.contains(EggConstants.CMD_FERTILIZE_EGGS)) {
                eggThemAll.fertilize(attacker, response, rawCommand);
            } else if (rawCommand.contains(EggConstants.CMD_COPULATE)) {
                eggThemAll.copulate(attacker, response);
            } else if (rawCommand.contains(EggConstants.CMD_THANOS)) {
                eggThemAll.eatCake(response);
            } else if (rawCommand.contains(EggConstants.CMD_DISPLAY_RESOURCE_COUNT)) {
                eggThemAll.getResourceCounts(attacker, response);
            } else if (rawCommand.contains(EggConstants.CMD_DISPLAY_EGGBOARD)) {
                response.append(EggMessages.UNDER_CONSTRUCTION);
//                List<StringBuilder> bldrs = eggThemAll.displayLeaderBoard();
//                for (StringBuilder bldr : bldrs) {
//                    channel.sendMessage(bldr.toString()).queue();
//                }
            } else if (rawCommand.contains(EggConstants.CMD_DISPLAY_CASTLE_INFO)) {
                eggThemAll.displayCastleInfo(attacker, response);
            } else if (rawCommand.contains(EggConstants.CMD_GENERATE_RANDOM_ENCOUNTER)) {
                eggThemAll.generateRandomEncounter(responseObject);
            }
        }
    }
}