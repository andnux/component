package top.andnux.libbase.downloader.service;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;

import top.andnux.libbase.annotation.InjectValue;
import top.andnux.libbase.downloader.bean.FileBean;
import top.andnux.libbase.downloader.constant.Constant;
import top.andnux.libbase.manager.ThreadManager;
import top.andnux.libbase.service.BaseService;

public class DownloadService extends BaseService {

    @InjectValue
    private FileBean fileBean;
    private File downloadPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS);

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    FileBean fileBean = (FileBean) msg.obj;
                    Log.e("TAG", fileBean.toString());
                    break;
            }
        }
    };

    @Override
    public void onStarted(Intent intent) {
        if (Constant.ACTION_START.equals(intent.getAction())) {
            Log.e("TAG", fileBean.toString());
            top.andnux.libbase.downloader.service.InitThread initThread = new top.andnux.libbase.downloader.service.InitThread(fileBean, downloadPath, handler);
            ThreadManager.getInstance().execute(initThread);
        } else if (Constant.ACTION_STOP.equals(intent.getAction())) {
            Log.e("TAG", fileBean.toString());
        }
    }


}
