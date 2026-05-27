package akro.ghost.tele.virtuals.messenger;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class Utilities {

    public static DispatchQueue getStageQueue(){
        return new DispatchQueue(XposedHelpers.getStaticObjectField(ClassLoad.getClass(ClassNames.UTILITIES), AutomationResolver.resolve("Utilities", "stageQueue", AutomationResolver.ResolverType.Field)));
    }
}
