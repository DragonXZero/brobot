package egg.them.all.eggs;

import egg.them.all.children.*;

//This is currently an example egg class
public class BlueEgg extends Egg
{

    @Override
    public Child hatch() {
        return new CoolChild();
    }
}
