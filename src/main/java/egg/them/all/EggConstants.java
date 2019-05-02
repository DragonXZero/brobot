package egg.them.all;

public class EggConstants {
    public static final int EGG_OVULATION_COUNT = 100;
    public static final int EGG_STEAL_MIN = 2;
    public static final int EGG_STEAL_MAX = 20;

    /* commands */
    public static final String OVULATE_CMD = "ovulate";
    public static final String GIVE_EGGS_CMD = "give eggs";

    /* egg timer constants */
    public static int EGG_TIMER_UPDATE_FREQUENCY = 1000; // default to 10 seconds
    public static int EGG_TIMER_BLESSING_INCREMENT = 1; // 10 eggs per blessing
}
