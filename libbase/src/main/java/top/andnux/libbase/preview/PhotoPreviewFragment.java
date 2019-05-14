package top.andnux.libbase.preview;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.github.chrisbanes.photoview.PhotoView;

import top.andnux.libbase.R;
import top.andnux.libbase.annotation.InjectValue;
import top.andnux.libbase.fragment.BaseFragment;
import top.andnux.libbase.image.ImageManager;

public class PhotoPreviewFragment extends BaseFragment {

    @InjectValue
    private String url;
    private PhotoView mPhotoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_photo_preview, container, false);
        mPhotoView = view.findViewById(R.id.photoView);
        return view;
    }

    @Override
    public void onCreated(@Nullable Bundle savedInstanceState) {
        ImageManager.getInstance().display(mPhotoView,url);
    }
}
