package akro.ghost.tele.virtuals.ui;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.features.SecretMediaSave;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.virtuals.messenger.FileLoader;
import akro.ghost.tele.virtuals.messenger.MessageObject;
import akro.ghost.tele.virtuals.messenger.UserConfig;
import akro.ghost.tele.virtuals.tgnet.TLRPC;

import java.io.File;

public class SecretMediaViewer {

    private static boolean isEnable = false;

    public static void openMedia() {
        try {
            if (!isEnable) {
                isEnable = true;

                if (ClassLoad.getClass(ClassNames.SECRET_MEDIA_VIEWER) == null) return;

                HMethod.hookMethod(ClassLoad.getClass(ClassNames.SECRET_MEDIA_VIEWER), AutomationResolver.resolve("SecretMediaViewer", "openMedia", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("PhotoViewer$PhotoViewerProvider", new Class[]{ClassLoad.getClass(ClassNames.MESSAGE_OBJECT), ClassLoad.getClass(ClassNames.PHOTO_VIEWER_PROVIDER), java.lang.Runnable.class, java.lang.Runnable.class}), new AbstractMethodHook() {
                    @Override
                    protected void beforeMethod(MethodHookParam param) {
                        if (ConfigManager.preventMedia.isEnable() && !ConfigManager.secretMediaSave.isEnable()) {
                            param.args[2] = null;
                            param.args[3] = null;

                            MessageObject messageObject = new MessageObject(param.args[0]);
                            if (messageObject.getMessageObject() != null) {
                                TLRPC.Message messageOwner = messageObject.getMessageOwner();
                                if (messageOwner.message != null) {
                                    messageOwner.setTtl(0);
                                }
                            }
                        }

                        if (ConfigManager.secretMediaSave.isEnable() && (param.args.length >= 2 || param.args[0] != null)) {
                            MessageObject messageObject = new MessageObject(param.args[0]);
                            PhotoViewer.PhotoViewerProvider provider = new PhotoViewer.PhotoViewerProvider(param.args[1]);
                            final PhotoViewer.PlaceProviderObject object = provider.getPlaceForPhoto(messageObject, null, 0, true, false);
                            if (object.getImageReceiver().imageReceiver != null) {
                                FileLoader fileLoader = FileLoader.getInstance(UserConfig.getSelectedAccount());
                                File image = fileLoader.getLocalFile(object.getImageReceiver().getImageLocation());
                                if (image != null) {
                                    SecretMediaSave.pathImage = image;
                                    SecretMediaSave.id = messageObject.getMessageOwner().getID();
                                }
                                PhotoViewer.getInstance().openPhoto(messageObject, messageObject.getDialogId(), 0L, 0L, provider, false);
                                param.setResult(null);
                            }
                        }
                    }
                }));
            }
        } catch (Throwable e) {
            Logger.e(e);
        }
    }

}