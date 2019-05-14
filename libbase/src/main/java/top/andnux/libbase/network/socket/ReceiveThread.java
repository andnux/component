package top.andnux.libbase.network.socket;


import top.andnux.libbase.network.ICallback;

import java.io.InputStream;
import java.net.Socket;


public class ReceiveThread implements Runnable {

    private Socket socket;
    private ICallback<InputStream> callback;

    public ReceiveThread(Socket socket, ICallback<InputStream> callback) {
        this.socket = socket;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            while (true){
                if (callback != null){
                    callback.success(socket.getInputStream());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
