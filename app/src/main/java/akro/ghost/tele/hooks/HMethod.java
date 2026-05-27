package akro.ghost.tele.hooks;

import akro.ghost.tele.ClientChecker;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;

import java.lang.reflect.Method;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HMethod {

    public static void hookMethod(Class<?> cls, String name, Object... args) {
        try {
            if (cls != null) {
                XposedHelpers.findAndHookMethod(cls, name, args);
            }
        } catch (Throwable t) {
            Logger.e(t);
        }
    }

    public static void hookConstructor(Class<?> cls, Object... args) {
        try {
            if (cls != null) {
                XposedHelpers.findAndHookConstructor(cls, args);
            }
        } catch (Throwable t) {
            Logger.e(t);
        }
    }

    public static void hookMethod(Class<?> cls, String className, String[] names, Object... args) {
        try {
            if (cls != null) {
                for (String name : names) {
                    if (ClientChecker.check(ClientChecker.ClientType.Nagram) && name.equals("formatPmEditedDate")) continue;
                    XposedHelpers.findAndHookMethod(cls, AutomationResolver.resolve(className, name, AutomationResolver.ResolverType.Method), args);
                }
            }
        } catch (Throwable t) {
            Logger.e(t);
        }
    }
    public static void hookMethod(Method method, AbstractMethodHook callback) {
        try {
            if (method != null) {
                XposedBridge.hookMethod(method, callback);
            }
        } catch (Throwable t) {
            Logger.e(t);
        }
    }

}
