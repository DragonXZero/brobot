package brobot.mudae;

import brobot.CommandExecutor;
import brobot.RequestObject;
import brobot.ResponseObject;

public class MudaeCommandExecutorImpl implements CommandExecutor {
    private final Mudae mudae;

    public MudaeCommandExecutorImpl() {
        this.mudae = new Mudae();
    }

    @Override
    public void executeCommand(final ResponseObject responseObject, final RequestObject requestObject) {
        final String command = requestObject.getCommand();

        switch (command) {
            case MudaeConstants.CMD_ACTIVE_ROLLS:
            case MudaeConstants.CMD_ACTIVE_ROLLS_SHORTCUT: {
                mudae.displayActiveRolls(responseObject, requestObject.getDiscordMessage());
                break;
            }
            case MudaeConstants.CMD_SPECIAL: {
                mudae.addRoll(responseObject, requestObject.getDiscordMessage());
                break;
            }
            default: {
                responseObject.addMessage(MudaeMessages.INVALID_COMMAND);
            }
        }
    }
}
