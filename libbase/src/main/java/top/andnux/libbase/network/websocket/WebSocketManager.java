package top.andnux.libbase.network.websocket;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class WebSocketManager {

    private static final WebSocketManager ourInstance = new WebSocketManager();

    public static WebSocketManager getInstance() {
        return ourInstance;
    }

    private WebSocketHandler webSocketHandler;
    private WebSocket mWebSocket;

    public WebSocketHandler getWebSocketHandler() {
        return webSocketHandler;
    }

    public void setWebSocketHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    private WebSocketManager() {

    }

    public void connect(String url) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder().url(url).build();
        DefaultWebSocketListener socketListener = new DefaultWebSocketListener(this.webSocketHandler);
        socketListener.setmWebSocketListener(new DefaultWebSocketListener.WebSocketListener() {
            @Override
            public void onOpen(WebSocket socket) {
                WebSocketManager.getInstance().mWebSocket = socket;
            }
        });
        client.newWebSocket(request, socketListener);
        client.dispatcher().executorService().shutdown();
    }

    public void close() {
        if (mWebSocket != null) {
            mWebSocket.close(-1, "客服端关闭");
        }
    }
}
