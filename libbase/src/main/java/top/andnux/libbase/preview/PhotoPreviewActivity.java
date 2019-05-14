package top.andnux.libbase.preview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import top.andnux.libbase.R;
import top.andnux.libbase.annotation.InjectValue;
import top.andnux.libbase.base.BaseActivity;
import top.andnux.libbase.factory.FragmentFactory;

public class PhotoPreviewActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "PhotoPreviewActivity";

    private List<Fragment> previewFragments;
    private PhotoPreviewAdapter adapter;
    @InjectValue
    private int index;
    @InjectValue
    private List<String> urls;
    private ViewPager mViewPager;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_photo_preview);
        mViewPager = findViewById(R.id.viewPager);
        mText = findViewById(R.id.text);
        findViewById(R.id.close).setOnClickListener(v -> this.finish());
        onCreated(savedInstanceState);
    }

    @Override
    public void onCreated(@Nullable Bundle savedInstanceState) {
        previewFragments = new ArrayList<>();
        for (String s : urls) {
            PhotoPreviewFragment fragment = FragmentFactory.with(PhotoPreviewFragment.class)
                    .addParam("url", s).build();
            previewFragments.add(fragment);
        }
        change(index);
        adapter = new PhotoPreviewAdapter(getSupportFragmentManager(), previewFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(index);
    }

    private void change(int position) {
        if (urls.size() > 1) {
            mText.setVisibility(View.VISIBLE);
            mText.setText(position + "/" + urls.size());
        } else {
            mText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled() called with: position = [" + position + "], positionOffset = [" + positionOffset + "], positionOffsetPixels = [" + positionOffsetPixels + "]");
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected() called with: position = [" + position + "]");
        change(position + 1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "onPageScrollStateChanged() called with: state = [" + state + "]");
    }
}
