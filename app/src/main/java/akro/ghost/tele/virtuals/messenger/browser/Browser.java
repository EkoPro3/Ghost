package akro.ghost.tele.virtuals.messenger.browser;

import android.content.Context;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class Browser {

    public static void openUrl(Context context, String url){
        XposedHelpers.callStaticMethod(
                ClassLoad.getClass(ClassNames.BROWSER),
                AutomationResolver.resolve("Browser", "openUrl", AutomationResolver.ResolverType.Method), context, url
        );
    }

}
