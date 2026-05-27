package akro.ghost.tele.ui.toolBar;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import akro.ghost.tele.application.AndroidUtilities;
import akro.ghost.tele.virtuals.Theme;

/**
 * AkroGhost Main Toolbar
 * Premium ghost-themed toolbar with gradient background & glow accent
 */
public class MainToolBar extends LinearLayout {

    private final Context context;
    private TextView title;
    private TextView subtitle;
    private ImageView image;

    public MainToolBar(Context context) {
        super(context);
        this.context = context;
        createToolbar();
    }

    @SuppressLint({"InternalInsetResource", "DiscouragedApi"})
    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void createToolbar() {
        int statusBar = getStatusBarHeight(context);
        boolean dark = !Theme.isLight();

        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                AndroidUtilities.dp(60) + statusBar));

        // Gradient background: dark navy → slightly lighter
        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Theme.getToolBarColor(), Theme.getToolBarGradientEndColor()}
        );
        this.setBackground(gradient);

        this.setFitsSystemWindows(false);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setPadding(
                AndroidUtilities.dp(4),
                statusBar,
                AndroidUtilities.dp(16),
                0
        );

        // Elevation shadow (API 21+)
        this.setElevation(AndroidUtilities.dp(4));

        // Back arrow button
        image = new ImageView(context);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                AndroidUtilities.dp(44), AndroidUtilities.dp(44));
        iconParams.gravity = Gravity.CENTER_VERTICAL;
        image.setLayoutParams(iconParams);
        image.setPadding(
                AndroidUtilities.dp(10), AndroidUtilities.dp(10),
                AndroidUtilities.dp(10), AndroidUtilities.dp(10));
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // Title + subtitle vertical container
        LinearLayout textContainer = new LinearLayout(context);
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams tcParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
        tcParams.gravity = Gravity.CENTER_VERTICAL;
        tcParams.setMargins(AndroidUtilities.dp(6), 0, 0, 0);
        textContainer.setLayoutParams(tcParams);

        // Main title
        title = new TextView(context);
        title.setTextSize(17f);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setMaxLines(1);
        title.setLetterSpacing(0.02f);
        title.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // Subtitle "@A_KOJO"
        subtitle = new TextView(context);
        subtitle.setText("@A_KOJO");
        subtitle.setTextSize(11f);
        subtitle.setTypeface(Typeface.DEFAULT);
        subtitle.setAlpha(0.65f);
        subtitle.setMaxLines(1);
        subtitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        textContainer.addView(title);
        textContainer.addView(subtitle);

        this.addView(image);
        this.addView(textContainer);

        // Apply colors
        applyColors();

        // Animate toolbar entrance (alpha fade-in)
        this.setAlpha(0f);
        this.animate().alpha(1f).setDuration(350)
                .setInterpolator(new DecelerateInterpolator())
                .setStartDelay(80)
                .start();
    }

    private void applyColors() {
        boolean dark = !Theme.isLight();
        title.setTextColor(Theme.getTextToolBarColor());
        subtitle.setTextColor(dark ? Theme.GHOST_ACCENT_CYAN : Theme.LIGHT_ACCENT);

        // Accent bottom border line (simulate glow line)
        if (dark) {
            GradientDrawable accent = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{Color.TRANSPARENT, Theme.GHOST_ACCENT_CYAN, Color.TRANSPARENT}
            );
            accent.setSize(AndroidUtilities.dp(1000), AndroidUtilities.dp(1));
        }
    }

    // Animate toolbar in with a slide-down + fade effect
    public void animateIn(int statusBarHeight) {
        this.setTranslationY(-AndroidUtilities.dp(60) - statusBarHeight);
        this.setAlpha(0f);
        this.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(380)
                .setInterpolator(new DecelerateInterpolator(1.4f))
                .start();
    }

    public void setTextTitle(CharSequence titleText) {
        this.title.setText(titleText);
    }

    public void setColorTitle(int colorTitle) {
        this.title.setTextColor(colorTitle);
    }

    public void setImageDrawable(Drawable drawable) {
        this.image.setImageDrawable(drawable);
    }

    public void setRippleColor(int color) {
        android.graphics.drawable.RippleDrawable ripple = new android.graphics.drawable.RippleDrawable(
                ColorStateList.valueOf(color),
                null,
                null
        );
        image.setBackground(ripple);
    }

    public ImageView getImage() {
        return this.image;
    }
}
