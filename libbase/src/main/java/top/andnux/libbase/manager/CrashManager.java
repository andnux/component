package top.andnux.libbase.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CrashManager implements Thread.UncaughtExceptionHandler {

    private static CrashManager mInstance;

    private static final String TAG = "CrashManager";
    // 获取系统默认的
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public static CrashManager getInstance() {
        if (mInstance == null) {
            // 用来解决多并发的问题
            synchronized (CrashManager.class) {
                if (mInstance == null) {
                    mInstance = new CrashManager();
                }
            }
        }
        return mInstance;
    }

    // 用来获取应用的一些信息
    private Context mContext;

    public void init(Context context) {
        this.mContext = context;
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mDefaultExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "报异常了~");
        String crashFileName = saveInfoToSD(ex);
        cacheCrashFile(crashFileName);
        mDefaultExceptionHandler.uncaughtException(thread, ex);
    }

    private void cacheCrashFile(String fileName) {
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        sp.edit().putString("file", fileName).apply();
    }

    public File getCrashFile() {
        String crashFileName = mContext.getSharedPreferences("crash",
                Context.MODE_PRIVATE).getString("file", "");
        return new File(crashFileName);
    }

    private String saveInfoToSD(Throwable ex) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext)
                .entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }
        sb.append(obtainExceptionInfo(ex));
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            File dir = new File(mContext.getFilesDir() + File.separator + "crash" + File.separator);
            if (dir.exists()) {
                deleteDir(dir);
            }
            if (!dir.exists()) {
                dir.mkdir();
            }
            try {
                fileName = dir.toString()
                        + File.separator
                        + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    private String getAssignTime(String dateFormatStr) {
        DateFormat dataFormat = new SimpleDateFormat(dateFormatStr, Locale.CHINA);
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(currentTime);
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     *
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MOBLE_INFO", getMobileInfo());
        return map;
    }

    public static String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            // 利用反射获取 Build 的所有属性
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    private String obtainExceptionInfo(Throwable throwable) {
        // Java基础 异常
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (File child : children) {
                child.delete();
            }
        }
        return true;
    }
}
