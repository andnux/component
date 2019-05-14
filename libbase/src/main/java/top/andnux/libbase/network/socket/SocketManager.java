package top.andnux.libbase.network.socket;


import top.andnux.libbase.manager.ThreadManager;
import top.andnux.libbase.network.ICallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class SocketManager {

    private static final SocketManager ourInstance = new SocketManager();
    private ICallback callback;

    public static SocketManager getInstance() {
        return ourInstance;
    }

    private Socket socket;

    private SocketManager() {

    }

    public ICallback getCallback() {
        return callback;
    }

    public void setCallback(ICallback callback) {
        this.callback = callback;
    }

    public void connect(String host, int port) throws Exception {
        close();
        socket = new Socket(host, port);
        socket.setSoTimeout(60000);
        socket.setKeepAlive(true);
        ThreadManager.getInstance().execute( new ReceiveThread(socket, callback));
    }

    public void send(InputStream inputStream){
        if (socket == null){
            throw  new RuntimeException("请先调用SocketManager.getInstance().connect(String host, int port)");
        }
        ThreadManager.getInstance().execute(new SendThread(socket, inputStream));
    }

    public void send(String data){
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        send(inputStream);
    }

    public void close(){
        if (socket != null){
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
