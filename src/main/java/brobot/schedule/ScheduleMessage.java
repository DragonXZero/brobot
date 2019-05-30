package brobot.schedule;

import net.dv8tion.jda.core.entities.User;

import java.util.Date;
import java.util.List;

public class ScheduleMessage {
    String msg;
    Date initDate;
    long delay;
    List<User> userList; // for notifications

    public ScheduleMessage() {
        this.msg = "";
        this.initDate = null;
        this.delay = 0;
    }

    public ScheduleMessage(String msg, Date initDate, long delay) {
        this.msg = msg;
        this.initDate = initDate;
        this.delay = delay;
    }
}
