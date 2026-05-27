package akro.ghost.tele.virtuals.messenger;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.SQLite.SQLiteDatabase;

import de.robv.android.xposed.XposedHelpers;

public class MessagesStorage {

    Object messagesStorage;

    public MessagesStorage(Object obj) {
        messagesStorage = obj;
    }

    public SQLiteDatabase getDatabase() {

        return new SQLiteDatabase(XposedHelpers.callMethod(messagesStorage, AutomationResolver.resolve("MessagesStorage", "getDatabase", AutomationResolver.ResolverType.Method)));
    }

    public DispatchQueue getStorageQueue() {

        return new DispatchQueue(XposedHelpers.callMethod(messagesStorage, AutomationResolver.resolve("MessagesStorage", "getStorageQueue", AutomationResolver.ResolverType.Method)));
    }

    public static MessagesStorage getInstance(int num) {
        return new MessagesStorage(XposedHelpers.callStaticMethod(ClassLoad.getClass(ClassNames.MESSAGES_STORAGE), AutomationResolver.resolve("MessagesStorage", "getInstance", AutomationResolver.ResolverType.Method), num));
    }

}
