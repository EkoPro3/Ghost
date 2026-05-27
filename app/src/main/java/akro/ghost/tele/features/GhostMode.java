package akro.ghost.tele.features;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.ClientChecker;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class GhostMode {

    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;
                if (ClassLoad.getClass(ClassNames.CONNECTIONS_MANAGER) != null && ConfigManager.isGhostMode()) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.CONNECTIONS_MANAGER), AutomationResolver.resolve("ConnectionsManager", "sendRequestInternal", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("sendRequestInternal", new Class[]{ClassLoad.getClass(ClassNames.TL_OBJECT), ClassLoad.getClass(ClassNames.REQUEST_DELEGATE), ClassLoad.getClass(ClassNames.REQUEST_DELEGATE_TIMESTAMP), ClassLoad.getClass(ClassNames.QUICK_ACK_DELEGATE), ClassLoad.getClass(ClassNames.WRITE_TO_SOCKET_DELEGATE), int.class, int.class, int.class, boolean.class, int.class}), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            try {
                                if (HideSeen.isReadMessages) {
                                    HideSeen.isReadMessages = false;
                                } else if (ConfigManager.isGhostMode()) {
                                    Object object = param.args[0];
                                    if (ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                                        HideSeen.saveReadHistory(object);
                                    }
                                    if (ConfigManager.hideOnline.isEnable()) {
                                        if (ClassLoad.getClass(ClassNames.TL_ACCOUNT_UPDATE_STATUS) != null) {

                                            if (ClassLoad.getClass(ClassNames.TL_ACCOUNT_UPDATE_STATUS).isInstance(object)) {
                                                XposedHelpers.setBooleanField(object, AutomationResolver.resolve("TL_account$updateStatus", "offline", AutomationResolver.ResolverType.Field), true);
                                            }
                                        }
                                    }

                                    if (ConfigManager.hideSeen.isEnable() && HideSeen.isReadMessageRequest(object)) {
                                        HideSeen.sendFakeReadResponse(param.args[1]);
                                        param.setResult(null);
                                        return;
                                    }

                                    if (ConfigManager.hideTyping.isEnable() && HideTyping.isTypingRequest(object)) {
                                        param.setResult(null);
                                        return;
                                    }

                                    if (ConfigManager.hideStoryView.isEnable() && HideStoryRead.isReadStoriesRequest(object)) {
                                        param.setResult(null);
                                        return;
                                    }

                                    HideSeen.handleReadAfterSend(object);
                                }
                            } catch (Throwable e) {
                                XposedBridge.log(e);
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
