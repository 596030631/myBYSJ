package green.wlhl.com.bysj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import green.wlhl.com.bysj.R;

public class GasAdapter extends RecyclerView.Adapter<GasAdapter.VH> {

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


    public static class GAS{
        private String bizdate;
        private String gas;

        public GAS(){
        }

        public GAS(String date,String gas){
            this.gas = gas;
            bizdate = date;
        }

        public String getBizdate() {
            return bizdate;
        }

        public void setBizdate(String bizdate) {
            this.bizdate = bizdate;
        }

        public String getGas() {
            return gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }
    }

    private List<GAS> mList;
    private Context mContext;
    public GasAdapter(Context c , List<GAS> list){
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
        //vh.temp.setText(mList.get(i).gas);
        vh.tempLine.setHeight(40);

        vh.tempLine.setBackgroundColor(("1".equals(mList.get(i).getGas()) ? Color.rgb(200,1,1) : Color.rgb(200,200,200)));



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
