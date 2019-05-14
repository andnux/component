package top.andnux.libbase.web;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.andnux.libbase.R;
import top.andnux.libbase.annotation.InjectValue;
import top.andnux.libbase.annotation.NetWork;
import top.andnux.libbase.base.BaseActivity;
import top.andnux.libbase.factory.ActivityFactory;
import top.andnux.libbase.factory.FragmentFactory;
import top.andnux.libbase.network.netstate.NetType;
import top.andnux.libbase.preview.PhotoPreviewActivity;
import top.andnux.libbase.utils.CommonUtil;

public class WebActivity extends BaseActivity implements OnWebViewListener,
        AdapterView.OnItemClickListener {

    private BaseWebFragment mBaseWebFragment;
    @InjectValue
    private String url;
    @InjectValue
    protected boolean showTitle = true;
    private ListPopupWindow mPopup;
    private String[] mMoreArray = {"刷新", "复制链接", "退出", "测试"};


    @NetWork(NetType.AUTO)
    private void netChange(NetType netType) {
        switch (netType) {
            case AUTO:
                Toast.makeText(this, "wifi或流量", Toast.LENGTH_LONG).show();
                mBaseWebFragment.reload();
                break;
            case NONE:
                Toast.makeText(this, "无网络", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.a_web);
        onCreated(savedInstanceState);
    }

    @Override
    public void onCreated(@Nullable Bundle savedInstanceState) {
        mBaseWebFragment = FragmentFactory.with(WebFragment.class)
                .addParam("url", url)
                .build();
        if (showTitle) {
            findViewById(R.id.titleBar).setVisibility(View.VISIBLE);
            stepTitle();
        } else {
            findViewById(R.id.titleBar).setVisibility(View.GONE);
        }
        mBaseWebFragment.setWebViewListener(this);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.add(R.id.container, mBaseWebFragment).commit();
    }

    private void stepTitle() {
        mPopup = new ListPopupWindow(this);
        mPopup.setAdapter(new MoreAdapter(this, Arrays.asList(mMoreArray)));
        mPopup.setOnItemClickListener(this);
        mPopup.setWidth(300);
        mPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.setAnchorView(v);
                mPopup.setVerticalOffset(20);
                mPopup.show();
            }
        });
        TextView view = findViewById(R.id.title);
        view.setText(url);
        findViewById(R.id.close).setOnClickListener(v -> this.finish());
        findViewById(R.id.back).setOnClickListener(v -> {
            if (mBaseWebFragment.canGoBack()) {
                mBaseWebFragment.goBack();
            } else {
                this.finish();
            }
        });
    }

    @Override
    public void onReceivedTitle(String title) {
        if (showTitle) {
            findViewById(R.id.titleBar).setVisibility(View.VISIBLE);
            TextView view = findViewById(R.id.title);
            view.setText(title);
        } else {
            findViewById(R.id.titleBar).setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            this.mBaseWebFragment.reload();
            if (mPopup != null && mPopup.isShowing()) {
                mPopup.dismiss();
            }
        } else if (position == 1) {
            CommonUtil.setClipData(this, mBaseWebFragment.getUrl());
            Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
            if (mPopup != null && mPopup.isShowing()) {
                mPopup.dismiss();
            }
        } else if (position == 2) {
            this.finish();
        } else {
            List<String> urls = new ArrayList<>();
            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551704225668&di=fa9f3b5b116ca37fcb7140d9bcfd9e22&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F5601195554177.jpg");
            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551704225668&di=fa9f3b5b116ca37fcb7140d9bcfd9e22&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F5601195554177.jpg");
            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551704225668&di=fa9f3b5b116ca37fcb7140d9bcfd9e22&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F5601195554177.jpg");
            ActivityFactory.with(this, PhotoPreviewActivity.class)
                    .addParam("urls", urls)
                    .addParam("index", 1)
                    .navigation();
        }
    }

}
