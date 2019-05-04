package egg.them.all.eggs;

import egg.them.all.kids.*;

//This is currently an example egg class
public class BlueEgg extends Egg
{
    @Override
    public Kid hatch()
    {
        return new CoolKid();
    }
}