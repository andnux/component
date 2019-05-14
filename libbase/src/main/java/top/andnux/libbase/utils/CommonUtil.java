package top.andnux.libbase.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public final class CommonUtil {

    public float density;

    public CommonUtil() {
        density = Resources.getSystem().getDisplayMetrics().density;
    }

    public static void setClipData(Context context, String data) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", data);
        manager.setPrimaryClip(myClip);
    }

    public static String getClipData(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = manager.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            ClipData.Item itemAt = clip.getItemAt(0);
            CharSequence text = itemAt.getText();
            return text.toString();
        }
        return "";
    }

    /**
     * 获取屏幕宽度 px(像素)
     *
     * @param context 上下文
     * @return 像素
     */
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度 px(像素)
     *
     * @param context 上下文
     * @return 像素
     */
    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 虚拟像素
     * @return 像素
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue 像素
     * @return 虚拟像素
     */
    public static float px2dp(int pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 虚拟像素
     * @return 像素
     */
    public int dip2px(float dpValue) {
        return (int) (0.5f + dpValue * density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue 像素
     * @return 虚拟像素
     */
    public float px2dip(int pxValue) {
        return (pxValue / density);
    }
}
