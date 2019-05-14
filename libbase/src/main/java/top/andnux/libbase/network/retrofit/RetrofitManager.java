package top.andnux.libbase.network.retrofit;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import top.andnux.libbase.BasicApp;

public class RetrofitManager {

    private static final RetrofitManager ourInstance = new RetrofitManager();
    private final OkHttpClient.Builder mOkHttpClientBuilder;

    public static RetrofitManager getInstance() {
        return ourInstance;
    }

    private String baseUrl;
    private Retrofit mRetrofit;

    private RetrofitManager() {
        Cache cache = new Cache(this.getCacheDir(), 10 * 1024 * 1024);
        mOkHttpClientBuilder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .cache(cache)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS);
    }

    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return mOkHttpClientBuilder;
    }

    private File getCacheDir() {
        return BasicApp.getApplication().getCacheDir();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public RetrofitManager setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mOkHttpClientBuilder.build())
                .baseUrl(baseUrl)
                .build();
        return this;
    }

    public <T> T getService(Class<T> service) {
        return mRetrofit.create(service);
    }
}
