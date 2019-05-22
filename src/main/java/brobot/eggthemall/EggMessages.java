package brobot.eggthemall;

public class EggMessages {
    public static final String UNDER_CONSTRUCTION = "This is broken right now D: Please try again later!";
    public static final String INVALID_COMMAND = "What are you trying to do here? o_o??";

    public static final String CASTLE_CREATE_SUCCESS = "Welcome to the game **%s**. You just lost";
    public static final String OVULATE_FAIL = "I already gave you eggs, go away!!! D:<";
    public static final String OVULATE_SUCCESS = "**%s**, you now have 100 eggs.";

    public static final String STEAL_EGGS_FAIL_SELF = "You can't steal from yourself dummy. HEY EVERYONE, **%s** IS A DUM DUM!";
    public static final String STEAL_EGGS_FAIL_NO_EGGS_TO_STEAL = "**%s** has no eggs. :( Nooooo, dōshite?????";
    public static final String STEAL_EGGS_SUCCESS = "**%s**, you stole %d eggs from **%s**. You now have %d eggs and they have %d eggs!";

    public static final String FERTILIZE_EGGS_FAIL_NOT_ENOUGH_EGGS = "You don't have enough eggs :( Go steal some more!";
    public static final String FERTILIZE_EGGS_SUCCESS = "Congratulations **%s**, you made %d kids! You now have %d kids and %d eggs!";

    public static final String ABANDON_KIDS_FAIL_SELF = "**%s**, you can't dump these kids on yourself! They're already your kids!";
    public static final String ABANDON_KIDS_FAIL_NOT_ENOUGH_KIDS_TO_ABANDON = "**%s**, you don't have enough kids to give away. :( Go make more! o:";
    public static final String ABANDON_KIDS_SUCCESS = "Congratulations **%s**, you got rid of %d kids and gave them to **%s**! You now have %d kids and they have %d kids!";

    public static final String COPULATE_FAIL_NO_KIDS = "**%s**, you don't have any kids. o.o So sad, you'll probably die alone too.. lul\n";
    public static final String COPULATE_SUCCESS = "Congratulations **%s**! Some of your kids laid eggs! You now have %d eggs!!! :D\n";

    public static final String THANOS_ENOUGH_EGGS = "**%s**, your kids ate %d of your eggs! Those bastards! D: You now have %d eggs\n";
    public static final String THANOS_NOT_ENOUGH_EGGS = "**%s**, you didn't have enough eggs to feed all of your kids... %d ran away! You now have %d kids and %d eggs. :(\n";

    public static final String RESOURCES_GET_SELF = "**%s**'s resources: %d kids and %d eggs!";

    public static final String CASTLE_HEALTH_INFO = "**%s**, your castle now has %d health!";

    public static final String ATTACK_INTRO = "**%s**, your army clashes with **%s**'s army in an epic battle!\n";
    public static final String ATTACK_DRAW = "Both sides are too evenly matched! You retreat before your army suffers unnecessary casualties!\n";
    public static final String ATTACK_VICTORY = "Your army easily overpowers the opposing force!\n";
    public static final String ATTACK_VICTORY_OVERWHELMING = "Your army absolutely obliterates the enemy! You proceed to burn down their crops and obliterate their people from existence!\n";
    public static final String ATTACK_DEFEAT = "Your army had no chance against the opposing force! Why did you even bother attacking?\n";
    public static final String ATTACK_DEFEAT_OVERWHELMING = "You sent your poor kids into a 300 type scenario! They had no chance... Say hello to your new daddy: **%s**.\n";

    public static final String ATTACK_BATTLE_SUMMARY =
            "\tBattle Summary:"
            + "\n\t\t**%s**'s :baby:'s defeated in battle: %d"
            + "\n\t\t**%s**'s :baby:'s defeated in battle: %d";

    public static final String ENCOUNTER_ALREADY_IN_AN_ENCOUTER = "There's already an ongoing encounter! Bitch out and run away or man up and resolve it!\n";
    public static final String ENCOUNTER_NO_ENCOUNTER = "Dude, there's nothing here to fight... *insert john travolta gif*\n";
    public static final String ENCOUNTER_FIGHT_SUCCESS = "**%s**, you dealt %d damage to %s! %s has %d :heart: left!\n";
    public static final String ENCOUNTER_FIGHT_FAIL_NO_DAMAGE = "**%s**, your attack did no damage. You're so weak that the monster just ignores you... get swole.\n";
    public static final String ENCOUNTER_FIGHT_COUNTER_ATTACK = "**%s**, you pissed off **%s**! It attacks you back for %d damage! Owwies! :<\n";
    public static final String ENCOUNTER_BATTLE_REWARD = "Congratulations **%s**, you've received %d eggs for helping to defeat **%s**.\n";
    public static final String ENCOUNTER_FLEE_SUCCESS = "Damn **%s**, you a bitch tho.\n";

    public static final String HELP_MESSAGE =
            "**steal/s** @user : steal eggs\n" +
            "**fertilize/fe** <val> : make kids from eggs\n" +
            "**copulate/x** : all castles's kids make eggs\n" +
            "**abandon/n** <val> @user :  give your kids away\n" +
            "**castle/c** : display your castle's info\n" +
            "**attack/a** @user : attacks a castle\n" +
            "**explore/e** : generates an encounter, beware\n" +
            "**fight/f** : fights the monster in the encounter\n" +
            "**bitch/r** : ends the current encounter\n" +
            "**cake** :  mini-thanos the server\n" +
            "**m active/m a** : displays all rolls made within the last 30 seconds\n";
}

