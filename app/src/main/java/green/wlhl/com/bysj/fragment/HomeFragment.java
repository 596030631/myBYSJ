package green.wlhl.com.bysj.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import green.wlhl.com.bysj.ClientThreads;
import green.wlhl.com.bysj.LoginActivity;
import green.wlhl.com.bysj.MainActy;
import green.wlhl.com.bysj.R;
import green.wlhl.com.bysj.sqlite.Action;

public class HomeFragment extends Fragment {
    protected String strIpAddr;
    private TextView
            textNode1, textNode2, textNode3, textNode4,
            textTemp1, textTemp2, textTemp3, textTemp4,
            textHumi1, textHumi2, textHumi3, textHumi4,
            ivGas1, ivGas2, ivGas3, ivGas4,
            btnLamp1, btnLamp2, btnLamp3, btnLamp4,
            btnNetwork, btnExit, btnLampAll, btnScene;
//    static TextView textTips;
    public static final int RX_DATA_UPDATE_UI = 1;
    public final int TX_DATA_UPDATE_UI = 2;
    public static final int TIPS_UPDATE_UI = 3;
    public final int READ_ALL_INFO = 4;
    public final int WRITE_LAMP = 5;
    public final int WRITE_LAMP_ALL = 6;
    public static final int MAX_NODE = 4;
    public static Handler mainHandler;
    private ClientThreads clientThread = null;
    private Timer mainTimer;
    public static byte NodeData[][] = new byte[MAX_NODE][5];; //[5] 0=温度 1=湿度 2=气体 3=灯
    public byte SendBuf[] = { 0x3A, 0x00, 0x01, 0x0A, 0x00, 0x00, 0x23, 0x00 };
    private String strTemp, strHumi;
    private Message MainMsg;
    public final byte LAMP_ON = 0;
    public final byte LAMP_OFF = 1;
    private byte LampAllState = LAMP_ON;
    protected Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        mContext = inflater.getContext();
        strIpAddr = LoginActivity.ip;
        if(strIpAddr != null){
            boolean ret = isIPAddress(strIpAddr);
            if (!ret) {
                Toast.makeText(mContext, "IP格式错误！", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        initWidget(view);
        initWidgetStatus();
        initMainHandler();
        clientThread = new ClientThreads(strIpAddr);//建立客户端线程
        clientThread.start();
        mainTimer = new Timer();//定时查询所有终端信息
        setTimerTask();
        return view;

    }

    class ButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (clientThread == null
                    && (v.getId() != R.id.btn_exit) && (v.getId() != R.id.btn_network)) {
//                textTips.setText("请先连接网络");
                return;
            }
            switch (v.getId()) {
                case R.id.btn_lamp_all: //广播操作 开关所有灯
                    MainMsg = mainHandler.obtainMessage(TX_DATA_UPDATE_UI,
                            WRITE_LAMP_ALL, 0xFF);
                    mainHandler.sendMessage(MainMsg);
                    break;
                case R.id.image_lamp1: //开关终端1的灯
                    MainMsg = mainHandler.obtainMessage(TX_DATA_UPDATE_UI,
                            WRITE_LAMP, 1);
                    mainHandler.sendMessage(MainMsg);
                    break;
                case R.id.image_lamp2:
                    MainMsg = mainHandler.obtainMessage(TX_DATA_UPDATE_UI,
                            WRITE_LAMP, 2);
                    mainHandler.sendMessage(MainMsg);
                    break;
                case R.id.image_lamp3:
                    MainMsg = mainHandler.obtainMessage(TX_DATA_UPDATE_UI,
                            WRITE_LAMP, 3);
                    mainHandler.sendMessage(MainMsg);
                    break;
                case R.id.image_lamp4:
                    MainMsg = mainHandler.obtainMessage(TX_DATA_UPDATE_UI,
                            WRITE_LAMP, 4);
                    mainHandler.sendMessage(MainMsg);
                    break;
                case R.id.btn_scenes: //暂时用作停止自动刷新功能
                    mainTimer.cancel();
                    break;
            }
        }
    }
//    //显示连接对话框
//    private void showDialog(green.wlhl.com.bysj.MainActivity context) {
//        final EditText editIP = new EditText(context);
//        editIP.setText("192.168.191.1");
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        //builder.setIcon(R.drawable.ic_launcher);
//        builder.setTitle("请输入服务器IP，默认端口33333");
//        builder.setView(editIP);
//        builder.setPositiveButton("连接", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                boolean ret = isIPAddress(strIpAddr);
//                if (ret) {
//                    textTips.setText("服务器IP地址:" + strIpAddr);
//                } else {
//                    textTips.setText("IP地址不合法，请重新设置");
//                    return;
//                }
//                clientThread = new ClientThreads(strIpAddr);//建立客户端线程
//                clientThread.start();
//                mainTimer = new Timer();//定时查询所有终端信息
//                setTimerTask();
//            }
//        });
//        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                if (clientThread != null) {
//                    MainMsg = ClientThreads.childHandler
//                            .obtainMessage(ClientThreads.RX_EXIT);
//                    ClientThreads.childHandler.sendMessage(MainMsg);
//                    textTips.setText("与服务器断开连接");
//                }
//            }
//        });
//        builder.show();
//    }
    private void setTimerTask() {
        mainTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (clientThread != null) {
                    MainMsg = mainHandler.obtainMessage(TX_DATA_UPDATE_UI,
                            READ_ALL_INFO, 0xFF);
                    mainHandler.sendMessage(MainMsg);
                }
            }
        }, 500, 1000);//表示500毫秒之后，每隔1000毫秒执行一次
    }
    //通知客户端线程 发送消息
    public void SendData(byte buffer[], int len) {

        try{

            MainMsg = ClientThreads.childHandler.obtainMessage(ClientThreads.TX_DATA, len, 0, (Object) buffer);
            ClientThreads.childHandler.sendMessage(MainMsg);
        }catch (Exception e){
           e.printStackTrace();
        }
    }
    @SuppressLint("HandlerLeak")
    void initMainHandler() {
        final List<String> listT = new ArrayList<>();
        final List<String> listH = new ArrayList<>();
        final List<String> listG = new ArrayList<>();
        mainHandler = new Handler() {
            //主线程消息处理中心
            public void handleMessage(Message msg) {
                final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

                switch (msg.what) {
                    case RX_DATA_UPDATE_UI:
                        String bizdate = df.format(new Date());
                        //终端1
                        strTemp = "温度：" + NodeData[0][0] + "℃";
                        textTemp1.setText(strTemp);

                        listT.add(String.valueOf(NodeData[0][0]));
                        if(listT.size() >= 10){
                            Action.insertRecordTemp(getContext(),String.valueOf(NodeData[0][0]),bizdate);
                            listT.clear();
                        }

                        Log.e("TAG","采集到温度："+NodeData[0][0]+"");
                        strHumi = "湿度：" + NodeData[0][1] + "%";

                        listH.add(String.valueOf(NodeData[0][0]));
                        if(listH.size() >= 10){
                            Action.insertRecordHumi(getContext(),String.valueOf(NodeData[0][1]),bizdate);
                            listH.clear();
                        }


                        textHumi1.setText(strHumi);
                        if (NodeData[0][2] == 1)
                            ivGas1.setText("气体正常");   //气体高电平时正常
                        else
                            ivGas1.setText("气体异常");   //气体低电平时异常

                        listG.add(String.valueOf(NodeData[0][0]));
                        if(listG.size() >= 10){
                            Action.insertRecordGas(getContext(),String.valueOf(NodeData[0][2]),bizdate);
                            listG.clear();
                        }

                        Log.e("TAG","--------------------------"+"采集到气体："+NodeData[0][2]);

                        if (NodeData[0][3] == 0)           //低电平亮，高电平灭
                            btnLamp1.setText("点击关闭"); //灯亮
                        else
                            btnLamp1.setText("点击开启"); //灯灭

                        //终端2
                        strTemp = "温度：" + NodeData[1][0] + "℃";
                        textTemp2.setText(strTemp);
                        strHumi = "湿度：" + NodeData[1][1] + "%";
                        textHumi2.setText(strHumi);
                        if (NodeData[1][2] == 1)
                            ivGas2.setText("气体正常");   //气体高电平时正常
                        else
                            ivGas2.setText("气体异常");   //气体低电平时异常

                        if (NodeData[1][3] == 0)
                            btnLamp2.setText("点击关闭"); //灯亮
                        else
                            btnLamp2.setText("点击开启"); //灯灭

                        //终端3
                        strTemp = "温度：" + NodeData[2][0] + "℃";
                        textTemp3.setText(strTemp);
                        strHumi = "湿度：" + NodeData[2][1] + "%";
                        textHumi3.setText(strHumi);
                        if (NodeData[2][2] == 1)
                            ivGas3.setText("气体正常");   //气体高电平时正常
                        else
                            ivGas3.setText("气体异常");   //气体低电平时异常

                        if (NodeData[2][3] == 0)
                            btnLamp3.setText("点击关闭"); //灯亮
                        else
                            btnLamp3.setText("点击开启"); //灯灭
                        //终端4
                        strTemp = "温度：" + NodeData[3][0] + "℃";
                        textTemp4.setText(strTemp);
                        strHumi = "湿度：" + NodeData[3][1] + "%";
                        textHumi4.setText(strHumi);
                        if (NodeData[3][2] == 1)
                            ivGas4.setText("气体正常");   //气体高电平时正常
                        else
                            ivGas4.setText("气体异常");   //气体低电平时异常
                        if (NodeData[3][3] == 0)
                            btnLamp4.setText("点击关闭"); //灯亮
                        else
                            btnLamp4.setText("点击开启"); //灯灭
                        break;

                    case TX_DATA_UPDATE_UI: //msg.arg1保存功能码 arg2保存终端地址
                        switch (msg.arg1) {
                            case READ_ALL_INFO:
                                SendBuf[2] = (byte) msg.arg2;//0xFF;
                                SendBuf[3] = 0x01;           //FC
                                SendBuf[4] = (byte) 0xC4;
                                SendBuf[5] = (byte) 0x23;
                                SendData(SendBuf, 6); //查询所有终端报文3A 00 FF 01 C4 23
                                break;
                            case WRITE_LAMP:
                                SendBuf[2] = (byte) msg.arg2;
                                SendBuf[3] = 0x0A;
                                if (NodeData[SendBuf[2] - 1][3] == LAMP_OFF) { //当前灯处于熄灭状态，发命令将灯点亮
                                    NodeData[SendBuf[2] - 1][3] = 0x01;
                                    SendBuf[4] = 0x01; //data
                                } else {
                                    NodeData[SendBuf[2] - 1][3] = 0x00;
                                    SendBuf[4] = 0x00;
                                }
                                SendBuf[5] = XorCheckSum(SendBuf, 6);
                                SendData(SendBuf, 7); //发命令控制灯 3A 00 01 0A 00 00 23
                                break;
                            case WRITE_LAMP_ALL:
                                SendBuf[2] = (byte) 0xFF;
                                SendBuf[3] = 0x0A;
                                SendBuf[4] = LampAllState;
                                if (LampAllState == LAMP_ON) {
                                    LampAllState = LAMP_OFF;
                                } else {
                                    LampAllState = LAMP_ON;
                                }
                                SendBuf[5] = XorCheckSum(SendBuf, 6);
                                SendData(SendBuf, 7); //发命令控制灯
                                break;
                            default:
                                break;
                        }
                        break;
                    case TIPS_UPDATE_UI:
                        String str = (String) msg.obj;
//                        textTips.setText(str);
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
    void initWidgetStatus() {
        textNode1.setText("节点一");
        textNode2.setText("节点二");
        textNode3.setText("节点三");
        textNode4.setText("节点四");
        textTemp1.setText(R.string.init_temp);
        textTemp2.setText(R.string.init_temp);
        textTemp3.setText(R.string.init_temp);
        textTemp4.setText(R.string.init_temp);
        textHumi1.setText(R.string.init_humi);
        textHumi2.setText(R.string.init_humi);
        textHumi3.setText(R.string.init_humi);
        textHumi4.setText(R.string.init_humi);
        ivGas1.setText(R.string.init_gas);//异常
        ivGas2.setText(R.string.init_gas);//异常
        ivGas3.setText(R.string.init_gas);//异常
        ivGas4.setText(R.string.init_gas);//异常
        btnLamp1.setText(R.string.init_lamp); //灯灭
        btnLamp2.setText(R.string.init_lamp); //灯灭
        btnLamp3.setText(R.string.init_lamp); //灯灭
        btnLamp4.setText(R.string.init_lamp); //灯灭
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mainTimer != null)
            mainTimer.cancel();
    }

    private boolean isIPAddress(String ipaddr) {
        boolean flag = false;
        Pattern pattern = Pattern
                .compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher m = pattern.matcher(ipaddr);
        flag = m.matches();
        return flag;
    }
    private byte XorCheckSum(byte[] pBuf, int len) {
        int i;
        byte byRet = 0;
        if (len == 0)
            return byRet;
        else
            byRet = pBuf[0];
        for (i = 1; i < len; i++)
            byRet = (byte) (byRet ^ pBuf[i]);
        return byRet;
    }

    void initWidget(View view) {
        textNode1 = view.findViewById(R.id.node_title1);
        textNode2 = view.findViewById(R.id.node_title2);
        textNode3 = view.findViewById(R.id.node_title3);
        textNode4 = view.findViewById(R.id.node_title4);
        textTemp1 = view.findViewById(R.id.temperature1);
        textTemp2 = view.findViewById(R.id.temperature2);
        textTemp3 = view.findViewById(R.id.temperature3);
        textTemp4 = view.findViewById(R.id.temperature4);
        textHumi1 = view.findViewById(R.id.humidity1);
        textHumi2 = view.findViewById(R.id.humidity2);
        textHumi3 = view.findViewById(R.id.humidity3);
        textHumi4 =view.findViewById(R.id.humidity4);
        ivGas1 = view.findViewById(R.id.image_gas1);
        ivGas2 = view.findViewById(R.id.image_gas2);
        ivGas3 = view.findViewById(R.id.image_gas3);
        ivGas4 = view.findViewById(R.id.image_gas4);
        btnLamp1 = view.findViewById(R.id.image_lamp1);
        btnLamp2 = view.findViewById(R.id.image_lamp2);
        btnLamp3 = view.findViewById(R.id.image_lamp3);
        btnLamp4 = view.findViewById(R.id.image_lamp4);
        btnLamp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMsg = mainHandler.obtainMessage(TX_DATA_UPDATE_UI,
                        WRITE_LAMP, 1);
                mainHandler.sendMessage(MainMsg);
            }
        });

        btnLamp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMsg = mainHandler.obtainMessage(TX_DATA_UPDATE_UI,
                        WRITE_LAMP, 2);
                mainHandler.sendMessage(MainMsg);
            }
        });

        btnLamp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMsg = mainHandler.obtainMessage(TX_DATA_UPDATE_UI,
                        WRITE_LAMP, 3);
                mainHandler.sendMessage(MainMsg);
            }
        });

        btnLamp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMsg = mainHandler.obtainMessage(TX_DATA_UPDATE_UI,
                        WRITE_LAMP, 4);
                mainHandler.sendMessage(MainMsg);
            }
        });
    }
}


