package top.andnux.libbase.adapter;


import android.view.View;

public interface ItemViewType<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
