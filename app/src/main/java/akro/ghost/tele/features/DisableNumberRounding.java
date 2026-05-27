package akro.ghost.tele.features;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;

public class DisableNumberRounding {

    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;

                if (ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER), AutomationResolver.resolve("LocaleController", "formatShortNumber", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("formatShortNumber", new Class[]{int.class, int[].class}),
                            new AbstractMethodHook() {
                                @Override
                                protected void beforeMethod(MethodHookParam param) {
                                    if (ConfigManager.disableNumberRounding.isEnable()) {
                                        int[] rounded = (int[]) param.args[1];
                                        int number = (int) param.args[0];
                                        if (rounded != null) {
                                            rounded[0] = number;
                                        }
                                        param.setResult(String.valueOf(number));
                                    }
                                }
                            }));
                }
            }
        } catch (Throwable t) {
            Logger.e(t);
        }
    }
}
