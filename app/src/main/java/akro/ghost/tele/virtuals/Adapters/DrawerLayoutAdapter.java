package akro.ghost.tele.virtuals.Adapters;

import akro.ghost.tele.obfuscate.AutomationResolver;

import java.util.ArrayList;

import de.robv.android.xposed.XposedHelpers;

public class DrawerLayoutAdapter {

    private final Object drawerLayout;

    public DrawerLayoutAdapter(Object obj){
        drawerLayout = obj;
    }

    public ArrayList<?> getItems(){
        return (ArrayList<?>) XposedHelpers.getObjectField(drawerLayout, AutomationResolver.resolve("DrawerLayoutAdapter", "items", AutomationResolver.ResolverType.Field));
    }

}
