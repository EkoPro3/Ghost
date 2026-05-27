package akro.ghost.tele.virtuals.messenger;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class NotificationCenter {


    private static int messagesDeleted = -1;
    private static int tlSchemeParseException = -1;


    public static int getMessagesDeleted() {
        if (messagesDeleted == -1) {
            messagesDeleted = XposedHelpers.getStaticIntField(ClassLoad.getClass(ClassNames.NOTIFICATION_CENTER), AutomationResolver.resolve("NotificationCenter", "messagesDeleted", AutomationResolver.ResolverType.Field));
        }
        return messagesDeleted;
    }

    public static int getTlSchemeParseException() {
        if (tlSchemeParseException == -1) {
            tlSchemeParseException = XposedHelpers.getStaticIntField(ClassLoad.getClass(ClassNames.NOTIFICATION_CENTER), AutomationResolver.resolve("NotificationCenter", "tlSchemeParseException", AutomationResolver.ResolverType.Field));
        }
        return tlSchemeParseException;
    }
}
