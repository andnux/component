package top.andnux.libbase.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import top.andnux.libbase.adapter.MultiAdapter;
import top.andnux.libbase.R;
public abstract class BaseMultiListFragment<T> extends BaseFragment
        implements OnRefreshListener, OnLoadMoreListener {

    protected RecyclerView mRecyclerView;
    protected RefreshLayout mRefreshLayout;
    protected MultiAdapter<T> adapter;

    public abstract RecyclerView.LayoutManager getLayoutManager();

    public abstract MultiAdapter<T> getAdapter();

    @Override
    public void onCreated(@Nullable Bundle savedInstanceState) {
        adapter = getAdapter();
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_list, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        return view;
    }
}
