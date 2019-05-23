package brobot.schedule;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.TimerTask;

public class ScheduleMessageTask extends TimerTask {
    private String message;
    private MessageChannel channel;

    public ScheduleMessageTask(){
        this.message = "";
        this.channel = null;
    }

    public ScheduleMessageTask(MessageChannel channel, String message){
        this.channel = channel;
        this.message = message;
    }

    @Override
    public void run() {
        if (channel != null) {
            channel.sendMessage(message).queue();
        }
    }
}
