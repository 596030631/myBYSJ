package green.wlhl.com.bysj;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import green.wlhl.com.bysj.fragment.HomeFragment;

public class ClientThreads extends Thread {
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private Socket socket;
    private SocketAddress socketAddress;
    public static Handler childHandler;
    private boolean RxFlag = true;
    private RxThread rxThread;
    final int TEXT_INFO = 12;
    static final int RX_EXIT = 11;
    public static final int TX_DATA = 10;
    Context mainContext;
    Message msg;
    private String strIP;
    byte cNodeData[][]=new byte[4][5]; // [5] 0=温度 1=湿度 2=气体 3=开关
    final int SERVER_PORT = 33333;//服务端socket绑定端口号

    /**
     * @auther sj by 2019-04-08
     * @param  ip*/
    public ClientThreads(String ip) {
        strIP = ip;
    }

    /**
     * connect to the net
     * */
    void connect() {
        RxFlag = true;
        socketAddress = new InetSocketAddress(strIP, SERVER_PORT);
        socket = new Socket();

        try {
            socket.connect(socketAddress, SERVER_PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            msg = HomeFragment.mainHandler.obtainMessage(HomeFragment.TIPS_UPDATE_UI, "连接成功");
            HomeFragment.mainHandler.sendMessage(msg);
            rxThread = new RxThread();
            rxThread.start();
        } catch (IOException e) {
            try {
                sleep(10);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            msg = HomeFragment.mainHandler.obtainMessage(HomeFragment.TIPS_UPDATE_UI, "无法连接到服务器");
            HomeFragment.mainHandler.sendMessage(msg);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    void initChildHandler() {
        Looper.prepare();
        childHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TX_DATA:
                        int len = msg.arg1;
                        try {
                            outputStream.write((byte [])msg.obj, 0, len);
                            outputStream.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case RX_EXIT:
                        RxFlag = false;
                        try {
                            if (socket.isConnected()) {
                                inputStream.close();
                                outputStream.close();
                                socket.close();
                            }

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        childHandler.getLooper().quit();// 结束消息队列

                        break;

                    default:
                        break;
                }

            }
        };

        // 启动该线程的消息队列
      Looper.loop();

    }

    public void run() {
        connect();
        initChildHandler();
        msg = HomeFragment.mainHandler.obtainMessage(HomeFragment.TIPS_UPDATE_UI, "与服务器断开连接");
        HomeFragment.mainHandler.sendMessage(msg);
    }

    /**
     * socket receive thread
     * */
    public class RxThread extends Thread {
        public void run() {
            try {
                while (socket.isConnected() && RxFlag) {
                    byte [] RxBuf = new byte[256];//设置缓冲区
                    int len = inputStream.read(RxBuf);//通过read方法存入缓冲区，返回值用int接收，-1代表空，其他对应Acsii码

                    if (len > 4 && RxBuf[0] == 0x3A) {//3*16+10 = 58,0x3A是zigbee第一个节点的信息，每个节点的信息递增4分别为 0x3A,0x3E,0x42,0x46,.....
                        int i , index = 4;
                        // 解析数据
                        switch (RxBuf[3]) {//默认四个节点，写死了，有时间做一些优化，可以无限新增，最终节点未集合size-1；
                            case 0x01:    //  1
                                for (i = 0; i <  HomeFragment.MAX_NODE; i++) {//
                                    System.arraycopy(RxBuf, index, HomeFragment.NodeData[i], 0, 4);
                                    index += 4;
                                }
                                msg = HomeFragment.mainHandler.obtainMessage(HomeFragment.RX_DATA_UPDATE_UI,"Connect");
                                HomeFragment.mainHandler.sendMessage(msg);
                                break;
                            case 0x0A:  //循环在刷新画面，写操作时这里可以不用刷新  10
                                break;
                            default:
                                break;
                        }
                    }else if (len < 0){
                        msg = HomeFragment.mainHandler.obtainMessage(HomeFragment.TIPS_UPDATE_UI,
                                "与服务器断开连接");
                        HomeFragment.mainHandler.sendMessage(msg);

                        //退出接收线程
                        msg = childHandler.obtainMessage(RX_EXIT);
                        childHandler.sendMessage(msg);
                        break;

                    }

                    //sleep(100);
                }

                if (socket.isConnected())
                    socket.close();

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}