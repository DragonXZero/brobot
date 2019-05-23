package brobot.markov;

import brobot.CommandExecutor;
import brobot.RequestObject;
import brobot.ResponseObject;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class MarkovCommandExecutorImpl implements CommandExecutor {
    private final Markov markov;

    public MarkovCommandExecutorImpl() {
        this.markov = new Markov();
    }

    public void executeCommand(final ResponseObject responseObject, final RequestObject requestObject) {
        //final User author = requestObject.getAuthor();
        final String command = requestObject.getCommand();
        final long commandVal = requestObject.getCommandVal();
        final List<Member> members = requestObject.getMentionedUsers();
        final User user = (members != null && members.size() == 1) ? members.get(0).getUser() : requestObject.getAuthor();

        switch (command) {
            case MarkovConstants.CMD_N_MOST_FREQ:
            case MarkovConstants.CMD_N_MOST_FREQ_SHORTCUT: {
                markov.getNMostFrequentlyUsedWord(responseObject, user, commandVal);
                break;
            }
            case MarkovConstants.CMD_MOST_FREQ_SHORTCUT:
            default: {
                markov.getMostFrequentlyUsedWord(responseObject, user);
                break;
            }
        }
    }
}
