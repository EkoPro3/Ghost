package akro.ghost.tele.application;

import android.content.Context;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class ApplicationLoaderHook {

    private static Context applicationContext;

    public static Context getApplicationContext() {
        if (applicationContext == null) {
            applicationContext = (Context) XposedHelpers.getStaticObjectField(
                    ClassLoad.getClass(ClassNames.APPLICATION_LOADER),
                    AutomationResolver.resolve("ApplicationLoader", "applicationContext", AutomationResolver.ResolverType.Field)
            );
        }
        return applicationContext;
    }
}