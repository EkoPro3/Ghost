package akro.ghost.tele.virtuals.messenger;

import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class BaseController {

    Object baseController;

    public BaseController(Object obj){baseController = obj;}

    public UserConfig getUserConfig(){
        return new UserConfig(XposedHelpers.callMethod(baseController, AutomationResolver.resolve("BaseController", "getUserConfig", AutomationResolver.ResolverType.Method)));
    }

}
