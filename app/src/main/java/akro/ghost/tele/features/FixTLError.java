package akro.ghost.tele.features;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.messenger.NotificationCenter;

public class FixTLError {

    public static boolean isEnable = false;

    public static void init(){
        try {
            if (!isEnable) {
                isEnable = true;
                if (ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY), AutomationResolver.resolve("LaunchActivity", "didReceivedNotification", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("didReceivedNotification", new Class[]{int.class, int.class, Object[].class}), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            int id = (int) param.args[0];
                            if (id == NotificationCenter.getTlSchemeParseException() && ConfigManager.fixTLError.isEnable())
                                param.setResult(null);
                        }
                    }));
                }
            }
        } catch (Throwable t){
            Logger.e(t);
    }
    }

}
