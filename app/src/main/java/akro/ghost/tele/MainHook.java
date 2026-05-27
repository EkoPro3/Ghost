package akro.ghost.tele;


import android.app.Activity;
import android.os.Bundle;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.utils.Utils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private boolean isStart;

    @Override
    public void initZygote(StartupParam startupParam){ Utils.modulePath = startupParam.modulePath; }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (!ClientChecker.ClientType.containsPackage(lpparam.packageName)) {
            return;
        }
        Utils.pkgName = lpparam.packageName;
        Utils.classLoader = lpparam.classLoader;


        HMethod.hookMethod(ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY), "onCreate", Bundle.class, new AbstractMethodHook() {
            @Override
            protected void beforeMethod(MethodHookParam param) {
                Activity launchActivity = (Activity) param.thisObject;
                if (!isStart) {
                    TeleVip.startHook(launchActivity);
                    isStart = true;
                }
            }
        });
    }


}

