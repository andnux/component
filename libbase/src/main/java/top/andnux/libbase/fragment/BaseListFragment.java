package top.andnux.libbase.fragment;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import top.andnux.libbase.adapter.CommonAdapter;
import top.andnux.libbase.adapter.MultiAdapter;
import top.andnux.libbase.adapter.ViewHolder;

public abstract class BaseListFragment<T> extends BaseMultiListFragment<T> {


    public abstract int getItemId();

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this.getContext());
    }

    public abstract void convert(@NonNull ViewHolder holder, @NonNull T t, @NonNull int position);

    @Override
    public MultiAdapter<T> getAdapter() {
        return new CommonAdapter<T>(getContext(), getItemId()) {
            @Override
            public void convert(ViewHolder holder, T t, int position) {
                BaseListFragment.this.convert(holder, t, position);
            }
        };
    }
}
