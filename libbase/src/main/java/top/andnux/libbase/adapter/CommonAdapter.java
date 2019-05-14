package top.andnux.libbase.adapter;

import android.content.Context;

import java.util.List;

public abstract class CommonAdapter<T> extends MultiAdapter<T> {

    public CommonAdapter(Context mContext, List<T> datas, int resId) {
        super(mContext, datas);
        addItemViewType(new ItemViewType<T>() {
            @Override
            public int getItemViewLayoutId() {
                return resId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    public CommonAdapter(Context mContext, int resId) {
        super(mContext);
        addItemViewType(new ItemViewType<T>() {
            @Override
            public int getItemViewLayoutId() {
                return resId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    public abstract void convert(ViewHolder holder, T t, int position);
}
