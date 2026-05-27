package akro.ghost.tele.features.otherFeatures;

import android.view.View;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.ui.PhotoViewer;

public class AlwaysSaveMedia {
    public static void init() {
        try {
            if (ClassLoad.getClass(ClassNames.PHOTO_VIEWER) != null) {

                HMethod.hookMethod(ClassLoad.getClass(ClassNames.PHOTO_VIEWER), AutomationResolver.resolve("PhotoViewer", "setIsAboutToSwitchToIndex", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("setIsAboutToSwitchToIndex", new Class[]{int.class, boolean.class, boolean.class, boolean.class}), new AbstractMethodHook() {
                    @Override
                    protected void afterMethod(MethodHookParam param) {
                        final PhotoViewer photoViewer = new PhotoViewer(param.thisObject);
                        if (photoViewer.getGalleryButton()!= null) photoViewer.getGalleryButton().setVisibility(View.VISIBLE);

                    }
                }));
            }
        } catch (Throwable t) {
            Logger.e(t);
        }
    }
}