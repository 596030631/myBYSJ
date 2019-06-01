package green.wlhl.com.bysj.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import green.wlhl.com.bysj.R;
import green.wlhl.com.bysj.adapter.GasAdapter;
import green.wlhl.com.bysj.adapter.HumiAdapter;
import green.wlhl.com.bysj.adapter.TempAdapter;
import green.wlhl.com.bysj.sqlite.Action;

public class MyFragment extends Fragment {


    private int count = 1;
    /**
     * 消息处理器的应用
     */
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    Log .e("TAG","刷新");

                    list1.clear();
                    list2.clear();
                    list3.clear();

                    list1 = Action.selectRecordTemp(getContext(), list1);
                    list2 = Action.selectRecordHumi(getContext(), list2);
                    list3 = Action.selectRecordGas(getContext(), list3);
                    tempAdapter.notifyDataSetChanged();
                    humiAdapter.notifyDataSetChanged();
                    gasAdapter.notifyDataSetChanged();

                    break;
                case 2:
                    mTimer.cancel();//
                    mTimer=null;
            }
            super.handleMessage(msg);
        }
    };
    private List<TempAdapter.TEMP> list1 = new ArrayList<>();
    private List<HumiAdapter.HUMI> list2 = new ArrayList<>();
    private List<GasAdapter.GAS> list3 = new ArrayList<>();
    private TempAdapter tempAdapter;
    private HumiAdapter humiAdapter;
    private GasAdapter gasAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_my,container,false);
        TextView mBtn = view.findViewById(R.id.button);


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(mTimer!=null){
                        mTimer.cancel();// 退出之前的mTimer
                    }
                    mTimer = new Timer();// new一个Timer,否则会报错
                    timerTask();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });


                RecyclerView recyclerView1 = view.findViewById(R.id.recycler1);
        RecyclerView recyclerView2 = view.findViewById(R.id.recycler2);
        RecyclerView recyclerView3 = view.findViewById(R.id.recycler3);


        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
        llm1.setOrientation(OrientationHelper.HORIZONTAL);
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        llm2.setOrientation(OrientationHelper.HORIZONTAL);
        LinearLayoutManager llm3 = new LinearLayoutManager(getContext());
        llm3.setOrientation(OrientationHelper.HORIZONTAL);
        recyclerView1.setLayoutManager(llm1);
        recyclerView2.setLayoutManager(llm2);
        recyclerView3.setLayoutManager(llm3);

