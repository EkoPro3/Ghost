package akro.ghost.tele.virtuals.ui.Cells;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import akro.ghost.tele.application.AndroidUtilities;
import akro.ghost.tele.virtuals.Theme;
import akro.ghost.tele.virtuals.ui.Components.LayoutHelper;

public class TextInfoCell extends FrameLayout {

    private final TextView textView;

    public TextInfoCell(Context context) {
        super(context);

        textView = new TextView(context);
        textView.setTextColor(Theme.getTextGrayColor());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, AndroidUtilities.dp(19), 0, AndroidUtilities.dp(19));
        addView(textView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER, 17, 0, 17, 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
    }

    public TextView getTextView() {
        return textView;
    }
}
