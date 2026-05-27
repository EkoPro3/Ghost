package akro.ghost.tele.settings.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Drawable.ArrowDrawable;
import akro.ghost.tele.audio;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.dex.DexInjector;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.language.Keys;
import akro.ghost.tele.language.Translator;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.settings.controller.SettingsController;
import akro.ghost.tele.ui.toolBar.MainToolBar;
import akro.ghost.tele.virtuals.TeleVip.Bridge.Bridge;
import akro.ghost.tele.virtuals.Theme;
import akro.ghost.tele.virtuals.ui.Components.RecyclerListView;
import akro.ghost.tele.application.AndroidUtilities;

import de.robv.android.xposed.XposedHelpers;

/**
 * AkroGhost Settings UI
 * Ghost-themed settings screen with slide-up entrance animation
 */
public class SettingsActivity {

    public static boolean isSettings;
    private final Context context;
    public RecyclerListView listView;

    public View createView(SettingsController settingsController) {
        LinearLayout layout = new LinearLayout(context);

        try {
            layout.setOrientation(LinearLayout.VERTICAL);

            // Rich dark background
            GradientDrawable bgGradient = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{Theme.getBackgroundGrayColor(), Theme.getBackgroundDeepColor()}
            );
            layout.setBackground(bgGradient);

            // Premium toolbar
            MainToolBar toolbar = new MainToolBar(context);
            toolbar.setColorTitle(Theme.getTextToolBarColor());
            toolbar.setRippleColor(Theme.getToolBarRippleColor());
            toolbar.setTextTitle(Translator.get(Keys.GhostMode));

            ArrowDrawable arrow = new ArrowDrawable();
            toolbar.setImageDrawable(arrow);
            toolbar.getImage().setOnClickListener(v -> {
                hideWithAnimation(layout, settingsController);
            });

            layout.addView(toolbar);

            // Spacer line under toolbar (accent glow)
            View accentLine = new View(context);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, AndroidUtilities.dp(1));
            accentLine.setLayoutParams(lineParams);
            accentLine.setBackgroundColor(Theme.isLight() ? Theme.LIGHT_ACCENT : Theme.GHOST_ACCENT_CYAN);
            accentLine.setAlpha(0.5f);
            layout.addView(accentLine);

            listView = new RecyclerListView(context);

            Object adapter = XposedHelpers.newInstance(
                    ClassLoad.getClass(ClassNames.SETTINGS_ADAPTER_LIST_ADAPTER, DexInjector.classLoader),
                    context);

            listView.setAdapter(adapter);
            listView.setBackgroundColor(Theme.getBackgroundWhiteOrBlueColor());
            listView.setVerticalScrollBarEnabled(false);
            listView.setLayoutManager(Bridge.getLayoutManager(context));

            LinearLayout.LayoutParams recyclerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
            recyclerParams.setMargins(0, 0, 0, 0);
            layout.addView(listView.getRecyclerListView(), recyclerParams);

            // Entrance animation: slide from bottom + fade in
            animateIn(layout);

        } catch (Throwable e) {
            Logger.e(e);
        }

        return layout;
    }

    /** Slide-up + fade in animation when opening settings */
    private void animateIn(View view) {
        view.setAlpha(0f);
        view.setTranslationY(120f);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(400);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator slideUp = ObjectAnimator.ofFloat(view, "translationY", 120f, 0f);
        slideUp.setDuration(420);
        slideUp.setInterpolator(new DecelerateInterpolator(1.6f));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(fadeIn, slideUp);
        set.setStartDelay(30);
        set.start();
    }

    /** Slide-down + fade out when closing */
    private void hideWithAnimation(View view, SettingsController controller) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.setDuration(250);
        fadeOut.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator slideDown = ObjectAnimator.ofFloat(view, "translationY", 0f, 80f);
        slideDown.setDuration(280);
        slideDown.setInterpolator(new DecelerateInterpolator(1.2f));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(fadeOut, slideDown);
        set.start();

        view.postDelayed(controller::hide, 270);
    }

    public SettingsActivity(Context context) {
        this.context = context;
        isSettings = true;
    }

    public static void init(SettingsController settingsController) {
        audio.init();
        try {
            HMethod.hookMethod(ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY), "onBackPressed", new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam param) {
                    if (isSettings) {
                        settingsController.hide();
                        settingsController.settingsView = null;
                        param.setResult(null);
                    }
                }
            });

            HMethod.hookMethod(ClassLoad.getClass(ClassNames.ANDROID_UTILITIES),
                    AutomationResolver.resolve("AndroidUtilities", "isTablet", AutomationResolver.ResolverType.Method),
                    new AbstractMethodHook() {
                        @Override
                        protected void beforeMethod(MethodHookParam param) {
                            if (isSettings) param.setResult(true);
                        }
                    });
        } catch (Throwable e) {
            Logger.e(e);
        }
    }
}
