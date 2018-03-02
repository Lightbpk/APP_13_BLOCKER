package bpk.light.app_13_blocker;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class BlockActivity extends AppCompatActivity {
    SharedPreferences sP;
    //Timer mTimer = new Timer();
    String start1,stop1, LL = "LightLog";
    Button btnUnlock, btnBind;
    ServiceConnection sConn;
    BlockService blockService;
    IBinder iBinder1;
    boolean bound = false;
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        btnUnlock = findViewById(R.id.btnUnlock);
        btnBind = findViewById(R.id.btnBind);
        sP = PreferenceManager.getDefaultSharedPreferences(this);
        start1 = sP.getString("start1","2018-03-01 16:25");
        stop1 = sP.getString("stop1","2018-03-01 16:25");
        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d(LL, "onServiceConnected");
                blockService = ((BlockService.MyBinder) iBinder).getService();
                iBinder1 = iBinder;
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(LL, "onServiceDisconnected");
                bound = false;
            }
        };
        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService(new Intent("bpk.light.app_13_blocker.action.service"),sConn,BIND_AUTO_CREATE);
                Log.d(LL, "try bind");
            }
        });

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bound) {
                    Log.d("LightLog","Bound - true");
                    //bindService(new Intent(BlockActivity.this, BlockService.class),sConn,BIND_AUTO_CREATE);
                    blockService = ((BlockService.MyBinder) iBinder1).getService();
                    blockService.stopTask();
                    return;
                }else{
                    Log.d("LightLog","Bound - false");
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Date now = new Date();
        try {
            if (now.before(ft.parse(start1))) {
                super.onBackPressed();
            }
            else if(now.after(ft.parse(stop1))){
                super.onBackPressed();
            }
            // super.onBackPressed();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
