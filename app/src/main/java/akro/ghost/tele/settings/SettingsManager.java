package akro.ghost.tele.settings;

import akro.ghost.tele.utils.Utils;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.settings.controller.SettingsController;
import akro.ghost.tele.settings.hook.SettingsHook;
import akro.ghost.tele.settings.ui.SettingsActivity;

import de.robv.android.xposed.XposedHelpers;

public class SettingsManager {

    public static void init(SettingsController settingsController) {

        SettingsActivity.init(settingsController);

        Class<?> SettingsActivityClass = XposedHelpers.findClassIfExists(
                AutomationResolver.resolve("org.telegram.ui.SettingsActivity"),
                Utils.classLoader
        );

        Class<?> SettingsActivity$SettingCell$FactoryClass = XposedHelpers.findClassIfExists(
                AutomationResolver.resolve("org.telegram.ui.SettingsActivity$SettingCell$Factory"),
                Utils.classLoader
        );

        SettingsHook settings = new SettingsHook();
        if (SettingsActivityClass != null && SettingsActivity$SettingCell$FactoryClass != null) {
            settings.newSettings(SettingsActivityClass, SettingsActivity$SettingCell$FactoryClass, settingsController);
        } else {
            settings.oldSettings(settingsController);
        }
    }

}
