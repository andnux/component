package top.andnux.libbase.network.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SendThread implements Runnable{

    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    public SendThread(Socket socket, InputStream inputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            socket.setSoTimeout(60000);
            socket.setKeepAlive(true);
            outputStream = socket.getOutputStream();
            if (inputStream == null){
                return;
            }
            byte[] buffer = new byte[1024 * 4];
            int length = -1;
            while ((length = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
