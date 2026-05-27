package akro.ghost.tele.virtuals.ui.Cells;

import android.content.Context;
import android.view.View;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.obfuscate.AutomationResolver;

import de.robv.android.xposed.XposedHelpers;

public class HeaderCell {

    public Object headerCell;

    public HeaderCell(Context context){
        headerCell = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.HEADER_CELL), context);
    }

    public HeaderCell(Object obj){
       headerCell = obj;
    }

    public View getView(){
        return (View) headerCell;
    }

    public void setText(CharSequence text){
        XposedHelpers.callMethod(headerCell, AutomationResolver.resolve("HeaderCell","setText", AutomationResolver.ResolverType.Method), text);
    }

}
