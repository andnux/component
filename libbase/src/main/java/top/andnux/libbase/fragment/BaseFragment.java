package top.andnux.libbase.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pub.devrel.easypermissions.EasyPermissions;
import top.andnux.libbase.utils.InjectUtil;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    private EasyPermissions.PermissionCallbacks mCallbacks;

    public abstract void onCreated(@Nullable Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = InjectUtil.injectFragmentLayout(this, inflater, container);
        if (view != null) {
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        try {
            InjectUtil.injectFragmentValue(this, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onCreated(savedInstanceState);
    }
}
