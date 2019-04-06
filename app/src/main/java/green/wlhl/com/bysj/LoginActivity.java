package green.wlhl.com.bysj;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private boolean user_name_empty;
    public static String ip;
    private LinearLayout mRootView;
    private String mUserName;
    private String mUserPassword;
    int count = 1;
    LinearLayout viewBoard;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewBoard= (LinearLayout) findViewById(R.id.view_board);
        final LinearLayout.LayoutParams view = (LinearLayout.LayoutParams) viewBoard.getLayoutParams();
        final Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(LoginActivity.this);
        EditText et_username = findViewById(R.id.user_name);
        EditText et_password = findViewById(R.id.password);
        mUserName = et_username.getText().toString().trim();
        mUserPassword = et_password.getText().toString().trim();
        EditText et_ip = findViewById(R.id.init_ip);
        ip = et_ip.getText().toString().trim();
        user_name_empty = true;
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mUserName = s.toString();
                if(s != null){
                    user_name_empty = false;
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mUserPassword = s.toString();
                if(s != null && !user_name_empty){
                    btn_login.setAlpha(0.8f);
                }
            }
        });
            mRootView = (LinearLayout) findViewById(R.id.root_linearlayout);//根布局
            DisplayMetrics metrics = new DisplayMetrics();//获取屏幕高度
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            final int screenHeight = metrics.heightPixels;
            //因为系统没有直接监听软键盘API，所以就用以下方法
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() { //当界面大小变化时，系统就会调用该方法
                            Rect r = new Rect(); //该对象代表一个矩形（rectangle）
                            mRootView.getWindowVisibleDisplayFrame(r); //将当前界面的尺寸传给Rect矩形
                            int deltaHeight = screenHeight - r.bottom;  //弹起键盘时的变化高度，在该场景下其实就是键盘高度。
                            if (deltaHeight > 150) {  //该值是随便写的，认为界面高度变化超过150px就视为键盘弹起。
                                view.height = (int)screenHeight*3/5;
                            } else {
                                if(count == 1){
                                    view.height = screenHeight*3/5;
                                    count++;
                                } else{
                                    view.height = (int)screenHeight/4;
                                }

                            }
                        }
                    });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if(ip != null && mUserName.equals("admin") && mUserPassword.equals("admin")){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else{
                    if (ip ==null){
                        Toast.makeText(this, "IP为空", Toast.LENGTH_SHORT).show();
                    }else if(mUserName.equals("")){
                        Toast.makeText(this, "用户名为空", Toast.LENGTH_SHORT).show();
                    }else if(mUserPassword.equals("")){
                        Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "错误", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
    public void keyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}

