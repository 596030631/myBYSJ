package green.wlhl.com.bysj.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import green.wlhl.com.bysj.R;

public class HumiAdapter extends RecyclerView.Adapter<HumiAdapter.VH> {

    public static class VH extends RecyclerView.ViewHolder{
        private TextView temp;
        private TextView tempLine;
        private TextView hour;
        private TextView mine;
        public VH(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.list_temp);
            tempLine = itemView.findViewById(R.id.list_temp_line);
            hour = itemView.findViewById(R.id.hour);
            mine = itemView.findViewById(R.id.mine);
        }
    }
    public static class HUMI{
        private String bizdate;
        private String humi;

        public HUMI(){
        }

        public HUMI(String date,String tem){
            humi = tem;
            bizdate = date;
        }
        public String getBizdate() { return bizdate; }
        public void setBizdate(String bizdate) { this.bizdate = bizdate; }

        public String getHumi() {
            return humi;
        }

        public void setHumi(String humi) {
            this.humi = humi;
        }
    }
    private List<HUMI> mList;
    private Context mContext;
    public HumiAdapter(Context c , List<HUMI> list){
        mList = list;
        mContext = c;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_recycler1,viewGroup,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        vh.temp.setText(mList.get(i).humi);
        vh.tempLine.setHeight(Integer.parseInt(mList.get(i).humi)*4);
        String h = mList.get(i).bizdate.substring(11,13);
        vh.hour.setText(h);
        String m = mList.get(i).bizdate.substring(14,16);
        vh.mine.setText(m);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

}
