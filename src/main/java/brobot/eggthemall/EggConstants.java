package brobot.eggthemall;

public class EggConstants {

    /* commands */
    public static final String CMD_OVULATE = "ovulate";
    public static final String CMD_GIVE_EGGS = "give eggs";

    /* resource constants */
    public static final String RESOURCE_EGGS = "eggs";
    public static final String RESOURCE_KIDS = "kids";

    /* egg timer constants */
    public static int EGG_TIMER_UPDATE_FREQUENCY = 1000; // default to 10 seconds
    public static int EGG_TIMER_BLESSING_INCREMENT = 1; // 10 eggs per blessing

    /* HatcheryConstants */
    public static int HATCHERY_BASIC_EGG_ROLL_RESULT_MAX = 999;
    public static int HATCHERY_BASIC_EGG_ROLL_COMMON_THRESHOLD = 699;
    public static int HATCHERY_BASIC_EGG_ROLL_UNCOMMON_THRESHOLD = 949;
    public static int HATCHERY_BASIC_EGG_ROLL_RARE_THRESHOLD = 998;
    public static int HATCHERY_BASIC_EGG_ROLL_LEGENDARY_THRESHOLD = 999;

    /* Misc values */
    public static final Long EGG_OVULATION_AMOUNT = 100l;
    public static final int EGG_STEAL_MIN = 2;
    public static final int EGG_STEAL_MAX = 20;
}
