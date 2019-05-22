package brobot.markov;

import brobot.CommandExecutor;
import brobot.RequestObject;
import brobot.ResponseObject;

public class MarkovCommandExecutorImpl implements CommandExecutor {
    private final Markov markov;

    public MarkovCommandExecutorImpl() {
        this.markov = new Markov();
    }

    public void executeCommand(final ResponseObject responseObject, final RequestObject requestObject) {

    }
}
