package top.andnux.libbase.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import top.andnux.libbase.R;

public abstract class BaseListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_list);

    }
}
