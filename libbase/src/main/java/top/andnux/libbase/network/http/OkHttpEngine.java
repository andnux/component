package top.andnux.libbase.network.http;

import android.content.Context;
import android.text.TextUtils;


import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpEngine implements IHttpEngine {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void post(boolean cache,
                     boolean loading,
                     Context context,
                     String url,
                     Map<String, Object> params,
                     Map<String, String> headers,
                     final EngineCallBack callBack) {

        final String jointUrl = SupportUtil.jointParams(url, params);
        RequestBody requestBody = appendBody(params);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody);
        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                if (value != null) {
                    requestBuilder.addHeader(key, value);
                }
            }
        }
        mOkHttpClient.newCall(requestBuilder.build()).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (callBack != null) {
                            callBack.onError(e);
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream result = response.body().byteStream();
                        if (callBack != null) {
                            callBack.onSuccess(result);
                        }
                    }
                }
        );
    }

    /**
     * 组装post请求参数body
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    // 添加参数
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file
                                    .getAbsolutePath())), file));
                } else if (value instanceof List) {
                    // 代表提交的是 List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 猜测文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @Override
    public void get(final boolean cache,
                    boolean loading,
                    Context context,
                    String url,
                    Map<String, Object> params,
                    Map<String, String> headers,
                    final EngineCallBack callBack) {
        final String finalUrl = SupportUtil.jointParams(url, params);
        if (cache) {
            String resultJson = CacheDataUtil.getCacheResultJson(finalUrl);
            if (!TextUtils.isEmpty(resultJson)) {
                // 需要缓存，而且数据库有缓存,直接就去执行，里面执行成功
                byte[] bytes = resultJson.getBytes(Charset.forName("utf-8"));
                InputStream inputStream = new ByteArrayInputStream(bytes);
                if (callBack != null) {
                    callBack.onSuccess(inputStream);
                }
            }
        }

        Request.Builder requestBuilder = new Request.Builder().url(finalUrl).tag(context);
        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                if (value != null) {
                    requestBuilder.addHeader(key, value);
                }
            }
        }
        Request request = requestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callBack != null) {
                    callBack.onError(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultJson = response.body().string();
                if (cache) {
                    String cacheResultJson = CacheDataUtil.getCacheResultJson(finalUrl);
                    if (!TextUtils.isEmpty(resultJson)) {
                        if (resultJson.equals(cacheResultJson)) {
                            return;
                        }
                    }
                }
                byte[] bytes = resultJson.getBytes(Charset.forName("utf-8"));
                InputStream inputStream = new ByteArrayInputStream(bytes);
                if (callBack != null) {
                    callBack.onSuccess(inputStream);
                }
                if (cache) {
                    // 2.3 缓存数据
                    CacheDataUtil.cacheData(finalUrl, resultJson);
                }
            }
        });
    }
}
