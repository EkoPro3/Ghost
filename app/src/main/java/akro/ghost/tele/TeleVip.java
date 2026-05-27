package akro.ghost.tele;

import static akro.ghost.tele.obfuscate.AutomationResolver.resolverRegistry;

import android.content.Context;

import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.application.AndroidUtilities;
import akro.ghost.tele.dex.DexInjector;
import akro.ghost.tele.language.Translator;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.settings.SettingsManager;
import akro.ghost.tele.settings.controller.SettingsController;
import akro.ghost.tele.utils.Utils;
import akro.ghost.tele.virtuals.TeleVip.Bridge.Bridge;

public class TeleVip {
    
    public static void startHook(Context context) {
        try {
            resolverRegistry.loadParameter();
            Translator.init();
            AndroidUtilities.init(context);
            DexInjector.injectDex(Utils.classLoader);

            SettingsController settingsController = new SettingsController(context);

            Bridge.init(settingsController);
            ConfigManager.loadAndRead(context);
            SettingsManager.init(settingsController);

        } catch (Throwable e){
            Logger.e(e);
        }

    }

}
