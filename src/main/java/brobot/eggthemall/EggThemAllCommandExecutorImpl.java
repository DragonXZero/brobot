package brobot.eggthemall;

import brobot.*;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class EggThemAllCommandExecutorImpl implements CommandExecutor {
    private final EggThemAll eggThemAll;

    public EggThemAllCommandExecutorImpl()  {
        this.eggThemAll = new EggThemAll();
    }

    @Override
    public void executeCommand(final ResponseObject responseObject, final RequestObject requestObject) {
        eggThemAll.updateResources();

        final User attacker = requestObject.getAuthor();
        final String command = requestObject.getCommand();
        final long commandVal = requestObject.getCommandVal();
        final List<Member> members = requestObject.getMentionedUsers();

        if (members != null && members.size() == 1) {
            // Commands that require a mentioned user referred to as the defender
            final User defender = members.get(0).getUser();
            switch (command) {
                case EggConstants.CMD_TICKLE: {
                    Utils.tickle(responseObject, defender);
                    break;
                }
                case EggConstants.CMD_OVULATE:
                case EggConstants.CMD_OVULATE_SHORTCUT: {
                    eggThemAll.ovulate(responseObject, defender);
                    break;
                }
                case EggConstants.CMD_STEAL_EGGS:
                case EggConstants.CMD_STEAL_EGGS_SHORTCUT: {
                    eggThemAll.stealEggs(responseObject, attacker, defender);
                    break;
                }
                case EggConstants.CMD_GIVE_KIDS:
                case EggConstants.CMD_GIVE_KIDS_SHORTCUT: {
                    eggThemAll.giveKids(responseObject, attacker, defender, commandVal);
                    break;
                }
                case EggConstants.CMD_ATTACK:
                case EggConstants.CMD_ATTACK_SHORTCUT: {
                    eggThemAll.attack(responseObject, attacker, defender);
                    break;
                }
                default: {
                    responseObject.addMessage(EggMessages.INVALID_COMMAND);
                }
            }
        } else {
            // Commands that do not require a mentioned user, these might be global
            switch (command) {
                case EggConstants.CMD_HELP:
                case EggConstants.CMD_HELP_SHORTCUT: {
                    eggThemAll.displayHelpInformation(responseObject);
                    break;
                }
                case EggConstants.CMD_FERTILIZE_EGGS:
                case EggConstants.CMD_FERTILIZE_EGGS_SHORTCUT: {
                    eggThemAll.fertilize(responseObject, attacker, commandVal);
                    break;
                }
                case EggConstants.CMD_COPULATE:
                case EggConstants.CMD_COPULATE_SHORTCUT: {
                    eggThemAll.copulate(responseObject);
                    break;
                }
                case EggConstants.CMD_DISPLAY_CASTLE_INFO_SHORTCUT:
                case EggConstants.CMD_DISPLAY_CASTLE_INFO: {
                    eggThemAll.displayCastleInfo(responseObject, attacker);
                    break;
                }
                case EggConstants.CMD_GENERATE_RANDOM_ENCOUNTER:
                case EggConstants.CMD_GENERATE_RANDOM_ENCOUNTER_SHORTCUT: {
                    eggThemAll.generateRandomEncounter(responseObject);
                    break;
                }
                case EggConstants.CMD_ENCOUNTER_ATTACK:
                case EggConstants.CMD_ENCOUNTER_ATTACK_SHORTCUT: {
                    eggThemAll.fightEncounter(responseObject, attacker);
                    break;
                }
                case EggConstants.CMD_ENCOUNTER_FLEE:
                case EggConstants.CMD_ENCOUNTER_FLEE_SHORTCUT: {
                    eggThemAll.fleeEncounter(responseObject, attacker);
                    break;
                }
                case EggConstants.CMD_DISPLAY_EGGBOARD: {
                    responseObject.addMessage(EggMessages.UNDER_CONSTRUCTION);
                    break;
                }
                case EggConstants.CMD_THANOS: {
                    eggThemAll.eatCake(responseObject);
                    break;
                }
            }
        }
    }
}