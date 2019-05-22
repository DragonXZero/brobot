package brobot.mudae;

import brobot.CommandExecutor;
import brobot.RequestObject;
import brobot.ResponseObject;
import brobot.eggthemall.EggMessages;

public class MudaeCommandExecutorImpl implements CommandExecutor {
    private final Mudae mudae;

    public MudaeCommandExecutorImpl() {
        this.mudae = new Mudae();
    }

    @Override
    public void executeCommand(final RequestObject requestObject, final ResponseObject responseObject) {
        final String command = requestObject.getCommand();

        switch (command) {
            case MudaeConstants.CMD_ACTIVE_ROLLS:
            case MudaeConstants.CMD_ACTIVE_ROLLS_SHORTCUT: {
                mudae.displayActiveRolls(responseObject);
                break;
            }
            default: {
                responseObject.addMessage(EggMessages.INVALID_COMMAND);
            }
        }
    }
}
