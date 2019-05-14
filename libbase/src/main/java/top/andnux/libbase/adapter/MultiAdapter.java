package top.andnux.libbase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.andnux.libbase.adapter.ItemViewType;

public abstract class MultiAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    protected List<T> mDatas;
    private SparseArrayCompat<ItemViewType<T>> delegates = new SparseArrayCompat<ItemViewType<T>>();


    public MultiAdapter(Context mContext, List<T> datas) {
        this.mContext = mContext;
        this.mDatas = datas;
        if (this.mDatas == null) {
            this.mDatas = new ArrayList<>();
        }
    }

    public MultiAdapter(Context mContext) {
        this.mContext = mContext;
        this.mDatas = new ArrayList<>();
    }

    protected void addItemViewType(ItemViewType<T> type) {
        int viewType = delegates.size();
        if (type != null) {
            delegates.put(viewType, type);
        }
    }


    @Override
    public int getItemViewType(int position) {
        int delegatesCount = delegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemViewType<T> delegate = delegates.valueAt(i);
            if (delegate.isForViewType(mDatas.get(position), position)) {
                return delegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=" + position + " in data source");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int delegatesCount = delegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemViewType<T> delegate = delegates.valueAt(i);
            int itemViewLayoutId = delegate.getItemViewLayoutId();
            return ViewHolder.createViewHolder(mContext, parent, itemViewLayoutId);
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=" + viewType + " in data source");

    }

    private void convert(ViewHolder holder, T item, int position) {
        int delegatesCount = delegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            ItemViewType<T> delegate = delegates.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                delegate.convert(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=" + position + " in data source");

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public void addData(T data) {
        this.mDatas.add(data);
        notifyItemInserted(this.mDatas.size() - 1);
    }

    public void addDatas(List<T> data) {
        this.mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public T removeData(int position) {
        T remove = this.mDatas.remove(position);
        notifyItemRemoved(position);
        return remove;
    }

    public T removeData(T data) {
        T remove = null;
        for (int i = 0; i < this.mDatas.size(); i++) {
            T t = this.mDatas.get(i);
            if (t.equals(data)) {
                remove = this.mDatas.remove(i);
                notifyItemRemoved(i);
            }
        }
        return remove;
    }

    public void clear() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }

}
