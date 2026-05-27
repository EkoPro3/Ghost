package akro.ghost.tele.features;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.ClientChecker;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;

public class DisableStories {

    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;

                if (ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER), "MessagesController", new String[]{"storiesEnabled", "storyEntitiesAllowed",}, new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.disableStories.isEnable()) param.setResult(false);
                        }
                    });

                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER), AutomationResolver.resolve("MessagesController", "storyEntitiesAllowed2", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("storyEntitiesAllowed", new Class[]{ClassLoad.getClass(ClassNames.TLRPC_USER)}), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.disableStories.isEnable()) param.setResult(false);
                        }
                    }));
                }

                if (ClassLoad.getClass(ClassNames.STORIES_CONTROLLER) != null) {
                    if (ClientChecker.check(ClientChecker.ClientType.NagramX)) {
                        HMethod.hookMethod(ClassLoad.getClass(ClassNames.STORIES_CONTROLLER), AutomationResolver.resolve("StoriesController", "hasStories2", AutomationResolver.ResolverType.Method), long.class, new AbstractMethodHook() {
                            @Override
                            protected void beforeMethod(MethodHookParam param) {
                                if (ConfigManager.disableStories.isEnable())
                                    param.setResult(false);
                            }
                        });
                    } else {
                        HMethod.hookMethod(ClassLoad.getClass(ClassNames.STORIES_CONTROLLER), AutomationResolver.resolve("StoriesController", "hasStories", AutomationResolver.ResolverType.Method), new AbstractMethodHook() {
                            @Override
                            protected void beforeMethod(MethodHookParam param) {
                                if (ConfigManager.disableStories.isEnable())
                                    param.setResult(false);
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
