package top.andnux.libpay;

public interface PayListener {

    void success(String ... message);

    void failure(String ... message);
}
