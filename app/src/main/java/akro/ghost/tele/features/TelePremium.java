package akro.ghost.tele.features;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.ClientChecker;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.utils.Utils;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.logging.Logger;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class TelePremium {

    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;

                if (ClassLoad.getClass(ClassNames.USER_CONFIG) != null) {

                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.USER_CONFIG), AutomationResolver.resolve("UserConfig", "isPremium", AutomationResolver.ResolverType.Method), new AbstractMethodHook() {
                        @Override
                        public void beforeMethod(XC_MethodHook.MethodHookParam param) {
                            if (ConfigManager.telegramPremium.isEnable()) param.setResult(true);
                        }
                    });
                }
                if (ClientChecker.check(ClientChecker.ClientType.iMe) || ClientChecker.check(ClientChecker.ClientType.iMeWeb)) {
                    Class<?> ForkPremiumPreferencClass = XposedHelpers.findClassIfExists("com.iMe.storage.data.locale.prefs.impl.ForkPremiumPreference", Utils.classLoader);
                    if (ForkPremiumPreferencClass != null) {
                        HMethod.hookMethod(ForkPremiumPreferencClass, "isPremium", new AbstractMethodHook() {
                            @Override
                            protected void beforeMethod(MethodHookParam param) {
                                if (ConfigManager.telegramPremium.isEnable())
                                    param.setResult(true);
                            }
                        });
                    }
                }
            }
        } catch (Throwable t){
            Logger.e(t);
        }
    }

}
