package akro.ghost.tele.virtuals.messenger;

import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class ImageReceiver {

    public Object imageReceiver;

    public ImageReceiver(Object imageReceiver){
        this.imageReceiver = imageReceiver;
    }

    public ImageLocation getImageLocation() {
        return new ImageLocation(XposedHelpers.callMethod(imageReceiver, AutomationResolver.resolve("ImageReceiver", "getImageLocation", AutomationResolver.ResolverType.Method)));
    }

}
