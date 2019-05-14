package top.andnux.libbase.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Method;

public class SkinResource {

    // 资源通过这个对象获取
    private Resources mSkinResource;
    private String mPackageName;
    private Context mContext;
    private static final String TAG = "SkinResource";

    public SkinResource(Context context,String skinPath) {
        try {
            this.mContext = context;
            Resources superRes = context.getResources();
            AssetManager asset = AssetManager.class.newInstance();
            Method method = asset.getClass().getDeclaredMethod("addAssetPath", String.class);
            method.invoke(asset, skinPath);
            mSkinResource = new Resources(asset, superRes.getDisplayMetrics(),
                    superRes.getConfiguration());
            mPackageName = context.getPackageManager().getPackageArchiveInfo(
                    skinPath, PackageManager.GET_ACTIVITIES).packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过名字获取Drawable
     * @param resName
     * @return
     */
    public Drawable getDrawableByName(String resName){
        try {
            int resId = mSkinResource.getIdentifier(resName, "drawable", mPackageName);
            if (resId == 0){
                resId = mSkinResource.getIdentifier(resName, "mipmap", mPackageName);
            }
            return  mSkinResource.getDrawable(resId);
        }catch (Exception e){
            int resId = mSkinResource.getIdentifier(resName, "drawable", mContext.getPackageName());
            if (resId == 0){
                resId = mSkinResource.getIdentifier(resName, "mipmap", mContext.getPackageName());
            }
            return mSkinResource.getDrawable(resId);
        }
    }

    /**
     * 通过名字获取颜色
     * @param resName
     * @return
     */
    public ColorStateList getColorByName(String resName){
        try {
            int resId = mSkinResource.getIdentifier(resName, "color", mPackageName);
            return  mSkinResource.getColorStateList(resId);
        }catch (Exception e){
            int resId = mSkinResource.getIdentifier(resName, "color", mContext.getPackageName());
            return  mSkinResource.getColorStateList(resId);
        }
    }
}
