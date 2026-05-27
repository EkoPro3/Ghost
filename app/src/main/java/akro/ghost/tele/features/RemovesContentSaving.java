package akro.ghost.tele.features;


import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.logging.Logger;

public class RemovesContentSaving {

    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;

                if (ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER), AutomationResolver.resolve("MessagesController", "isChatNoForwards", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("isChatNoForwards", new Class[]{ClassLoad.getClass(ClassNames.TLRPC_CHAT)}), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.removesContentSaving.isEnable())
                                param.setResult(false);
                        }
                    }));
                }

                if (ClassLoad.getClass(ClassNames.CHAT_ACTIVITY) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.CHAT_ACTIVITY), AutomationResolver.resolve("ChatActivity", "hasSelectedNoforwardsMessage", AutomationResolver.ResolverType.Method), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.removesContentSaving.isEnable())
                                param.setResult(false);
                        }
                    });
                }

                if (ClassLoad.getClass(ClassNames.MESSAGE_OBJECT) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.MESSAGE_OBJECT), AutomationResolver.resolve("MessageObject", "canForwardMessage", AutomationResolver.ResolverType.Method), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.removesContentSaving.isEnable())
                                param.setResult(true);
                        }
                    });
                }
            }
        } catch (Throwable t) {
            Logger.e(t);
        }
    }

}
