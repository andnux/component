package top.andnux.compat;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;

public class ToastCompat {

    public static void showLongText(Context context, @StringRes int resId) {
        showShortText(context, resId, -1);
    }

    public static void showLongText(Context context, @StringRes int resId,
                                    int gravity) {
        showLongText(context, resId, gravity, 0, 0);
    }

    public static void showLongText(Context context, @StringRes int resId,
                                    int gravity, int xOffset, int yOffset) {
        showLongText(context, resId, gravity, xOffset, yOffset, null);
    }

    public static void showLongText(Context context, @StringRes int resId,
                                    int gravity, int xOffset, int yOffset,
                                    View view) {
        showText(context, resId, gravity, xOffset, yOffset, view, Toast.LENGTH_SHORT);
    }

    public static void showLongText(Context context, CharSequence s) {
        showLongText(context, s, -1);
    }

    public static void showLongText(Context context, CharSequence s,
                                    int gravity) {
        showLongText(context, s, gravity, 0, 0);
    }

    public static void showLongText(Context context, CharSequence s,
                                    int gravity, int xOffset, int yOffset) {
        showLongText(context, s, gravity, xOffset, yOffset, null);
    }

    public static void showLongText(Context context, CharSequence s,
                                    int gravity, int xOffset, int yOffset,
                                    View view) {
        showText(context, s, gravity, xOffset, yOffset, view, Toast.LENGTH_LONG);
    }

    public static void showShortText(Context context, @StringRes int resId) {
        showShortText(context, resId, -1);
    }

    public static void showShortText(Context context, @StringRes int resId,
                                     int gravity) {
        showShortText(context, resId, gravity, 0, 0);
    }

    public static void showShortText(Context context, @StringRes int resId,
                                     int gravity, int xOffset, int yOffset) {
        showShortText(context, resId, gravity, xOffset, yOffset, null);
    }

    public static void showShortText(Context context, @StringRes int resId,
                                     int gravity, int xOffset, int yOffset,
                                     View view) {
        showText(context, resId, gravity, xOffset, yOffset, view, Toast.LENGTH_SHORT);
    }

    public static void showShortText(Context context, CharSequence s) {
        showShortText(context, s, -1);
    }

    public static void showShortText(Context context, CharSequence s,
                                     int gravity) {
        showShortText(context, s, gravity, 0, 0);
    }

    public static void showShortText(Context context, CharSequence s,
                                     int gravity, int xOffset, int yOffset) {
        showShortText(context, s, gravity, xOffset, yOffset, null);
    }

    public static void showShortText(Context context, CharSequence s,
                                     int gravity, int xOffset, int yOffset,
                                     View view) {
        showText(context, s, gravity, xOffset, yOffset, view, Toast.LENGTH_SHORT);
    }

    public static void showText(Context context, CharSequence s,
                                int gravity, int xOffset, int yOffset,
                                View view, int duration) {
        Toast toast = Toast.makeText(context, null, duration);
        if (view != null) {
            toast.setView(view);
        } else {
            toast.setText(s);
        }
        if (gravity != -1) {
            toast.setGravity(gravity, xOffset, yOffset);
        }
        toast.show();
    }

    public static void showText(Context context, @StringRes int resId,
                                int gravity, int xOffset, int yOffset,
                                View view, int duration) {
        Toast toast = Toast.makeText(context, null, duration);
        if (view != null) {
            toast.setView(view);
        } else {
            toast.setText(resId);
        }
        if (gravity != -1) {
            toast.setGravity(gravity, xOffset, yOffset);
        }
        toast.show();
    }
}
