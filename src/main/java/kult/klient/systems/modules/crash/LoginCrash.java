package kult.klient.systems.modules.crash;

import kult.klient.systems.modules.Categories;
import kult.klient.systems.modules.Module;
import net.minecraft.item.Items;

/*/--------------------------/*/
/*/ Made by 0x150            /*/
/*/ https://github.com/0x150 /*/
/*/--------------------------/*/

public class LoginCrash extends Module {
    public LoginCrash() {
        super(Categories.Crash, Items.COMMAND_BLOCK, "login-crash", "Tries to crash the server on login using null packets. (By 0x150)");
    }
}
