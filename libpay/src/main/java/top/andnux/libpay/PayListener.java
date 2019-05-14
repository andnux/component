package top.andnux.libpay;

public interface PayListener {

    void onSuccess(String ... message);

    void onFailure(String ... message);

    void onCancel(String ... message);
}
