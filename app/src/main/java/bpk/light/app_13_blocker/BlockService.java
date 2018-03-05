package bpk.light.app_13_blocker;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BlockService extends Service {
    String LL = "LightLog";
    SharedPreferences sP;
    String start1, stop1;
    Timer mTimer;
    MyTimerTask myTimerTask;
    boolean running = true;
    MyBinder binder = new MyBinder();
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public void onCreate() {
        super.onCreate();
        sP = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d(LL, "onCreate");
       /* start1 = sP.getString("start1","2018-03-01 12:19:15");
        stop1 = sP.getString("stop1","2018-03-01 12:21:15");*/
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        start1 = sP.getString("start1","2018-03-01 16:25");
        stop1 = sP.getString("stop1","2018-03-01 16:26");
        Log.d(LL, "onStartCommand");
        blockTask(startId);
        return super.onStartCommand(intent, flags, startId);

    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LL, "onDestroy");
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LL, "onBind");
        return binder;
    }
    void stopTask(){
        try {
            mTimer.cancel();
        Log.d(LL, " stop mTimer");
        }catch (Exception e){
            e.printStackTrace();
            Log.d(LL, " stop mTimer fail");
        }
        try {
            myTimerTask.cancel();
            Log.d(LL, " stopmTimerTask");
        }catch (Exception e){
            e.printStackTrace();
            Log.d(LL, " stop mTimerTask fail");
        }
        try {
            stopSelf();
            Log.d(LL, " stop self");
        }catch (Exception e){
            e.printStackTrace();
            Log.d(LL, " stop self fail");
        }

    }

    void blockTask(int startId){
        Date date = new Date();
        mTimer =null;
        mTimer = new Timer();
        myTimerTask = null;
        myTimerTask = new MyTimerTask();
        Log.d(LL,date.toString());
        Date parsingDate;
        try {
            if (ft.parse(stop1).after(ft.parse(start1))) {
                Date now = new Date();
                if(now.before(ft.parse(stop1))){
                    parsingDate = ft.parse(start1);
                    Log.d(LL, parsingDate.toString());
                    running = true;
                    mTimer.schedule(myTimerTask, parsingDate,(long)2000);
                    Log.d(LL, " Timer start");
                }else if (now.after(ft.parse(stop1))){
                    mTimer.cancel();
                }
                else {
                    Log.d(LL, " now not before stop");
                }
                /*Log.d(LL, "stop -after start ");
                parsingDate = ft.parse("2018-02-27 17:03:15");
                Log.d(LL, parsingDate.toString());
                mTimer.schedule(myTimerTask, parsingDate);*/
            }else {
                Log.d(LL, " stop after start");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(LL,"Parse err");
        }


    }
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Date taskDate = new Date();
            int i=0;
            //try {
            try {
                if(taskDate.after(ft.parse(stop1))||sP.getBoolean("passCorrect",false)){
                    mTimer.cancel();
                    myTimerTask.cancel();
                    Log.d(LL, "tack canceled");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(sP.getBoolean("hideActStatus",false)&&!(sP.getBoolean("passCorrect",false))) {
                Intent intent = new Intent("android.intent.action.blockpage");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Log.d(LL, "Start time" + start1 + " stop time+ " + stop1);
                /*while(taskDate.before(ft.parse(stop1))&&running == true){
                    i++;
                    Log.d(LL,"Task executing...");
                    taskDate = new Date();
                }*/
                //stopSelf();
                //running = false;
            /*} catch (ParseException e) {
                e.printStackTrace();
            }*/
            }

        }
        }
    class MyBinder extends Binder {
        BlockService getService() {
            return BlockService.this;
        }
    }
    }

