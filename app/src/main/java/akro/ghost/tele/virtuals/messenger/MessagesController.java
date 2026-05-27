package akro.ghost.tele.virtuals.messenger;

import android.content.SharedPreferences;
import android.util.SparseArray;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.androidx.LongSparseArray;
import akro.ghost.tele.virtuals.tgnet.TLRPC;

import de.robv.android.xposed.XposedHelpers;

public class MessagesController {
    final Object messagesController;

    public MessagesController(Object instance)
    {
        this.messagesController = instance;
    }

    public void processNewDifferenceParams(int seq, int pts, int date, int pts_count) {
        XposedHelpers.callMethod(messagesController, AutomationResolver.resolve("MessagesController", "processNewDifferenceParams", AutomationResolver.ResolverType.Method), seq, pts, date, pts_count);
    }

    public void processNewDifferenceParams(int pts, int date, int pts_count) {
        //Nagram
        XposedHelpers.callMethod(messagesController, "processNewDifferenceParams", pts, date, pts_count);
    }

    public void removePromoDialog() {
        XposedHelpers.callMethod(messagesController, AutomationResolver.resolve("MessagesController", "removePromoDialog", AutomationResolver.ResolverType.Method));
    }

    public static Object getInputChannel(TLRPC.InputPeer peer) {
        return XposedHelpers.callStaticMethod(ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER), AutomationResolver.resolve("MessagesController", "getInputChannel", AutomationResolver.ResolverType.Method), peer.inputPeer);
    }

    public SparseArray<Object> getDialogMessagesByIds() {
        return (SparseArray<Object>) XposedHelpers.getObjectField(messagesController, AutomationResolver.resolve("MessagesController", "dialogMessagesByIds", AutomationResolver.ResolverType.Field));
    }

    public LongSparseArray getDialogMessage() {
        return  new LongSparseArray(XposedHelpers.getObjectField(messagesController, AutomationResolver.resolve("MessagesController", "dialogMessage", AutomationResolver.ResolverType.Field)));
    }

    public static SharedPreferences getGlobalMainSettings() {
        return (SharedPreferences) XposedHelpers.callStaticMethod(ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER), AutomationResolver.resolve("MessagesController", "getGlobalMainSettings", AutomationResolver.ResolverType.Method));
    }

    public static Object getInputChannel(long id) {
        return XposedHelpers.callStaticMethod(ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER), AutomationResolver.resolve("MessagesController", "getInputChannel", AutomationResolver.ResolverType.Method), id);
    }

    public MessagesStorage getMessagesStorage() {
        return new MessagesStorage(XposedHelpers.callMethod(messagesController, AutomationResolver.resolve("MessagesController", "getMessagesStorage", AutomationResolver.ResolverType.Method)));
    }

    public static MessagesController getInstance(int num){
        return new MessagesController(XposedHelpers.callStaticMethod(ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER), AutomationResolver.resolve("MessagesController", "getInstance", AutomationResolver.ResolverType.Method), num));
    }
}
