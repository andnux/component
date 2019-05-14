package top.andnux.libbase.preview;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class PhotoPreviewAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public PhotoPreviewAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
