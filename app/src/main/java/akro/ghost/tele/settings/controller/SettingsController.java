package akro.ghost.tele.settings.controller;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import akro.ghost.tele.Configs.ConfigPreferences;
import akro.ghost.tele.language.Keys;
import akro.ghost.tele.language.Translator;
import akro.ghost.tele.settings.ui.SettingsActivity;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.virtuals.ActionBar.AlertDialog;
import akro.ghost.tele.virtuals.messenger.browser.Browser;
import akro.ghost.tele.virtuals.ui.LaunchActivity;

/**
 * AkroGhost Settings Controller
 * Handles show/hide with smooth animations
 */
public class SettingsController {

    public FrameLayout settingsView;
    private final Context context;
    public SettingsActivity settingsActivity;

    public SettingsController(Context context) {
        this.context = context;
    }

    public void openView() {
        try {
            if (settingsView == null) {
                settingsView = new FrameLayout(context);
            }

            settingsView.removeAllViews();
            settingsActivity = new SettingsActivity(context);
            showJoinDialog();

            settingsView.addView(settingsActivity.createView(this));
            show(settingsView);

        } catch (Throwable e) {
            Logger.e(e);
        }
    }

    private void showJoinDialog() {
        try {
            if (!ConfigPreferences.getBoolean("JTV")) {
                AlertDialog alertDialog = new AlertDialog(context);
                alertDialog.setTitle(Translator.get(Keys.GhostMode));
                alertDialog.setMessage(Translator.get(Keys.JoinTeleVip));

                alertDialog.setPositiveButton(Translator.get(Keys.Join), AlertDialog.click(() -> {
                    Browser.openUrl(context, "https://t.me/A_KOJO");
                    hide();
                }));

                alertDialog.setNegativeButton(Translator.get(Keys.Cancel), null);
                alertDialog.setNeutralButton(Translator.get(Keys.DontShowAgain),
                        AlertDialog.click(() -> ConfigPreferences.putBoolean("JTV", true)));
                alertDialog.show();
            }
        } catch (Throwable e) {
            Logger.e(e);
        }
    }

    public void show(View target) {
        LaunchActivity launchActivity = new LaunchActivity(context);

        if (target.getParent() == null) {
            launchActivity.frameLayout.addView(target);
        }

        for (int i = 0; i < launchActivity.frameLayout.getChildCount(); i++) {
            View child = launchActivity.frameLayout.getChildAt(i);
            child.setVisibility(child == target ? View.VISIBLE : View.GONE);
        }

        target.bringToFront();

        // Animate the container in
        target.setAlpha(0f);
        target.animate()
                .alpha(1f)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    public void hide() {
        LaunchActivity launchActivity = new LaunchActivity(settingsView.getContext());

        for (int i = 0; i < launchActivity.frameLayout.getChildCount(); i++) {
            View child = launchActivity.frameLayout.getChildAt(i);
            child.setVisibility(child == settingsView ? View.GONE : View.VISIBLE);
        }

        if (settingsView != null && settingsView.getParent() != null) {
            launchActivity.frameLayout.removeView(settingsView);
        }

        SettingsActivity.isSettings = false;
    }

    public Context getContext() {
        return context;
    }
}
