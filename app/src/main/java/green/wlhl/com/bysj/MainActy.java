package green.wlhl.com.bysj;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import green.wlhl.com.bysj.fragment.HomeFragment;
import green.wlhl.com.bysj.fragment.MyFragment;

public class MainActy extends BaseActivity implements View.OnClickListener {
    private List<Fragment> fragments = new ArrayList();
    private ViewPager viewPager;
    private long mExitTime;
    private TextView home_icon;
    private TextView me_icon;
    private TextView title;
    private View choose1;
    private View choose2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_acty);
        Log.e("IP",LoginActivity.ip);
        //添加fragment
        fragments.add(new HomeFragment());
        fragments.add(new MyFragment());

        viewPager = (ViewPager) findViewById(R.id.view_page_fragment);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);

        LinearLayout btn_Page1 = findViewById(R.id.btn_fragment_page1);
        LinearLayout btn_Page2 = findViewById(R.id.btn_fragment_page2);
        btn_Page1.setOnClickListener(this);
        btn_Page2.setOnClickListener(this);
        title = findViewById(R.id.main_title_text);
        title.setOnClickListener(this);
        choose1 = findViewById(R.id.ischoose1);
        choose2 = findViewById(R.id.ischoose2);
        home_icon = findViewById(R.id.home_icon);
        me_icon = findViewById(R.id.me_icon);
        /*监听页面切换，在这主要作用是对应切换底部按钮*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }
            @Override
            public void onPageSelected(int i) {//i为页面编号
                //还原底部按钮颜色
                home_icon.setBackgroundResource(R.drawable.home_1);
                me_icon.setBackgroundResource(R.drawable.me_icon_1);
                choose1.setVisibility(View.GONE);
                choose2.setVisibility(View.GONE);
                switch (i){
                    case 0 :
                        home_icon.setBackgroundResource(R.drawable.home_2);
                        title.setText("节点信息");
                        choose1.setVisibility(View.VISIBLE);
                        break;
                    case 1 :
                        me_icon.setBackgroundResource(R.drawable.me_icon_2);
                        title.setText("其他");
                        choose2.setVisibility(View.VISIBLE);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) { }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.main_title_text:

                break;
            case R.id.btn_fragment_page1:
                viewPager.setCurrentItem(0);
                break;
                case R.id.btn_fragment_page2:
                viewPager.setCurrentItem(1);
                break;
        }
    }
    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        //获得碎片的所有
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        //返回碎片的长度
        @Override
        public int getCount() {
            return fragments.size();
        }
    }
    /**右上角设置按钮*/
    public void settings(View view) {
    }
        //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
