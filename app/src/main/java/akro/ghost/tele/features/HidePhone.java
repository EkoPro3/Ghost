package akro.ghost.tele.features;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.virtuals.messenger.UserConfig;
import akro.ghost.tele.virtuals.tgnet.TLRPC;

public class HidePhone {

    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;

                if (ClassLoad.getClass(ClassNames.USER_CONFIG) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.USER_CONFIG), AutomationResolver.resolve("UserConfig", "getClientUserId", AutomationResolver.ResolverType.Method), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.hidePhone.isEnable()) {
                                UserConfig userConfig = new UserConfig(param.thisObject);
                                if (userConfig.getCurrentUser().getUser() != null) {
                                    TLRPC.User user = userConfig.getCurrentUser();
                                    if (user.getPhone() != null) {
                                        user.setPhone(null);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        } catch (Throwable t){
            Logger.e(t);
        }
    }

}
