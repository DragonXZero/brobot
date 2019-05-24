package brobot.mudae;

import brobot.ResponseObject;
import brobot.Utils;
import brobot.schedule.ScheduleMessageManager;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.*;

public class Mudae {
    private final Map<String, ScheduleMessageManager> scheduleMsgChannels;
    private final List<MudaeRoll> rolls;


    public Mudae() {
        this.scheduleMsgChannels = new HashMap<>();
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

    public void scheduleMessages(final ResponseObject responseObject, final MessageChannel channel) {
        // Set up scheduled messages
        final String channelId = channel.getId();
        // Make sure not to create duplicate messages for a channel
        if (!scheduleMsgChannels.containsKey(channelId)) {
            ScheduleMessageManager smm = new ScheduleMessageManager(channel);
            smm.init();
            scheduleMsgChannels.put(channelId, smm);
            responseObject.addMessage(MudaeMessages.SEND_MESSAGE_ON);
        }
        else {
            ScheduleMessageManager smm = scheduleMsgChannels.remove(channelId);
            smm.cancelAll();
            responseObject.addMessage(MudaeMessages.SEND_MESSAGE_OFF);
        }

    }
}
