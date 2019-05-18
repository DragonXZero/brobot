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
        final String command = brobotCommand.getCommand();
        final long commandVal = brobotCommand.getCommandVal();
        final List<Member> members = brobotCommand.getMentionedUsers();

        if (members != null && members.size() == 1) {
            // Commands that require a mentioned user referred to as the defender
            final User defender = members.get(0).getUser();
            eggThemAll.initializeCastleIfNotInitialized(attacker, defender);

            switch (command) {
                case EggConstants.CMD_TICKLE: {
                    BrobotUtils.tickle(defender, response);
                    break;
                }
                case EggConstants.CMD_OVULATE:
                case EggConstants.CMD_OVULATE_SHORTCUT: {
                    eggThemAll.ovulate(defender, response);
                    break;
                }
                case EggConstants.CMD_STEAL_EGGS:
                case EggConstants.CMD_STEAL_EGGS_SHORTCUT: {
                    eggThemAll.stealEggs(attacker, defender, response);
                    break;
                }
                case EggConstants.CMD_GIVE_KIDS:
                case EggConstants.CMD_GIVE_KIDS_SHORTCUT: {
                    eggThemAll.giveKids(attacker, defender, response, commandVal);
                    break;
                }
                case EggConstants.CMD_ATTACK:
                case EggConstants.CMD_ATTACK_SHORTCUT: {
                    eggThemAll.attack(attacker, defender, response);
                    break;
                }
                default: {
                    responseObject.addMessage(EggMessages.INVALID_COMMAND);
                }
            }
        } else {
            // Commands that do not require a mentioned user, these might be global
            eggThemAll.initializeCastleIfNotInitialized(attacker);

            switch (command) {
                case EggConstants.CMD_FERTILIZE_EGGS:
                case EggConstants.CMD_FERTILIZE_EGGS_SHORTCUT: {
                    eggThemAll.fertilize(attacker, response, commandVal);
                    break;
                }
                case EggConstants.CMD_COPULATE:
                case EggConstants.CMD_COPULATE_SHORTCUT: {
                    eggThemAll.copulate(attacker, response);
                    break;
                }
                case EggConstants.CMD_DISPLAY_CASTLE_INFO_SHORTCUT:
                case EggConstants.CMD_DISPLAY_CASTLE_INFO: {
                    eggThemAll.displayCastleInfo(attacker, response);
                    break;
                }
                case EggConstants.CMD_GENERATE_RANDOM_ENCOUNTER:
                case EggConstants.CMD_GENERATE_RANDOM_ENCOUNTER_SHORTCUT: {
                    eggThemAll.generateRandomEncounter(responseObject);
                    break;
                }
                case EggConstants.CMD_ENCOUNTER_ATTACK:
                case EggConstants.CMD_ENCOUNTER_ATTACK_SHORTCUT: {
                    eggThemAll.processEncounterAttack(attacker, responseObject);
                    break;
                }
                case EggConstants.CMD_ENCOUNTER_FLEE:
                case EggConstants.CMD_ENCOUNTER_FLEE_SHORTCUT: {
                    eggThemAll.processEncounterFlee(attacker, responseObject);
                    break;
                }
                case EggConstants.CMD_DISPLAY_RESOURCE_COUNT: {
                    eggThemAll.getResourceCounts(attacker, response);
                    break;
                }
                case EggConstants.CMD_DISPLAY_EGGBOARD: {
                    response.append(EggMessages.UNDER_CONSTRUCTION);
                    break;
                }
                case EggConstants.CMD_THANOS: {
                    eggThemAll.eatCake(response);
                    break;
                }
            }
        }
    }
}