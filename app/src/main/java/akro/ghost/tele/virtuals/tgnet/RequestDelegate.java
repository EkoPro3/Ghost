package akro.ghost.tele.virtuals.tgnet;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.utils.Utils;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;

import java.lang.reflect.Proxy;

import de.robv.android.xposed.XposedHelpers;

public class RequestDelegate {

    public Object requestDelegate;

    public RequestDelegate(Object obj){
        requestDelegate = obj;
    }

    public void run(Object response, Object error){
        XposedHelpers.callMethod(requestDelegate, AutomationResolver.resolve("RequestDelegate", "run", AutomationResolver.ResolverType.Method), response, error);
    }


    @FunctionalInterface
    public interface requestDelegate {
        void run(Object response, Object error);
    }

    public static Object run(requestDelegate lambda) {
        Class<?> requestDelegateClass = ClassLoad.getClass(ClassNames.REQUEST_DELEGATE);
        if (requestDelegateClass != null) {
            return Proxy.newProxyInstance(
                    Utils.classLoader,
                    new Class[]{requestDelegateClass},
                    (proxy, method, args) -> {
                        if (method.getParameterCount() == 2 && method.getParameterTypes()[0] == ClassLoad.getClass(ClassNames.TL_OBJECT)) {
                            lambda.run(args[0], args[1]);
                        }
                        return null;
                    }
            );
        }
        return null;
    }
}
