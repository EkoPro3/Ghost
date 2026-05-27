package akro.ghost.tele.virtuals.ui;

import android.widget.FrameLayout;

import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class LaunchActivity {

    Object launchActivity;
    public FrameLayout frameLayout;

    public LaunchActivity(Object obj){
       launchActivity = obj;
       frameLayout = (FrameLayout) XposedHelpers.getObjectField(obj, AutomationResolver.resolve("LaunchActivity","frameLayout", AutomationResolver.ResolverType.Field));
    }

}
