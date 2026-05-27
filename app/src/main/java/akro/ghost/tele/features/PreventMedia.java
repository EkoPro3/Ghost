package akro.ghost.tele.features;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.virtuals.ui.SecretMediaViewer;

import de.robv.android.xposed.XposedHelpers;

public class PreventMedia {

    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;

                if (ClassLoad.getClass(ClassNames.CHAT_ACTIVITY) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.CHAT_ACTIVITY), AutomationResolver.resolve("ChatActivity", "sendSecretMessageRead", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("sendSecretMessageRead", new Class[]{ClassLoad.getClass(ClassNames.MESSAGE_OBJECT), boolean.class}), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.preventMedia.isEnable()) param.setResult(null);
                        }
                    }));
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.CHAT_ACTIVITY), AutomationResolver.resolve("ChatActivity", "sendSecretMediaDelete", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("sendSecretMediaDelete", new Class[]{ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)}), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.preventMedia.isEnable()) param.setResult(null);
                        }
                    }));
                }

                if (ClassLoad.getClass(ClassNames.SECRET_MEDIA_VIEWER) != null) {
                    SecretMediaViewer.openMedia();
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.SECRET_MEDIA_VIEWER), AutomationResolver.resolve("SecretMediaViewer", "closePhoto", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("closePhoto", new Class[]{boolean.class, boolean.class}), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.preventMedia.isEnable()) {
                                Object thisObject = param.thisObject;
                                XposedHelpers.setObjectField(thisObject, AutomationResolver.resolve("SecretMediaViewer", "onClose", AutomationResolver.ResolverType.Field), null);
                            }
                        }
                    }));
                }
            }
        } catch (Throwable t){
            Logger.e(t);
        }
    }

}