//        List<TempAdapter.TEMP> list = new ArrayList<>();
//        list.add(new TempAdapter.TEMP("2019-05-31 06:12:12","21"));
//        list.add(new TempAdapter.TEMP("2019-05-31 06:13:12","22"));
//        list.add(new TempAdapter.TEMP("2019-05-31 06:14:12","23"));
//        list.add(new TempAdapter.TEMP("2019-05-31 06:15:12","24"));
//        list.add(new TempAdapter.TEMP("2019-05-31 06:12:12","25"));
//        list.add(new TempAdapter.TEMP("2019-05-31 06:16:12","26"));
//        list.add(new TempAdapter.TEMP("2019-05-31 06:12:12","27"));
//        list.add(new TempAdapter.TEMP("2019-05-31 06:14:12","28"));
//        list.add(new TempAdapter.TEMP("2019-05-31 06:12:12","29"));
//        list.add(new TempAdapter.TEMP("2019-05-31 07:12:12","29"));
//        list.add(new TempAdapter.TEMP("2019-05-31 07:12:12","28"));
//        list.add(new TempAdapter.TEMP("2019-05-31 07:11:12","27"));
//        list.add(new TempAdapter.TEMP("2019-05-31 07:12:12","26"));
//        list.add(new TempAdapter.TEMP("2019-05-31 07:12:12","25"));
//        list.add(new TempAdapter.TEMP("2019-05-31 07:22:12","21"));
//        list.add(new TempAdapter.TEMP("2019-05-31 07:32:12","22"));
//        list.add(new TempAdapter.TEMP("2019-05-31 08:12:12","23"));
//        list.add(new TempAdapter.TEMP("2019-05-31 08:12:12","24"));
//        list.add(new TempAdapter.TEMP("2019-05-31 08:42:12","25"));
//        list.add(new TempAdapter.TEMP("2019-05-31 08:12:12","26"));
//        list.add(new TempAdapter.TEMP("2019-05-31 08:12:12","27"));
//        list.add(new TempAdapter.TEMP("2019-05-31 09:12:12","28"));
//        list.add(new TempAdapter.TEMP("2019-05-31 09:12:12","29"));
//        list.add(new TempAdapter.TEMP("2019-05-31 09:12:12","29"));
//        list.add(new TempAdapter.TEMP("2019-05-31 09:12:12","28"));
//        list.add(new TempAdapter.TEMP("2019-05-31 09:12:12","27"));
//        list.add(new TempAdapter.TEMP("2019-05-31 09:12:12","26"));
//        list.add(new TempAdapter.TEMP("2019-05-31 09:12:12","25"));
//
//
//        List<HumiAdapter.HUMI> list2 = new ArrayList<>();
//        list2.add(new HumiAdapter.HUMI("2019-05-31 06:12:12","51"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 06:13:12","52"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 06:14:12","53"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 06:15:12","54"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 06:12:12","65"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 06:16:12","66"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 06:12:12","57"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 06:14:12","68"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 06:12:12","59"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 07:12:12","79"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 07:32:12","62"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 08:12:12","63"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 08:12:12","64"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 08:42:12","65"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 08:12:12","66"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 08:12:12","87"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 09:12:12","78"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 09:12:12","69"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 09:12:12","59"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 09:12:12","18"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 09:12:12","37"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 09:12:12","36"));
//        list2.add(new HumiAdapter.HUMI("2019-05-31 09:12:12","65"));
//
//
//        List<GasAdapter.GAS> list3 = new ArrayList<>();
//        list3.add(new GasAdapter.GAS("2019-05-31 06:12:12","1"));
//        list3.add(new GasAdapter.GAS("2019-05-31 06:13:12","1"));
//        list3.add(new GasAdapter.GAS("2019-05-31 06:14:12","0"));
//        list3.add(new GasAdapter.GAS("2019-05-31 06:15:12","1"));
//        list3.add(new GasAdapter.GAS("2019-05-31 06:12:12","1"));
//        list3.add(new GasAdapter.GAS("2019-05-31 06:16:12","0"));
//        list3.add(new GasAdapter.GAS("2019-05-31 06:12:12","1"));
//        list3.add(new GasAdapter.GAS("2019-05-31 06:14:12","1"));
//        list3.add(new GasAdapter.GAS("2019-05-31 06:12:12","0"));
//        list3.add(new GasAdapter.GAS("2019-05-31 07:12:12","1"));
//        list3.add(new GasAdapter.GAS("2019-05-31 07:12:12","0"));
//        list3.add(new GasAdapter.GAS("2019-05-31 07:11:12","1"));
//        list3.add(new GasAdapter.GAS("2019-05-31 07:12:12","1"));
//        list3.add(new GasAdapter.GAS("2019-05-31 07:12:12","1"));
//        list3.add(new GasAdapter.GAS("2019-05-31 07:22:12","0"));


        tempAdapter = new TempAdapter(getContext(),list1);
        humiAdapter = new HumiAdapter(getContext(),list2);
        gasAdapter = new GasAdapter(getContext(),list3);

        recyclerView1.setAdapter(tempAdapter);
        recyclerView2.setAdapter(humiAdapter);
        recyclerView3.setAdapter(gasAdapter);

        timerTask(); // 定时执行
        return view;

    }

    public Timer mTimer = new Timer();// 定时器

    public void timerTask() {
        //创建定时线程执行更新任务
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(count<=5000){
                    System.out.println("TimerTask-->Id is "
                            + Thread.currentThread().getId());// TimerTask在它自己的线程中
                    mHandler.sendEmptyMessage(1);// 向Handler发送消息
                }else{
                    mHandler.sendEmptyMessage(2);// 向Handler发送消息停止继续执行
                }
                count++;
            }
        }, 5000, 5000);// 定时任务
    }

    /**
     * 销毁定时器的方式
     */
    @Override
    public void onDestroy() {
        mTimer.cancel();// 程序退出时cancel timer
        super.onDestroy();
    }




}
