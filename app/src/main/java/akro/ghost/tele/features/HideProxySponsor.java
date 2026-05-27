package akro.ghost.tele.features;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.application.AndroidUtilities;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.messenger.MessagesController;

public class HideProxySponsor {

    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;
                if (ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER),
                            AutomationResolver.resolve("MessagesController", "checkPromoInfoInternal", AutomationResolver.ResolverType.Method),AutomationResolver.merge(AutomationResolver.resolveObject("checkPromoInfoInternal", new Class[]{boolean.class}), new AbstractMethodHook() {
                                @Override
                                protected void afterMethod(MethodHookParam param) {
                                    if (ConfigManager.hideProxySponsor.isEnable()) {
                                        MessagesController messagesController = new MessagesController(param.thisObject);
                                        AndroidUtilities.runOnUIThread(messagesController::removePromoDialog);
                                        removePromoDialog();
                                    }
                                }
                            }));
                }
            }
        } catch (Throwable e) {
            Logger.e(e);
        }
    }

    public static void removePromoDialog() {
        if (MessagesController.getGlobalMainSettings() == null) return;
        MessagesController.getGlobalMainSettings().edit().remove("proxy_dialog").remove("proxyDialogAddress").remove("nextPromoInfoCheckTime").apply();
    }

}
