package akro.ghost.tele.virtuals.androidx;

import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class Adapter {

    private final Object adapter;

    public Adapter(Object adapter){
        this.adapter = adapter;
    }

    public void notifyItemChanged(int position) {
        XposedHelpers.callMethod(adapter, AutomationResolver.resolve("RecyclerListView", "notifyItemChanged", AutomationResolver.ResolverType.Method), position);
    }

}
