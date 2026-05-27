package akro.ghost.tele.features.otherFeatures;

import android.content.Context;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.ClientChecker;
import akro.ghost.tele.utils.Utils;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class FeatureInitializer {

    public static void init(Context context) {
        if (ClientChecker.check(ClientChecker.ClientType.TelegramPlus)) return;

        try {
            if (!FeatureStateManager.isChatEnabled()) {

                Class<?> actionBarClass = XposedHelpers.findClassIfExists(
                        AutomationResolver.resolve("org.telegram.ui.ActionBar.ActionBar"),
                        Utils.classLoader
                );

                HMethod.hookMethod(
                        actionBarClass,
                        AutomationResolver.resolve("ActionBar", "setActionBarMenuOnItemClick", AutomationResolver.ResolverType.Method), ClassLoad.getClass(ClassNames.ACTION_BAR_MENU_ON_ITEM_CLICK),
                        new AbstractMethodHook() {
                            @Override
                            protected void beforeMethod(MethodHookParam param) {

                                Object clazz = param.args[0];

                                if (clazz == null) return;

                                String name = clazz.getClass().getName();

                                if (name.contains("ChatActivity") && !FeatureStateManager.isChatEnabled()) {
                                    FeatureStateManager.saveChat(name);
                                    ChatHook.init(context, name);
                                }
                            }
                        });

            } else {
                ChatHook.init(context, FeatureStateManager.getChatClass());
            }

        } catch (Throwable t) {
            Logger.e(t);
        }
    }
}