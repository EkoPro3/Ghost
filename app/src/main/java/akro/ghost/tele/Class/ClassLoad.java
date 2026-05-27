package akro.ghost.tele.Class;

import akro.ghost.tele.ClientChecker;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XposedHelpers;

public class ClassLoad {

    private static final Map<String, Class<?>> cache = new HashMap<>();

    public static Class<?> getClass(String name) {
        String resolved = AutomationResolver.resolve(name);

        if (cache.containsKey(resolved)) {
            return cache.get(resolved);
        }

        try {
            Class<?> cls = XposedHelpers.findClassIfExists(
                    resolved,
                    Utils.classLoader
            );
            if (cls != null) {
                cache.put(resolved, cls);
            } else {
                if ((ClientChecker.check(ClientChecker.ClientType.Nagram) || ClientChecker.check(ClientChecker.ClientType.Momogram)) && name.equals(ClassNames.DRAWABLE)) return null;
                Logger.w("Not found " + name + ", " + resolved + " " + Utils.issue);
            }
            return cls;

        } catch (Throwable e) {
            Logger.e(e);
            return null;
        }
    }

    public static Class<?> getClass(String name, ClassLoader classLoader) {
        String resolved = AutomationResolver.resolve(name);

        if (cache.containsKey(resolved)) {
            return cache.get(resolved);
        }

        try {
            Class<?> cls = XposedHelpers.findClassIfExists(
                    resolved,
                    classLoader
            );
            if (cls != null) {
                cache.put(resolved, cls);
            } else {
                Logger.w("Not found Class " + name + ", " + resolved + " " + Utils.issue);
            }
            return cls;

        } catch (Throwable e) {
            Logger.e(e);
            return null;
        }
    }

}
