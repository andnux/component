package top.andnux.libbase.fragment.healper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class TabFragmentHelper extends FragmentHelper {

    public TabFragmentHelper(FragmentManager mFragmentManager, int mContainer) {
        super(mFragmentManager, mContainer);
    }

    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        for (Fragment it : fragments) {
            fragmentTransaction.hide(it);
        }
        if (fragments.contains(fragment)) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(mContainer, fragment);
        }
        fragmentTransaction.commit();
    }
}
