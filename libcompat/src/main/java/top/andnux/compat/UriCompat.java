package top.andnux.compat;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class UriCompat {

    public static String decode(String s) {
        if (s == null) {
            return null;
        }
        return Uri.decode(s);
    }

    public static String encode(String s) {
        return Uri.encode(s);
    }

    public static String encode(String s, String allow) {
        return Uri.encode(s, allow);
    }

    public static Uri fromParts(String scheme, String ssp,
                                String fragment) {
        return Uri.fromParts(scheme, ssp, fragment);
    }

    public static Uri parse(String uriString) {
        return Uri.parse(uriString);
    }

    public static Uri fromFile(Context context, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = context.getPackageName() + ".fileProvider";
            uri = FileProvider.getUriForFile(context, authority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
