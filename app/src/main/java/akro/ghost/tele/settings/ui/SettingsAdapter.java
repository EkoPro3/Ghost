package akro.ghost.tele.settings.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import akro.ghost.tele.Configs.ConfigItem;
import akro.ghost.tele.Configs.ConfigManager;
import akro.ghost.tele.utils.Utils;
import akro.ghost.tele.audio;
import akro.ghost.tele.language.Keys;
import akro.ghost.tele.language.Translator;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.settings.controller.SettingsController;
import akro.ghost.tele.utils.DialogUtils;
import akro.ghost.tele.virtuals.Theme;
import akro.ghost.tele.virtuals.androidx.ViewHolder;
import akro.ghost.tele.virtuals.messenger.browser.Browser;
import akro.ghost.tele.virtuals.ui.Cells.HeaderCell;
import akro.ghost.tele.virtuals.ui.Cells.TextCheckCell;
import akro.ghost.tele.virtuals.ui.Cells.TextInfoCell;
import akro.ghost.tele.virtuals.ui.Cells.TextSettingsCell;

import de.robv.android.xposed.XposedHelpers;

/**
 * AkroGhost Settings Adapter
 * Enhanced cell styling with ghost theme colors and subtle row animations
 */
public class SettingsAdapter {

    private static boolean isLongText = false;

    public static int getRow(int position) { return ConfigManager.getItems().get(position).getType(); }

    public static int getRowCount() { return ConfigManager.getItems().size(); }

