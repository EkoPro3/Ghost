package akro.ghost.tele.virtuals;

import android.graphics.Color;
import android.text.TextPaint;

import akro.ghost.tele.Class.ClassNames;
import akro.ghost.tele.utils.Utils;
import akro.ghost.tele.Class.ClassLoad;
import akro.ghost.tele.obfuscate.AutomationResolver;
import akro.ghost.tele.logging.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;

/**
 * AkroGhost Theme - Ghost Mode Premium UI
 * Dark navy + electric cyan/ghost-white accent palette
 */
public class Theme {

    // === AkroGhost Color Palette ===
    // Dark backgrounds
    public static final int GHOST_BG_DEEP    = 0xFF0A0E1A; // deep space black-blue
    public static final int GHOST_BG_DARK    = 0xFF111827; // dark navy
    public static final int GHOST_BG_CARD    = 0xFF1A2235; // card surface
    public static final int GHOST_BG_SECTION = 0xFF0D1321; // section divider

    // Toolbar
    public static final int GHOST_TOOLBAR    = 0xFF0F1A2E; // near black-blue
    public static final int GHOST_TOOLBAR_GRADIENT_END = 0xFF162040; // toolbar gradient end

    // Accent / highlight colors
    public static final int GHOST_ACCENT_CYAN    = 0xFF00E5FF; // electric cyan
    public static final int GHOST_ACCENT_BLUE    = 0xFF448AFF; // bright blue
    public static final int GHOST_ACCENT_PURPLE  = 0xFF7C4DFF; // ghost purple
    public static final int GHOST_ACCENT_WHITE   = 0xFFE8F4FF; // ghost white

    // Text
    public static final int GHOST_TEXT_PRIMARY   = 0xFFECF0FF; // primary white
    public static final int GHOST_TEXT_SECONDARY = 0xFF8DA4C4; // muted blue-gray
    public static final int GHOST_TEXT_HEADER    = 0xFF00E5FF; // header cyan

    // Divider
    public static final int GHOST_DIVIDER = 0xFF1E2D45;

    // Light theme fallbacks
    public static final int LIGHT_BG_GRAY   = 0xFFF1F3F9;
    public static final int LIGHT_BG_WHITE  = 0xFFFFFFFF;
    public static final int LIGHT_TOOLBAR   = 0xFFFFFFFF;
    public static final int LIGHT_TEXT      = 0xFF1A1A2E;
    public static final int LIGHT_ACCENT    = 0xFF2979FF;
    public static final int LIGHT_HEADER    = 0xFF1565C0;

    public static TextPaint getTextPaint() {
        Class<?> theme = ClassLoad.getClass(ClassNames.THEME);
        List<Field> fields = new ArrayList<>();
        for (Field declaredField : theme.getDeclaredFields())
            if (declaredField.getName().equals(AutomationResolver.resolve("Theme", "chat_timePaint", AutomationResolver.ResolverType.Field)))
                fields.add(declaredField);

        if (!fields.isEmpty()) {
            try {
                Field textPaintField = null;
                for (Field field : fields) {
                    if (field.getType().equals(TextPaint.class)) textPaintField = field;
                }
                if (textPaintField != null) return (TextPaint) textPaintField.get(null);
                else {
                    for (Field field : fields) {
                        if (field.getType().getName().contains("TextPaint")) textPaintField = field;
                    }
                    if (textPaintField != null) return (TextPaint) textPaintField.get(null);
                    else Logger.w("Not found chat_timePaint field in Theme, " + Utils.issue);
                }
            } catch (IllegalAccessException e) {
                Logger.e(e);
            }
        } else Logger.w("Not found chat_timePaint field in Theme, " + Utils.issue);
        return null;
    }

    public static boolean isLight() {
        return !((boolean) XposedHelpers.callStaticMethod(
                ClassLoad.getClass(ClassNames.THEME),
                AutomationResolver.resolve("Theme", "isCurrentThemeDark", AutomationResolver.ResolverType.Method)));
    }

    // ---- Background ----
    public static int getBackgroundGrayColor() {
        return isLight() ? LIGHT_BG_GRAY : GHOST_BG_DARK;
    }

    public static int getBackgroundWhiteOrBlueColor() {
        return isLight() ? LIGHT_BG_WHITE : GHOST_BG_CARD;
    }

    public static int getBackgroundDeepColor() {
        return isLight() ? LIGHT_BG_GRAY : GHOST_BG_DEEP;
    }

    public static int getCardBackgroundColor() {
        return isLight() ? LIGHT_BG_WHITE : GHOST_BG_CARD;
    }

    public static int getSectionBackgroundColor() {
        return isLight() ? LIGHT_BG_GRAY : GHOST_BG_SECTION;
    }

    // ---- Toolbar ----
    public static int getToolBarColor() {
        return isLight() ? LIGHT_TOOLBAR : GHOST_TOOLBAR;
    }

    public static int getToolBarGradientEndColor() {
        return isLight() ? 0xFFF5F7FF : GHOST_TOOLBAR_GRADIENT_END;
    }

    public static int getToolBarRippleColor() {
        return isLight() ? 0x20000000 : 0x3000E5FF;
    }

    // ---- Text ----
    public static int getTextToolBarColor() {
        return isLight() ? LIGHT_TEXT : GHOST_TEXT_PRIMARY;
    }

    public static int getTextColor() {
        return isLight() ? LIGHT_TEXT : GHOST_TEXT_PRIMARY;
    }

    public static int getTextBlueColor() {
        return isLight() ? LIGHT_ACCENT : GHOST_ACCENT_CYAN;
    }

    public static int getTextGrayColor() {
        return isLight() ? Color.rgb(128, 128, 128) : GHOST_TEXT_SECONDARY;
    }

    public static int getHeaderTextColor() {
        return isLight() ? LIGHT_HEADER : GHOST_TEXT_HEADER;
    }

    // ---- Accent ----
    public static int getAccentCyanColor() {
        return isLight() ? LIGHT_ACCENT : GHOST_ACCENT_CYAN;
    }

    public static int getAccentPurpleColor() {
        return isLight() ? 0xFF7C4DFF : GHOST_ACCENT_PURPLE;
    }

    public static int getDividerColor() {
        return isLight() ? 0xFFE8ECF0 : GHOST_DIVIDER;
    }

    public static int getArrowDrawableColor() {
        return isLight() ? LIGHT_TEXT : GHOST_ACCENT_CYAN;
    }

    // ---- Switch / Toggle ----
    public static int getSwitchTrackColor(boolean checked) {
        if (isLight()) {
            return checked ? 0xFF90CAF9 : 0xFFCFD8DC;
        }
        return checked ? 0x8000E5FF : 0x40607D8B;
    }

    public static int getSwitchThumbColor(boolean checked) {
        if (isLight()) {
            return checked ? LIGHT_ACCENT : 0xFFECEFF1;
        }
        return checked ? GHOST_ACCENT_CYAN : 0xFF455A64;
    }
}
