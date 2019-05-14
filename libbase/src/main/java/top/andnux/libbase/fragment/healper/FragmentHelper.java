package top.andnux.libbase.fragment.healper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentHelper {

    protected FragmentManager mFragmentManager;
    protected int mContainer;

    public FragmentHelper(FragmentManager mFragmentManager, int mContainer) {
        this.mFragmentManager = mFragmentManager;
        this.mContainer = mContainer;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(mContainer,fragment);
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(mContainer,fragment);
        fragmentTransaction.commit();
    }
}
