package brobot.eggthemall.egg;

import brobot.eggthemall.kid.*;

//This is currently an example egg class
public class DarkEgg extends Egg
{
    @Override
    public Kid hatch()
    {
        return new EmoKid();
    }
}