    public static void onBindViewHolder(Object holder, SettingsController settingsController, int position, int viewType) {
        try {
            ConfigItem item = ConfigManager.getItems().get(position);

            switch (viewType) {
                case ConfigItem.HEADER:
                    HeaderCellHolder headerCell = new HeaderCellHolder(holder);
                    headerCell.cell.setText(Translator.get(item.getKey()));
                    // Style header text in ghost accent color
                    try {
                        android.view.View hView = headerCell.cell.getView();
                        if (hView instanceof android.view.ViewGroup) {
                            android.view.ViewGroup hGroup = (android.view.ViewGroup) hView;
                            for (int i = 0; i < hGroup.getChildCount(); i++) {
                                android.view.View child = hGroup.getChildAt(i);
                                if (child instanceof TextView) {
                                    TextView headerText = (TextView) child;
                                    headerText.setTextColor(Theme.getHeaderTextColor());
                                    headerText.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
                                    headerText.setLetterSpacing(0.04f);
                                    break;
                                }
                            }
                        }
                    } catch (Throwable ignored) {}
                    break;

                case ConfigItem.SWITCH:
                    TextCheckCellHolder textCheck = new TextCheckCellHolder(holder);
                    if (item.getValue() != null) {
                        textCheck.cell.setTextAndValueAndCheck(
                                Translator.get(item.getKey()), item.getValue(),
                                item.isEnable(), true, false);
                    } else if (item.isRestartRequired()) {
                        textCheck.cell.setTextAndValueAndCheck(
                                Translator.get(item.getKey()),
                                Translator.get(Keys.RestartRequired),
                                item.isEnable(), true, false);
                    } else {
                        textCheck.cell.setTextAndCheck(Translator.get(item.getKey()), item.isEnable(), false);
                    }
                    textCheck.cell.getTextView().setLines(0);
                    textCheck.cell.getTextView().setMaxLines(0);
                    textCheck.cell.getTextView().setSingleLine(false);
                    textCheck.cell.getTextView().setEllipsize(null);
                    break;

                case ConfigItem.TEXT:
                    TextSettingsCellHolder settingsCell = new TextSettingsCellHolder(holder);
                    if (item.getKey().equals(Keys.Calendar)) {
                        String value = null;
                        switch (item.getCustomCalendar()) {
                            case 0: value = Translator.get(Keys.Gregorian); break;
                            case 1: value = Translator.get(Keys.Hijri); break;
                            case 2: value = Translator.get(Keys.Persian); break;
                        }
                        settingsCell.cell.setTextAndValue(Translator.get(item.getKey()), value, false, false);
                    } else {
                        settingsCell.cell.setText(Translator.get(item.getKey()), false);
                        settingsCell.cell.getTextView().setTextColor(Theme.getAccentCyanColor());
                    }
                    break;

                case ConfigItem.DIVIDER:
                    ShadowSectionCellHolder shadowSectionCell = new ShadowSectionCellHolder(holder);
                    shadowSectionCell.cell.setBackgroundColor(Theme.getSectionBackgroundColor());
                    break;

                case ConfigItem.INFO:
                    TextInfoCellHolder textInfoCell = new TextInfoCellHolder(holder);
                    TextView textView = textInfoCell.text.getTextView();
                    if (item.getKey().equals(Keys.OfflineVisibilityInfo)) {
                        textView.setMaxLines(2);
                        textView.setEllipsize(TextUtils.TruncateAt.END);
                        textView.setText(Translator.get(Keys.OfflineVisibilityInfo));
                        textView.setTextColor(Theme.getTextGrayColor());
                        textView.setOnClickListener(v -> {
                            if (!isLongText) {
                                textView.setMaxLines(Integer.MAX_VALUE);
                                textView.setEllipsize(null);
                                isLongText = true;
                            } else {
                                textView.setMaxLines(2);
                                textView.setEllipsize(TextUtils.TruncateAt.END);
                                textView.setText(Translator.get(Keys.OfflineVisibilityInfo));
                                isLongText = false;
                            }
                        });
                    }
                    break;
            }

            ViewHolder viewHolder = new ViewHolder(holder);

            // Apply row background styling (subtle card look for dark mode)
            try {
                View rowView = viewHolder.getItemView();
                if (!Theme.isLight() && viewType == ConfigItem.SWITCH) {
                    GradientDrawable bg = new GradientDrawable();
                    bg.setColor(Theme.GHOST_BG_CARD);
                    bg.setCornerRadius(0f);
                    rowView.setBackground(bg);
                }
            } catch (Throwable ignored) {}

            viewHolder.getItemView().setOnLongClickListener(v -> {
                if (audio.playing) audio.stop();
                else {
                    audio.start();
                    DialogUtils.showQuranAlert(settingsController.getContext());
                }
                return true;
            });

            viewHolder.getItemView().setOnClickListener(v -> {
                if (viewType == ConfigItem.SWITCH) {
                    TextCheckCellHolder textCheck = new TextCheckCellHolder(holder);
                    boolean checked = !textCheck.cell.isChecked();
                    textCheck.cell.setChecked(checked);
                    item.setEnable(checked);
                    item.run();
                } else if (viewType == ConfigItem.TEXT) {
                    switch (item.getKey()) {
                        case Keys.DeveloperChannel:
                            Browser.openUrl(settingsController.getContext(), "https://t.me/A_KOJO");
                            settingsController.hide();
                            break;
                        case Keys.RestartApp:
                            Intent intent = settingsController.getContext()
                                    .getPackageManager()
                                    .getLaunchIntentForPackage(Utils.pkgName);
                            if (intent != null) {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                settingsController.getContext().startActivity(intent);
                            }
                            ((Activity) settingsController.getContext()).finishAffinity();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            break;
                        case Keys.Calendar:
                            Dialog dlg = DialogUtils.createSingleChoiceDialog(
                                    (Activity) settingsController.getContext(),
                                    new String[]{
                                            Translator.get(Keys.Gregorian),
                                            Translator.get(Keys.Hijri),
                                            Translator.get(Keys.Persian)},
                                    Translator.get(Keys.Calendar), item.getCustomCalendar(),
                                    (dialog, which) -> {
                                        item.setCustomCalendar(which);
                                        item.run();
                                        if (settingsController.settingsActivity.listView.getAdapter() != null) {
                                            settingsController.settingsActivity.listView.getAdapter().notifyItemChanged(position);
                                        }
                                    });
                            dlg.show();
                            break;
                    }
                }
            });

        } catch (Throwable t) {
            Logger.e(t);
        }
    }

    // === ViewHolder helpers ===
    public static class HeaderCellHolder {
        HeaderCell cell;
        public HeaderCellHolder(Object obj) {
            cell = new HeaderCell(XposedHelpers.getObjectField(obj, "headerCell"));
        }
    }

    public static class TextCheckCellHolder {
        TextCheckCell cell;
        public TextCheckCellHolder(Object obj) {
            cell = new TextCheckCell(XposedHelpers.getObjectField(obj, "textCheckCell"));
        }
    }

    public static class TextSettingsCellHolder {
        TextSettingsCell cell;
        public TextSettingsCellHolder(Object obj) {
            cell = new TextSettingsCell(XposedHelpers.getObjectField(obj, "textSettingsCell"));
        }
    }

    public static class ShadowSectionCellHolder {
        View cell;
        public ShadowSectionCellHolder(Object obj) {
            cell = (View) XposedHelpers.getObjectField(obj, "view");
        }
    }

    public static class TextInfoCellHolder {
        TextInfoCell text;
        public TextInfoCellHolder(Object obj) {
            text = (TextInfoCell) XposedHelpers.getObjectField(obj, "view");
        }
    }
}
