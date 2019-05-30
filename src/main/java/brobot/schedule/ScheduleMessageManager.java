package brobot.schedule;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.*;

public class ScheduleMessageManager {
    private final long DELAY_24_HOUR = 1000L * 60 * 60 * 24;   // milliseconds * seconds * minutes * hours;
    private final long DELAY_1_HOUR = 1000L * 60 * 60 * 1;   // milliseconds * seconds * minutes * hours;
    private final long DELAY_2_HOUR = 1000L * 60 * 60 * 2;   // milliseconds * seconds * minutes * hours;
    private final long DELAY_1_MIN = 1000L * 60 * 1 * 1;   // milliseconds * seconds * minutes * hours;

    private List<Timer> timerList;   // Add/remove timers
    private MessageChannel channel;

    public ScheduleMessageManager(MessageChannel channel) {
        this.channel = channel;
        this.timerList = new ArrayList<>();
    }

    public void init() {
        // Daylight savings might mess up the times
        scheduleMudaeMsg();
        schedulePkmnMsg();
        scheduleKCMsg();
        scheduleFGOMsg();
        scheduleGFLMsg();
    }

    public void schedule(ScheduleMessage se) {
        // sanity check
        if (se == null || se.msg == null || se.msg.length() < 1 || se.initDate == null || se.delay < 1) {
            return;
        }
        Timer t = new Timer();
        ScheduleMessageTask sm = new ScheduleMessageTask(channel, se.msg);
        t.scheduleAtFixedRate(sm, se.initDate, se.delay);
        timerList.add(t);

        System.out.println("Added scheduled message (" + se.msg + ") at " + se.initDate.toString() + " with delay of " + (se.delay / 1000) + " secs");
    }

    public void cancelAll() {
        for (Timer t : timerList) {
            t.cancel();
        }
    }

    public void scheduleMudaeMsg() {
        Date nextScheduledTime;
        String rollMsg = "Mudae rolls have been refreshed!";
        String claimMsg = "New claim period is available!";

        // Rolls
        nextScheduledTime = getNextHour();
        schedule(new ScheduleMessage(rollMsg, nextScheduledTime, DELAY_1_HOUR));

        // Claims
        nextScheduledTime = getNextEvenHour();
        schedule(new ScheduleMessage(claimMsg, nextScheduledTime, DELAY_2_HOUR));
    }

    public void schedulePkmnMsg() {
        Date nextScheduledTime = getNextEvenHour();
        String msg = "Pokéslot has been refreshed!";

        // Pokéslot
        schedule(new ScheduleMessage(msg, nextScheduledTime, DELAY_2_HOUR));
    }

    public void scheduleKCMsg() {
        Date nextScheduledTime;
        String resetMsg = "KanColle PvP has been reset!";
        String warning1HrMsg = "KanColle PvP will reset in 1 hour!";

        // PVP reset: 11 AM Pacific
        nextScheduledTime = getNextSpecificHour(11);
        schedule(new ScheduleMessage(resetMsg, nextScheduledTime, DELAY_24_HOUR));

        // PVP reset: 11 PM Pacific
        nextScheduledTime = getNextSpecificHour(23);
        schedule(new ScheduleMessage(resetMsg, nextScheduledTime, DELAY_24_HOUR));

        // PVP warning: 10 AM Pacific
        nextScheduledTime = getNextSpecificHour(10);
        schedule(new ScheduleMessage(warning1HrMsg, nextScheduledTime, DELAY_24_HOUR));

        // PVP reset: 10 PM Pacific
        nextScheduledTime = getNextSpecificHour(22);
        schedule(new ScheduleMessage(warning1HrMsg, nextScheduledTime, DELAY_24_HOUR));
    }

    public void scheduleFGOMsg() {
        Date nextScheduledTime;
        String loginResetMsg = "FGO daily login has reset!";
        String questResetMsg = "FGO daily quests have been reset!";

        // Daily login reset: 5 PM Pacific
        nextScheduledTime = getNextSpecificHour(17);
        schedule(new ScheduleMessage(loginResetMsg, nextScheduledTime, DELAY_24_HOUR));

        // Daily quest reset: 9 PM Pacific
        nextScheduledTime = getNextSpecificHour(21);
        schedule(new ScheduleMessage(questResetMsg, nextScheduledTime, DELAY_24_HOUR));
    }

    public void scheduleGFLMsg() {
        Date nextScheduledTime;
        String loginResetMsg = "GFL daily login has reset!";
        String dormBatteryMsg = "GFL dorm batteries are available!";
        String friendBatteryMsg = "GFL friend batteries are available!";

        // Daily login reset: 1 AM Pacific
        nextScheduledTime = getNextSpecificHour(1);
        schedule(new ScheduleMessage(loginResetMsg, nextScheduledTime, DELAY_24_HOUR));

        // Dorm battery available: 12 PM Pacific
        nextScheduledTime = getNextSpecificHour(12);
        schedule(new ScheduleMessage(dormBatteryMsg, nextScheduledTime, DELAY_24_HOUR));

        // Dorm battery available: 6 PM Pacific
        nextScheduledTime = getNextSpecificHour(18);
        schedule(new ScheduleMessage(dormBatteryMsg, nextScheduledTime, DELAY_24_HOUR));

        // Friend battery available: 4 AM Pacific
        nextScheduledTime = getNextSpecificHour(4);
        schedule(new ScheduleMessage(friendBatteryMsg, nextScheduledTime, DELAY_24_HOUR));

        // Friend battery available: 4 PM Pacific
        nextScheduledTime = getNextSpecificHour(16);
        schedule(new ScheduleMessage(friendBatteryMsg, nextScheduledTime, DELAY_24_HOUR));
    }

    private Date getNextHour() {
        Date dt = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dt.getTime());

        cal.add(Calendar.HOUR, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    private Date getNextEvenHour() {
        Date dt = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dt.getTime());
        int currHour = cal.get(Calendar.HOUR);

        cal.add(Calendar.HOUR, (currHour % 2 == 0) ? 2 : 1);   // add 2 if even hour, else add 1 for odd
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    private Date getNextSpecificHour(int hour) {
        if (hour < 0 || hour >= 24) {    // using 24 hour format
            return null;
        }

        Date dt = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dt.getTime());
        int currHour = cal.get(Calendar.HOUR_OF_DAY);   // HOUR_OF_DAY - 24 hr format

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // set to next day if the specified hour has passed
        if (currHour >= hour) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return cal.getTime();
    }

    private Date getNextMinute() {
        Date dt = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dt.getTime());

        cal.add(Calendar.MINUTE, 1);

        return cal.getTime();
    }

    public void scheduleTest() {
        Date nextScheduledTime = getNextMinute();
        String msg = "Test message";

        // Test messages scheduled every minute
        schedule(new ScheduleMessage(msg, nextScheduledTime, DELAY_1_MIN));
    }
}