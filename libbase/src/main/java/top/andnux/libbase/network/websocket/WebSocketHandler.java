package top.andnux.libbase.network.websocket;

import java.io.InputStream;

public interface WebSocketHandler {

    void onOpen(InputStream response);

    void onMessage(String text);

    void onClosing(int code, String reason);

    void onClosed(int code, String reason);

    void onFailure(Throwable t, InputStream response);
}
