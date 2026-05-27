package akro.ghost.tele.virtuals.TeleVip.Bridge;


import android.content.Context;
import android.util.TypedValue;
import android.view.View;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.dex.DexInjector;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.settings.ui.SettingsAdapter;
import akro.ghost.tele.settings.controller.SettingsController;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.virtuals.Theme;
import akro.ghost.tele.virtuals.ui.Cells.HeaderCell;
import akro.ghost.tele.virtuals.ui.Cells.ShadowSectionCell;
import akro.ghost.tele.virtuals.ui.Cells.TextCheckCell;
import akro.ghost.tele.virtuals.ui.Cells.TextInfoCell;
import akro.ghost.tele.virtuals.ui.Cells.TextSettingsCell;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Bridge {

    public static Object getLayoutManager(Context context){
        return XposedHelpers.callStaticMethod(ClassLoad.getClass(ClassNames.SETTINGS_ADAPTER, DexInjector.classLoader), "getLayoutManager", context);
    }

    public static void init(SettingsController settingsController){
        try {
            Class<?> bridgeClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.Bridge", DexInjector.classLoader);
            Class<?> textCheckCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter$TextCheckCellHolder", DexInjector.classLoader);
            Class<?> textSettingsCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter$TextSettingsCellHolder", DexInjector.classLoader);
            Class<?> headerCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter$HeaderCellHolder", DexInjector.classLoader);
            Class<?> shadowSectionCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter$ShadowSectionCellHolder", DexInjector.classLoader);
            Class<?> textInfoCellClass = XposedHelpers.findClassIfExists("com.televip.SettingsAdapter.SettingsAdapter$TextInfoCellHolder", DexInjector.classLoader);

            HMethod.hookMethod(bridgeClass, "getRow", int.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(XC_MethodHook.MethodHookParam param) {
                    param.setResult(SettingsAdapter.getRow((int) param.args[0]));
                }
            });

            HMethod.hookMethod(bridgeClass, "log", String.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(XC_MethodHook.MethodHookParam param) {
                    Logger.l((String) param.args[0]);
                }
            });

            HMethod.hookMethod(bridgeClass, "getRowCount", new AbstractMethodHook() {
                @Override
                protected void beforeMethod(XC_MethodHook.MethodHookParam param) {
                    param.setResult(SettingsAdapter.getRowCount());
                }
            });
            
            HMethod.hookMethod(bridgeClass, "onBindViewHolder", Object.class, int.class, int.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(XC_MethodHook.MethodHookParam param) {
                   SettingsAdapter.onBindViewHolder(param.args[0], settingsController, (int) param.args[1], (int) param.args[2]);
                }
            });


            TypedValue outValue = new TypedValue();
            settingsController.getContext().getTheme().resolveAttribute(
                    android.R.attr.selectableItemBackground,
                    outValue,
                    true
            );
            
            XposedHelpers.findAndHookConstructor(textCheckCellClass, View.class, Object.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(XC_MethodHook.MethodHookParam param) {
                    TextCheckCell textCheckCell = new TextCheckCell(settingsController.getContext());
                    textCheckCell.getView().setBackgroundColor(Theme.getBackgroundWhiteOrBlueColor());

                    textCheckCell.getView().setBackgroundResource(outValue.resourceId);
                    textCheckCell.getView().setClickable(true);
                    textCheckCell.getView().setFocusable(true);
                    param.args[0] = textCheckCell.getView();
                    param.args[1] = textCheckCell.textCell;
                }
            });

            XposedHelpers.findAndHookConstructor(textSettingsCellClass, View.class, Object.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(XC_MethodHook.MethodHookParam param) {
                    TextSettingsCell textSettingsCell = new TextSettingsCell(settingsController.getContext());
                    textSettingsCell.getView().setBackgroundColor(Theme.getBackgroundWhiteOrBlueColor());
                    textSettingsCell.getView().setBackgroundResource(outValue.resourceId);
                    textSettingsCell.getView().setClickable(true);
                    textSettingsCell.getView().setFocusable(true);
                    param.args[0] = textSettingsCell.getView();
                    param.args[1] = textSettingsCell.textSettingsCell;
                }
            });

            XposedHelpers.findAndHookConstructor(headerCellClass, View.class, Object.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(XC_MethodHook.MethodHookParam param) {
                    HeaderCell header = new HeaderCell(settingsController.getContext());
                    header.getView().setBackgroundColor(Theme.getBackgroundWhiteOrBlueColor());
                    param.args[0] = header.getView();
                    param.args[1] = header.headerCell;
                }
            });

            XposedHelpers.findAndHookConstructor(shadowSectionCellClass, View.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(XC_MethodHook.MethodHookParam param) {
                    param.args[0] = new ShadowSectionCell(settingsController.getContext()).getView();
                }
            });

            XposedHelpers.findAndHookConstructor(textInfoCellClass, View.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(XC_MethodHook.MethodHookParam param) {
                    TextInfoCell textInfoCell = new TextInfoCell(settingsController.getContext());
                    textInfoCell.setBackgroundColor(Theme.getBackgroundGrayColor());
                    param.args[0] = textInfoCell;
                }
            });

        } catch (Throwable e){
            Logger.e(e);
        }
    }
}
