package akro.ghost.tele.virtuals.tgnet;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class ConnectionsManager {
    final Object connectionsManager;

    public ConnectionsManager(Object instance)
    {
        this.connectionsManager = instance;
    }

    public void sendRequest(Object object, Object completionBlock) {
        XposedHelpers.callMethod(connectionsManager, AutomationResolver.resolve("ConnectionsManager", "sendRequest", AutomationResolver.ResolverType.Method), object, completionBlock);
    }

    public static ConnectionsManager getInstance(int num){
        return new ConnectionsManager(XposedHelpers.callStaticMethod(ClassLoad.getClass(ClassNames.CONNECTIONS_MANAGER), AutomationResolver.resolve("ConnectionsManager", "getInstance", AutomationResolver.ResolverType.Method), num));
    }
}
