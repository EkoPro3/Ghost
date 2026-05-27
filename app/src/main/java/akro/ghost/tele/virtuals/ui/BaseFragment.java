package akro.ghost.tele.virtuals.ui;

import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.messenger.UserConfig;

import de.robv.android.xposed.XposedHelpers;

public class BaseFragment {

    Object baseFragment;

    public BaseFragment(Object obj){
        baseFragment = obj;
    }

    public UserConfig getUserConfig(){
        return new UserConfig(XposedHelpers.callMethod(baseFragment, AutomationResolver.resolve("BaseFragment", "getUserConfig", AutomationResolver.ResolverType.Method)));
    }

}
