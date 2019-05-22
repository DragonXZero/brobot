package brobot.mudae;

import brobot.ResponseObject;
import brobot.Utils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Mudae {
    private final List<MudaeRoll> rolls;

    public Mudae() {
        this.rolls = new LinkedList<>();
    }

    public void displayActiveRolls(final ResponseObject responseObject, final Message message) {
        // expire rolls created more than 30 seconds ago
        final int minute = message.getCreationTime().getMinute();
        final int second = message.getCreationTime().getSecond();
        for (final Iterator<MudaeRoll> itr = rolls.iterator(); itr.hasNext(); ) {
            if(itr.next().isExpired(minute, second)) {
                itr.remove();
            }
        }

        // get active rolls
        if (rolls.size() == 0) {
            responseObject.addMessage(MudaeMessages.NO_ACTIVE_ROLLS);
        } else {
            for (int i = rolls.size() - 1; i >= 0; --i) {
                responseObject.addMessage(rolls.get(i).toString());
            }
        }
    }

    public void addRoll(final ResponseObject responseObject, final Message message) {
        final List<MessageEmbed> embeddedMessages = message.getEmbeds();
        if (embeddedMessages != null && !embeddedMessages.isEmpty()) {
            final MessageEmbed embeddedMessage = embeddedMessages.get(0);
            final String name = embeddedMessages.get(0).getAuthor().getName();
            final String show = embeddedMessages.get(0).getDescription();
            final int minute = message.getCreationTime().getMinute();
            final int second = message.getCreationTime().getSecond();
            final String link = "https://discordapp.com/channels/562115015776403458/569428458430660619/" + message.getIdLong();
            if (!Utils.isNullOrEmpty(name) && !Utils.isNullOrEmpty(show) && show.length() < 200) {
                rolls.add(new MudaeRoll(name, show, minute, second, link));
                if (embeddedMessage.getFooter() == null) {
                    responseObject.addMessage("**" + name + "** / " + show + "\n");
                }
            }
        }
    }
}
