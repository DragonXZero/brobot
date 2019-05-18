package brobot.eggthemall;

public class EggConstants {
    /* Egg related commands */
    public static final String CMD_HELP = "help";
    public static final String CMD_HELP_SHORTCUT = "h";

    public static final String CMD_OVULATE = "ovulate";
    public static final String CMD_OVULATE_SHORTCUT = "o";
    public static final String CMD_ENTER_GAME = "give";
    public static final String CMD_ENTER_GAME_SHORTCUT = "g";
    public static final String CMD_STEAL_EGGS = "steal";
    public static final String CMD_STEAL_EGGS_SHORTCUT = "s";
    public static final String CMD_FERTILIZE_EGGS = "fertilize";
    public static final String CMD_FERTILIZE_EGGS_SHORTCUT = "fe";
    public static final String CMD_COPULATE = "copulate";
    public static final String CMD_COPULATE_SHORTCUT = "x";
    public static final String CMD_THANOS = "cake";

    /* Kid related commands */
    public static final String CMD_GIVE_KIDS = "abandon";
    public static final String CMD_GIVE_KIDS_SHORTCUT = "n";

    /* Castle related commands */
    public static final String CMD_DISPLAY_CASTLE_INFO = "castle";
    public static final String CMD_DISPLAY_CASTLE_INFO_SHORTCUT = "c";

    public static final String CMD_ATTACK = "attack";
    public static final String CMD_ATTACK_SHORTCUT = "a";


    /* Resource related commands */
    public static final String CMD_DISPLAY_RESOURCE_COUNT = "count";
    public static final String CMD_DISPLAY_EGGBOARD = "eggboard";

    /* Encounter related constants */
    public static final String CMD_GENERATE_RANDOM_ENCOUNTER = "explore";
    public static final String CMD_GENERATE_RANDOM_ENCOUNTER_SHORTCUT = "e";
    public static final String CMD_ENCOUNTER_ATTACK = "fight";
    public static final String CMD_ENCOUNTER_ATTACK_SHORTCUT = "f";
    public static final String CMD_ENCOUNTER_FLEE = "bitch";
    public static final String CMD_ENCOUNTER_FLEE_SHORTCUT = "r";

    /* Misc commands TODO - Might want to pull these out and put them into BrobotConstants */
    public static final String CMD_TICKLE = "tickle";

    /* Resource constants */
    public static final String RESOURCE_EGGS = "eggs";
    public static final String RESOURCE_KIDS = "kids";

    /* Egg timer constants */
    public static int EGG_TIMER_UPDATE_FREQUENCY = 1000; // default to 10 seconds
    public static int EGG_TIMER_BLESSING_INCREMENT = 1; // 10 eggs per blessing

    /* Hatchery constants */
    public static int HATCHERY_BASIC_EGG_ROLL_RESULT_MAX = 999;
    public static int HATCHERY_BASIC_EGG_ROLL_COMMON_THRESHOLD = 699;
    public static int HATCHERY_BASIC_EGG_ROLL_UNCOMMON_THRESHOLD = 949;
    public static int HATCHERY_BASIC_EGG_ROLL_RARE_THRESHOLD = 998;
    public static int HATCHERY_BASIC_EGG_ROLL_LEGENDARY_THRESHOLD = 999;

    /* Combat related constants */
    public static double ATTACK_LOSS_MULTIPLIER_DRAW = .1;
    public static double ATTACK_LOSS_MULTIPLIER_WINNER = .05;
    public static double ATTACK_LOSS_MULTIPLIER_WINNER_OVERWHELMING = .02;
    public static double ATTACK_LOSS_MULTIPLIER_LOSER = .2;
    public static double ATTACK_LOSS_MULTIPLIER_LOSER_OVERWHELMING = .5;

    /* Misc values */
    public static final Long EGG_OVULATION_AMOUNT = 100l;
    public static final int EGG_STEAL_MIN = 2;
    public static final int EGG_STEAL_MAX = 20;
}
