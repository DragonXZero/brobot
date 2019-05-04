package brobot.eggthemall.eggs;

import brobot.eggthemall.kids.*;

//This is currently an example egg class
public class DarkEgg extends Egg
{
    @Override
    public Kid hatch()
    {
        return new EmoKid();
    }
}