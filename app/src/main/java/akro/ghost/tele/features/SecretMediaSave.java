package akro.ghost.tele.features;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.messenger.MessageObject;
import akro.ghost.tele.virtuals.tgnet.TLRPC;
import akro.ghost.tele.virtuals.ui.Cells.ChatMessageCell;
import akro.ghost.tele.virtuals.ui.PhotoViewer;
import akro.ghost.tele.virtuals.ui.SecretMediaViewer;

import java.io.File;

public class SecretMediaSave {

    public static boolean isEnable = false;

    public static long id;
    public static File pathImage;

    public static void init() {
        try {
            if (!isEnable) {
                isEnable = true;
                if (ClassLoad.getClass(ClassNames.MESSAGE_OBJECT) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.MESSAGE_OBJECT), AutomationResolver.resolve("MessageObject", "isSecret", AutomationResolver.ResolverType.Method), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (ConfigManager.secretMediaSave.isEnable()) param.setResult(false);
                        }
                    });
                }
                if (ClassLoad.getClass(ClassNames.CHAT_MESSAGE_CELL_DELEGATE) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.CHAT_MESSAGE_CELL_DELEGATE), AutomationResolver.resolve("ChatActivity$ChatMessageCellDelegate", "didPressImage", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("didPressImage", new Class[]{ClassLoad.getClass(ClassNames.CHAT_MESSAGE_CELL), float.class, float.class, boolean.class}), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            try {
                                if (ConfigManager.secretMediaSave.isEnable() && param.args[0] != null) {
                                    ChatMessageCell messageCell = new ChatMessageCell(param.args[0]);
                                    if (messageCell.getChatMessageCell() != null) {
                                        MessageObject messageObject = messageCell.getMessageObject();
                                        if (messageObject.getMessageObject() != null) {
                                            TLRPC.Message message = messageObject.getMessageOwner();
                                            if (message.getTtl() > 0) message.setTtl(0);
                                        }
                                        bindPhotoViewerToActivity(messageCell);
                                    }
                                }
                            } catch (Throwable e) {
                                Logger.e(e);
                            }
                        }
                    }));
                }

                if (ClassLoad.getClass(ClassNames.FILE_LOADER) != null) {
                    HMethod.hookMethod(ClassLoad.getClass(ClassNames.FILE_LOADER), AutomationResolver.resolve("FileLoader", "getPathToMessage", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("getPathToMessage", new Class[]{ClassLoad.getClass(ClassNames.TL_MESSAGE)}), new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            try {
                                if (ConfigManager.secretMediaSave.isEnable() && param.args[0] != null && pathImage != null) {
                                    TLRPC.Message message = new TLRPC.Message(param.args[0]);
                                    if (message.getID() == id) {
                                        param.setResult(pathImage);
                                    }
                                }
                            } catch (Throwable e) {
                                Logger.e(e);
                            }
                        }
                    }));
                }


                if (ConfigManager.secretMediaSave.isEnable()) SecretMediaViewer.openMedia();
            }
        } catch (Throwable e){
            Logger.e(e);
        }
    }

    private static void bindPhotoViewerToActivity(ChatMessageCell cell) {
        try {
            if (!(cell.getChatMessageCell() instanceof View)) return;
            View view = (View) cell.getChatMessageCell();
            Context context = view.getContext();
            Activity activity = extractActivityFromContext(context);
            if (activity == null) return;

            PhotoViewer.getInstance().setParentActivity(activity);
        } catch (Throwable ignored) {}
    }

    private static Activity extractActivityFromContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) return (Activity) context;
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
