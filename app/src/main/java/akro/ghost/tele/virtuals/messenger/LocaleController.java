package akro.ghost.tele.virtuals.messenger;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;

import java.util.Locale;

import de.robv.android.xposed.XposedHelpers;

public class LocaleController {

    Object localeController;

    public LocaleController(){
        localeController = XposedHelpers.callStaticMethod(ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER), AutomationResolver.resolve("LocaleController", "getInstance", AutomationResolver.ResolverType.Method));
    }

    public Locale getCurrentLocale() {
        return (Locale) XposedHelpers.getObjectField(localeController, AutomationResolver.resolve("LocaleController", "currentLocale", AutomationResolver.ResolverType.Field));
    }

    public static boolean isRTL() {
        return (boolean) XposedHelpers.getStaticBooleanField(ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER), AutomationResolver.resolve("LocaleController", "isRTL", AutomationResolver.ResolverType.Field));
    }

}
