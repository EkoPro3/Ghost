package akro.ghost.tele.features;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.widget.ScrollView;
import android.widget.TextView;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.ClientChecker;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.Database.MessageDatabase;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.calendar.ConverterCalendar;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.language.Keys;
import akro.ghost.tele.language.Translator;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.ActionBar.AlertDialog;
import akro.ghost.tele.virtuals.SQLite.SQLiteCursor;
import akro.ghost.tele.virtuals.SQLite.SQLiteDatabase;
import akro.ghost.tele.virtuals.SettingsIconResolver;
import akro.ghost.tele.virtuals.Theme;
import akro.ghost.tele.virtuals.messenger.BaseController;
import akro.ghost.tele.virtuals.messenger.MessageObject;
import akro.ghost.tele.virtuals.messenger.MessagesStorage;
import akro.ghost.tele.virtuals.messenger.UserConfig;
import akro.ghost.tele.virtuals.tgnet.NativeByteBuffer;
import akro.ghost.tele.virtuals.tgnet.TLRPC;
import akro.ghost.tele.virtuals.ui.ChatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class SaveEditsHistory {

    private static MessageDatabase messageDatabase;

    public static boolean isEnable = false;

    public static void init(Context context) {
        try {
            if (!isEnable) {
                isEnable = true;
                messageDatabase = new MessageDatabase(context);

                if (ClassLoad.getClass(ClassNames.CHAT_ACTIVITY) != null) {

                    HMethod.hookMethod(
                            ClassLoad.getClass(ClassNames.CHAT_ACTIVITY), AutomationResolver.resolve("ChatActivity", "fillMessageMenu", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("fillMessageMenu", new Class[]{ClassLoad.getClass(ClassNames.MESSAGE_OBJECT), ArrayList.class, ArrayList.class, ArrayList.class}), new AbstractMethodHook() {
                                @Override
                                protected void afterMethod(MethodHookParam param) {
                                    if (ConfigManager.saveEditsHistory.isEnable()) {
                                        ChatActivity chatActivity = new ChatActivity(param.thisObject);

                                        if (chatActivity.getSelectedObject() != null) {
                                            MessageObject messageObject = chatActivity.getSelectedObject();

                                            if (messageObject.getMessageOwner() != null) {

                                                TLRPC.Message message = messageObject.getMessageOwner();
                                                if (message != null && message.getFrom_id() != null && message.getID() > 0) {

                                                    long user_id = message.getFrom_id().getUser_id();
                                                    long chat_id = message.getFrom_id().getChat_id();
                                                    long channel_id = message.getFrom_id().getChannel_id();
                                                    long dialogId = 0;

                                                    if (user_id != 0) {
                                                        dialogId = user_id;
                                                    } else if (chat_id != 0) {
                                                        dialogId = chat_id;
                                                    } else if (channel_id != 0) {
                                                        dialogId = channel_id;
                                                    }

                                                    if (dialogId != 0 && messageDatabase.getMessage(dialogId, message.getID()) != null) {

                                                        ArrayList<Integer> icons;
                                                        ArrayList<CharSequence> items;
                                                        ArrayList<Integer> options;

                                                        if (ClientChecker.check(ClientChecker.ClientType.Telegraph)) {
                                                            icons = (ArrayList<Integer>) param.args[2];
                                                            items = (ArrayList<CharSequence>) param.args[3];
                                                            options = (ArrayList<Integer>) param.args[4];
                                                        } else {
                                                            icons = (ArrayList<Integer>) param.args[1];
                                                            items = (ArrayList<CharSequence>) param.args[2];
                                                            options = (ArrayList<Integer>) param.args[3];
                                                        }

                                                        items.add(Translator.get(Keys.EditsHistory));
                                                        options.add(8353847);
                                                        if (!ClientChecker.check(ClientChecker.ClientType.Nagram))
                                                            icons.add(SettingsIconResolver.getIconSettings());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }));

                    HMethod.hookMethod(
                            ClassLoad.getClass(ClassNames.CHAT_ACTIVITY), AutomationResolver.resolve("ChatActivity", "processSelectedOption", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("processSelectedOption", new Class[]{int.class}), new AbstractMethodHook() {
                                @Override
                                protected void beforeMethod(MethodHookParam param) {
                                    if (ConfigManager.saveEditsHistory.isEnable()) {
                                        int option = (int) param.args[0];
                                        ChatActivity chatActivity = new ChatActivity(param.thisObject);

                                        if (option == 8353847) {

                                            if (chatActivity.getSelectedObject() != null) {
                                                MessageObject messageObject = chatActivity.getSelectedObject();

                                                if (messageObject.getMessageOwner() != null) {

                                                    TLRPC.Message message = messageObject.getMessageOwner();
                                                    if (message.message != null && message.getFrom_id() != null && message.getID() > 0 && message.getMessage() != null) {

                                                        if (getDialogId(message.getFrom_id()) != 0 & messageDatabase.searchMessage(getDialogId(message.getFrom_id()), message.getID())) {

                                                            AlertDialog alertDialog = new AlertDialog(context);
                                                            alertDialog.setTitle(Translator.get(Keys.EditsHistory));

                                                            TextView textView = new TextView(context);

                                                            int maxMsgCount = messageDatabase.getMaxMessageCount(getDialogId(message.getFrom_id()), message.getID());

                                                            StringBuilder builder = new StringBuilder();

                                                            if (maxMsgCount > 1) {

                                                                for (int i = 1; i <= maxMsgCount; i++) {

                                                                    String msg = messageDatabase.getMessage(getDialogId(message.getFrom_id()), message.getID(), i);

                                                                    if (msg != null) {

                                                                        long messageDate = messageDatabase.getMessageDate(getDialogId(message.getFrom_id()), message.getID(), i);

                                                                        if (messageDate != 0) {
                                                                            String date = ConverterCalendar.formatDate(messageDate);

                                                                            builder.append(Translator.get(Keys.Message)).append(i).append(" ").append(date).append("\n");
                                                                            builder.append(msg).append("\n");
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                long messageDate = messageDatabase.getMessageDate(getDialogId(message.getFrom_id()), message.getID(), 1);

                                                                if (messageDate != 0) {
                                                                    String date = ConverterCalendar.formatDate(messageDate);

                                                                    builder.append(date).append("\n");
                                                                }
                                                                builder.append(messageDatabase.getMessage(getDialogId(message.getFrom_id()), message.getID()));
                                                            }

                                                            textView.setText(builder.toString());
                                                            textView.setPadding(32, 32, 32, 32);
                                                            textView.setTextSize(16);
                                                            textView.setTextColor(Theme.getTextColor());
                                                            textView.setMovementMethod(new ScrollingMovementMethod());
                                                            textView.setTextIsSelectable(true);

                                                            ScrollView scrollView = new ScrollView(context);
                                                            scrollView.addView(textView);
                                                            alertDialog.setView(scrollView);
                                                            alertDialog.setPositiveButton(Translator.get(Keys.Done), null);
                                                            alertDialog.show();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }));
                }

                if (ClassLoad.getClass(ClassNames.MESSAGES_STORAGE) != null) {
                    HMethod.hookMethod(
                            ClassLoad.getClass(ClassNames.MESSAGES_STORAGE), AutomationResolver.resolve("MessagesStorage", "putMessages", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("putMessages", new Class[]{ClassLoad.getClass(ClassNames.TL_MESSAGES_MESSAGES), long.class, int.class, int.class, boolean.class, int.class, long.class}), new AbstractMethodHook() {
                                @Override
                                protected void beforeMethod(MethodHookParam param) {
                                    if (ConfigManager.saveEditsHistory.isEnable()) {
                                        int load_type = (int) param.args[2];
                                        if (ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                                            load_type = (int) param.args[0];
                                        }

                                        if (load_type == -2) {
                                            Object messagesStorageObject = param.thisObject;
                                            Object messagesObject = param.args[0];
                                            if (ClientChecker.check(ClientChecker.ClientType.Nagram)) {
                                                messagesObject = param.args[5];
                                            }

                                            if (messagesObject != null) {
                                                MessagesStorage messagesStorage = new MessagesStorage(messagesStorageObject);

                                                TLRPC.messages_Messages messages = new TLRPC.messages_Messages(messagesObject);

                                                int count = messages.getMessages().size();

                                                BaseController baseController = new BaseController(messagesStorageObject);
                                                UserConfig userConfig = baseController.getUserConfig();
                                                SQLiteDatabase sqLiteDatabase = messagesStorage.getDatabase();
                                                for (int a = 0; a < count; a++) {

                                                    TLRPC.Message message = new TLRPC.Message(messages.getMessages().get(a));

                                                    int id = message.getID();
                                                    SQLiteCursor cursor = sqLiteDatabase.queryFinalized(String.format(Locale.US, "SELECT data FROM messages_v2 WHERE mid = %d AND uid = %d", id, MessageObject.getDialogId(message)), new Object[]{});

                                                    if (cursor.next()) {
                                                        NativeByteBuffer data = cursor.byteBufferValue(0);

                                                        if (data.nativeByteBuffer != null) {

                                                            TLRPC.Message oldMessage = TLRPC.Message.TLdeserialize(data, data.readInt32(false), false);

                                                            oldMessage.readAttachPath(data, userConfig.getClientUserId());
                                                            data.reuse();
                                                            if (oldMessage.getMessage() != null && message.getMessage() != null) {
                                                                if (oldMessage.getFrom_id() != null && (!oldMessage.getMessage().equals(message.getMessage()))) {
                                                                    if (getDialogId(message.getFrom_id()) != 0) {
                                                                        messageDatabase.addMessage(getDialogId(message.getFrom_id()), oldMessage.getID(), oldMessage.getMessage());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }));
                }
            }
        } catch (Throwable t) {
            Logger.e(t);
        }
    }

    private static long getDialogId(TLRPC.Peer peer) {
        long user_id = peer.getUser_id();
        long chat_id = peer.getChat_id();
        long channel_id = peer.getChannel_id();
        long dialogId = 0;

        if (user_id != 0) {
            dialogId = user_id;
        } else if (chat_id != 0) {
            dialogId = chat_id;
        } else if (channel_id != 0) {
            dialogId = channel_id;
        }
        return dialogId;
    }

}
