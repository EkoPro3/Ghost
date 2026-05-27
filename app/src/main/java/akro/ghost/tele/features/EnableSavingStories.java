package akro.ghost.tele.features;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;

public class EnableSavingStories {

    public static boolean isEnable = false;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;

                if (ClassLoad.getClass(ClassNames.STORY_ITEM_HOLDER) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.STORY_ITEM_HOLDER), AutomationResolver.resolve("PeerStoriesView$StoryItemHolder", "allowScreenshots", AutomationResolver.ResolverType.Method),
                            new AbstractMethodHook() {
                                @Override
                                protected void beforeMethod(MethodHookParam param) {
                                    if (ConfigManager.enableSavingStories.isEnable())
                                        param.setResult(true);
                                }
                            });
                }
            }
        } catch (Throwable t){
            Logger.e(t);
        }
    }
}
