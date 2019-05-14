package top.andnux.libbase.network.netstate;

import java.lang.reflect.Method;

public class NetStateBean {

    private top.andnux.libbase.network.netstate.NetType mNetState;
    private Class<?> type;
    private Method mMethod;

    public NetStateBean() {
    }

    public NetStateBean(top.andnux.libbase.network.netstate.NetType mNetState, Class<?> type, Method mMethod) {
        this.mNetState = mNetState;
        this.type = type;
        this.mMethod = mMethod;
    }

    public top.andnux.libbase.network.netstate.NetType getNetState() {
        return mNetState;
    }

    public void setNetState(top.andnux.libbase.network.netstate.NetType mNetState) {
        this.mNetState = mNetState;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Method getMethod() {
        return mMethod;
    }

    public void setMethod(Method mMethod) {
        this.mMethod = mMethod;
    }
}
