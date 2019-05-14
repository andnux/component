package top.andnux.libbase.network.websocket;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import java.nio.charset.Charset;

public class DefaultWebSocketListener extends WebSocketListener {

    private WebSocketHandler handler;
    private WebSocket mWebSocket;

    interface WebSocketListener{
        void onOpen(WebSocket socket);
    }

    private  WebSocketListener mWebSocketListener;

    public WebSocketListener getWebSocketListener() {
        return mWebSocketListener;
    }

    public void setmWebSocketListener(WebSocketListener webSocketListener) {
        this.mWebSocketListener = webSocketListener;
    }

    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    public DefaultWebSocketListener(WebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        this.mWebSocket = webSocket;
        if (this.mWebSocketListener != null){
            mWebSocketListener.onOpen(webSocket);
        }
        if (handler != null){
            handler.onOpen(response.body().byteStream());
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        if (handler != null){
            handler.onMessage(text);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        if (handler != null){
            handler.onMessage(bytes.string(Charset.forName("utf-8")));
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        if (handler != null){
            handler.onClosing(code, reason);
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        if (handler != null){
            handler.onClosed(code, reason);
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        if (handler != null){
            handler.onFailure(t,response.body().byteStream());
        }
    }
}
