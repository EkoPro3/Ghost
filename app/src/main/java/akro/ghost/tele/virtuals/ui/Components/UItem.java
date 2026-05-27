package akro.ghost.tele.virtuals.ui.Components;

import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class UItem {

    Object uItem;

    public UItem(Object uItem){
        this.uItem = uItem;
    }

    public int getID(){
        return XposedHelpers.getIntField(uItem, AutomationResolver.resolve("UItem", "id", AutomationResolver.ResolverType.Field));
    }

    public Object getUItem(){
        return uItem;
    }
}
