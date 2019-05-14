package top.andnux.libbase.downloader.service;

import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import top.andnux.libbase.downloader.bean.FileBean;
import top.andnux.libbase.utils.IOUtil;

public class InitThread implements Runnable {

    private FileBean fileBean;
    private File downloadPath;
    private Handler handler;

    public InitThread(FileBean fileBean, File downloadPath, Handler handler) {
        this.fileBean = fileBean;
        this.downloadPath = downloadPath;
        this.handler = handler;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        RandomAccessFile accessFile = null;
        try {
            URL url = new URL(fileBean.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            conn.connect();
            int contentLength = 0;
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                contentLength = conn.getContentLength();
            }
            if (contentLength <= 0) {
                return;
            }
            Log.e("TAG", "contentLength ->" + contentLength);
            File file = new File(downloadPath, fileBean.getFileName());
            accessFile = new RandomAccessFile(file, "rwd");
            accessFile.setLength(contentLength);
            fileBean.setLength(contentLength);
            handler.obtainMessage(1, fileBean).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(accessFile);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
