package top.andnux.libbase.network;

public interface ICallback<T> {

    void success(T data);

    void fail(Exception exp);

    void complete();
}
