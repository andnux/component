package top.andnux.libbase.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.json.JSONObject;
import top.andnux.libbase.annotation.InjectLayout;
import top.andnux.libbase.annotation.InjectValue;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public final class InjectUtil {

    public static boolean injectActivityLayout(Activity object) {
        InjectLayout annotation = object.getClass().getAnnotation(InjectLayout.class);
        if (annotation != null) {
            int value = annotation.value();
            object.setContentView(value);
            return true;
        }
        return false;
    }

    public static View injectFragmentLayout(Fragment object, LayoutInflater inflater, ViewGroup container) {
        InjectLayout annotation = object.getClass().getAnnotation(InjectLayout.class);
        if (annotation != null) {
            int value = annotation.value();
            return LayoutInflater.from(object.getContext()).inflate(value, null);
        }
        return null;
    }

    public static void injectFragmentValue(Fragment object, Bundle bundle) throws Exception {
        List<Field> fields = ReflectUtil.getDeclaredFields(object.getClass());
        for (Field field : fields) {
            if (field == null) {
                continue;
            }
            field.setAccessible(true);
            InjectValue annotation = field.getAnnotation(InjectValue.class);
            if (annotation != null) {
                Log.e("TAG", "field ->" + field.getName());
                String value = annotation.value();
                if (TextUtils.isEmpty(value)) {
                    value = field.getName();
                }
                String data = bundle.getString("data");
                Gson gson = new Gson();
                Map map = gson.fromJson(data, Map.class);
                if (map != null) {
                    Object objectValue = map.get(value);
                    if (objectValue != null) {
                        if (objectValue.getClass().getName().endsWith("Map")) {
                            Object o = gson.fromJson(gson.toJson(objectValue), field.getType());
                            field.set(object, o);
                        } else {
                            field.set(object, objectValue);
                        }
                    }
                }
            }
        }
    }

    public static void injectActivityValue(Activity object, Intent intent) throws Exception {
        List<Field> fields = ReflectUtil.getDeclaredFields(object.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            InjectValue annotation = field.getAnnotation(InjectValue.class);
            if (annotation != null) {
                String value = annotation.value();
                if (TextUtils.isEmpty(value)) {
                    value = field.getName();
                }
                String data = intent.getStringExtra("data");
                Gson gson = new Gson();
                Map map = gson.fromJson(data, Map.class);
                if (map != null) {
                    Object objectValue = map.get(value);
                    if (objectValue != null) {
                        if (objectValue.getClass().getName().endsWith("Map")) {
                            Object o = gson.fromJson(gson.toJson(objectValue), field.getType());
                            field.set(object, o);
                        } else {
                            field.set(object, objectValue);
                        }
                    }
                }
            }
        }

    }

    public static void injectServiceValue(Service object, Intent intent) throws Exception {
        List<Field> fields = ReflectUtil.getDeclaredFields(object.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            InjectValue annotation = field.getAnnotation(InjectValue.class);
            if (annotation != null) {
                String value = annotation.value();
                if (TextUtils.isEmpty(value)) {
                    value = field.getName();
                }
                Class<?> type = field.getType();
                String data = intent.getStringExtra("data");
                Gson gson = new Gson();
                Map map = gson.fromJson(data, Map.class);
                if (map != null) {
                    Object objectValue = map.get(value);
                    if (objectValue != null) {
                        if (objectValue.getClass().getName().endsWith("Map")) {
                            Object o = gson.fromJson(gson.toJson(objectValue), field.getType());
                            field.set(object, o);
                        } else {
                            field.set(object, objectValue);
                        }
                    }
                }
            }
        }

    }
}
