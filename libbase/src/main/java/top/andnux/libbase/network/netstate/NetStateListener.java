package top.andnux.libbase.network.netstate;

public interface NetStateListener {

    void onConnect(NetType state);

    void onDisConnect();
}
