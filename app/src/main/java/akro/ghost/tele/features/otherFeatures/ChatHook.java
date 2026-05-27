package akro.ghost.tele.features.otherFeatures;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.ClientChecker;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.language.Keys;
import akro.ghost.tele.language.Translator;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.ActionBar.ActionBarMenuItem;
import akro.ghost.tele.virtuals.ActionBar.AlertDialog;
import akro.ghost.tele.virtuals.Theme;
import akro.ghost.tele.virtuals.ui.ChatActivity;

import de.robv.android.xposed.XposedHelpers;

public class ChatHook {

    private static boolean initialized = false;

    public static void init(Context context, String className) {
        if (initialized || ClientChecker.check(ClientChecker.ClientType.Nagram)) return;

        Class<?> clazz = ClassLoad.getClass(className);
        if (clazz == null) FeatureStateManager.reset(context);
        try {
            initialized = true;
            HMethod.hookMethod(ClassLoad.getClass(ClassNames.CHAT_ACTIVITY), AutomationResolver.resolve("ChatActivity", "createView", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("createView", new Class[]{Context.class}), new AbstractMethodHook() {
                @Override
                protected void afterMethod(MethodHookParam param) {
                    try {
                        ChatActivity chatActivity = new ChatActivity(param.thisObject);

                        ActionBarMenuItem headerItem = chatActivity.getHeaderItem();
                        if (headerItem.getActionBarMenuItem() != null) {

                            int drawableResource = XposedHelpers.getStaticIntField(ClassLoad.getClass(ClassNames.DRAWABLE), "msg_go_up");

                            if (!ClientChecker.check(ClientChecker.ClientType.iMe) && !ClientChecker.check(ClientChecker.ClientType.iMeWeb) && !ClientChecker.check(ClientChecker.ClientType.TelegramPlus) && !ClientChecker.check(ClientChecker.ClientType.XPlus) && !ClientChecker.check(ClientChecker.ClientType.forkgram) && !ClientChecker.check(ClientChecker.ClientType.forkgramBeta)) {
                                headerItem.lazilyAddSubItem(8353847, drawableResource, Translator.get(Keys.ToTheBeginning));
                            }
                            drawableResource = XposedHelpers.getStaticIntField(ClassLoad.getClass(ClassNames.DRAWABLE), "player_new_order");

                            headerItem.lazilyAddSubItem(8353848, drawableResource, Translator.get(Keys.ToTheMessage));

                        }
                    } catch (Throwable t){
                        Logger.e(t);
                    }

                }
            }));

            XposedHelpers.findAndHookMethod(clazz, "onItemClick", int.class, new AbstractMethodHook() {
                @Override
                protected void afterMethod(MethodHookParam param) {
                    try {
                        int id = (int) param.args[0];

                        final Object thisClass = XposedHelpers.getObjectField(param.thisObject, AutomationResolver.resolve("ChatActivity", "this$0", AutomationResolver.ResolverType.Field));
                        ChatActivity chat = new ChatActivity(thisClass);

                        if (id == 8353847) {
                            chat.scrollToMessageId(1, 0, true, 0, true, 0);
                        } else if (id == 8353848) {

                            AlertDialog dialog = new AlertDialog(context);
                            dialog.setTitle(Translator.get(Keys.InputMessageId));

                            EditText input = new EditText(context);
                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                            if (Theme.isLight()) {
                                input.setTextColor(0xFF000000);
                                input.setHintTextColor(0xFF424242);
                            } else {
                                input.setTextColor(0xFFFFFFFF);
                                input.setHintTextColor(0xFFBDBDBD);
                            }
                            input.setTextSize(18);
                            input.setPadding(20, 20, 20, 20);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(20, 20, 20, 20);
                            input.setLayoutParams(params);

                            LinearLayout layout = new LinearLayout(context);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.addView(input);

                            dialog.setView(layout);

                            dialog.setPositiveButton(Translator.get(Keys.Done), AlertDialog.click(() -> {
                                String text = input.getText().toString().trim();
                                if (!text.isEmpty()) {
                                    int msgId = Integer.parseInt(text);
                                    chat.scrollToMessageId(msgId, 0, true, 0, true, 0);
                                }
                            }));

                            dialog.show();
                        }
                    } catch (Throwable t) {
                        Logger.e(t);
                    }
                }
            });
        } catch (Throwable t){
            FeatureStateManager.reset(context);
        }
    }
}