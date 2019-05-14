package top.andnux.libpay.wxpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.openapi.IWXAPI;

public class PayEntryActivity extends Activity {

	private IWXAPI api = WxManager.getInstance().getApi();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		WxManager.getInstance().setActivity(this);
		api.handleIntent(getIntent(), WxManager.getInstance());
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		WxManager.getInstance().setActivity(this);
        api.handleIntent(intent, WxManager.getInstance());
	}
}