package akro.ghost.tele.features.otherFeatures;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.base.AbstractMethodHook;
import akro.ghost.tele.hooks.HMethod;
import akro.ghost.tele.language.Keys;
import akro.ghost.tele.language.Translator;
import akro.ghost.tele.logging.Logger;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.virtuals.ActionBar.SimpleTextView;
import akro.ghost.tele.virtuals.ui.ProfileActivity;

public class CopyNameHook {

    public static void init(Context context) {
        try {
            if (ClassLoad.getClass(ClassNames.PROFILE_ACTIVITY) != null) {

                HMethod.hookMethod(ClassLoad.getClass(ClassNames.PROFILE_ACTIVITY), AutomationResolver.resolve("ProfileActivity", "createView", AutomationResolver.ResolverType.Method), AutomationResolver.merge(AutomationResolver.resolveObject("createView", new Class[]{Context.class}), new AbstractMethodHook() {
                    @Override
                    protected void afterMethod(MethodHookParam param) {
                        final ProfileActivity profileActivity = new ProfileActivity(param.thisObject);

                        Object[] nameTextViewArray = profileActivity.getNameTextView();

                        if (nameTextViewArray != null && nameTextViewArray.length > 1) {

                            SimpleTextView simpleTextView = new SimpleTextView(nameTextViewArray[1]);

                            if (simpleTextView.getSimpleTextView() != null) {
                                simpleTextView.getSimpleTextView().setOnClickListener(v -> {
                                    if (simpleTextView.getText() != null) {
                                        String name = Translator.get(Keys.Copied, simpleTextView.getText());
                                        ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", simpleTextView.getText()));
                                        Toast.makeText(context, name, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                }));
            }
        } catch (Throwable t) {
            Logger.e(t);
        }
    }
}
