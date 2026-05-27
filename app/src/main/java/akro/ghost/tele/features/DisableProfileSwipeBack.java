package akro.ghost.tele.features;

import android.view.MotionEvent;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;

public class DisableProfileSwipeBack {
    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;

                if (ClassLoad.getClass(ClassNames.CHAT_ACTIVITY) != null) {

                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.CHAT_ACTIVITY), AutomationResolver.resolve("ProfileActivity", "isSwipeBackEnabled", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("isSwipeBackEnabled", new Class[]{MotionEvent.class}), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.disableProfileSwipeBack.isEnable()) {
                                param.setResult(false);
                            }
                        }
                    }));
                }
            }
        } catch (Throwable e){
            Logger.e(e);
        }
    }

}
