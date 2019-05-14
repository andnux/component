package top.andnux.libbase.web;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.just.agentweb.AgentWeb;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import top.andnux.libbase.R;
import top.andnux.libbase.annotation.InjectValue;
import top.andnux.libbase.fragment.BaseFragment;

public abstract class BaseWebFragment extends BaseFragment
        implements OnRefreshListener, View.OnKeyListener {

    private AgentWeb mAgentWeb;
    private SmartRefreshLayout mRefreshLayout;
    private BaseNativeInterface nativeInterface;
    @InjectValue
    private String url;
    private WebView mWebView;
    private LinearLayout mContainer;
    private OnWebViewListener mOnWebViewListener;

    public OnWebViewListener getWebViewListener() {
        return mOnWebViewListener;
    }

    public void setWebViewListener(OnWebViewListener webViewListener) {
        mOnWebViewListener = webViewListener;
    }

    public String getUrl() {
        return mWebView.getUrl();
    }

    public void reload() {
        mWebView.reload();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_web, container, false);
        mContainer = view.findViewById(R.id.container);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);
//        mRefreshLayout.autoRefresh();
        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mContainer, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .closeIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .createAgentWeb()
                .ready()
                .go(url);
        mWebView = mAgentWeb.getWebCreator().getWebView();
        mWebView.setOnKeyListener(this);
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mRefreshLayout.finishRefresh();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mOnWebViewListener != null) {
                mOnWebViewListener.onReceivedTitle(title);
            }
        }
    };

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mWebView.reload();
    }


    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    public void goBack() {
        mWebView.goBack();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }


    public BaseNativeInterface getNativeInterface() {
        return nativeInterface;
    }

    public void setNativeInterface(BaseNativeInterface nativeInterface) {
        this.nativeInterface = nativeInterface;
        if (this.nativeInterface != null) {
            mWebView.addJavascriptInterface(this.nativeInterface, "android");
        }
    }
}
