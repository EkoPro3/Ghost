package akro.ghost.tele.features;

import android.view.View;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.virtuals.ui.ChatActivity;

public class HidePinnedMessages {
    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;
                if (ClassLoad.getClass(ClassNames.CHAT_ACTIVITY) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.CHAT_ACTIVITY),
                            AutomationResolver.resolve("ChatActivity", "createPinnedMessageView", AutomationResolver.ResolverType.Method), new AbstractMethodHook() {
                                @Override
                                protected void afterMethod(MethodHookParam param) {
                                    if (ConfigManager.hidePinnedMessages.isEnable()) {
                                        View button = new ChatActivity(param.thisObject).getPinnedMessageView();
                                        if (button != null && button.getVisibility() != View.GONE)
                                            button.setVisibility(View.GONE);
                                    }
                                }
                            });
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.CHAT_ACTIVITY), AutomationResolver.resolve("ChatActivity", "updatePinnedMessageView", AutomationResolver.ResolverType.Method), boolean.class, int.class, new AbstractMethodHook() {
                        @Override
                        protected void afterMethod(MethodHookParam param) {
                            if (ConfigManager.hidePinnedMessages.isEnable()) {
                                View button = new ChatActivity(param.thisObject).getPinnedMessageView();
                                if (button != null && button.getVisibility() != View.GONE)
                                    button.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        } catch (Throwable e){
            Logger.e(e);
        }
    }

}
