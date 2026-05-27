package akro.ghost.tele.virtuals.messenger;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.tgnet.TLRPC;

import de.robv.android.xposed.XposedHelpers;

public class UserConfig {

    Object userConfig;

    public UserConfig(Object obl){
        userConfig = obl;
    }

    public static int getSelectedAccount() {
        String selectedAccountField = AutomationResolver.resolve("UserConfig", "selectedAccount", AutomationResolver.ResolverType.Field);
        return XposedHelpers.getStaticIntField(ClassLoad.getClass(ClassNames.USER_CONFIG), selectedAccountField);
    }

    public long getClientUserId(){
        return XposedHelpers.getLongField(userConfig, AutomationResolver.resolve("UserConfig" , "clientUserId", AutomationResolver.ResolverType.Field));
    }

    public TLRPC.User getCurrentUser(){
        return new TLRPC.User(XposedHelpers.callMethod(userConfig, AutomationResolver.resolve("UserConfig" , "getCurrentUser", AutomationResolver.ResolverType.Method)));
    }

}
