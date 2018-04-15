package com.example.ottochristianto.hiddenobject;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

public class timers_object extends AppCompatTextView {

    public timers_object(Context context) {
        super(context);
    }

    public timers_object(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public timers_object(Context context, AttributeSet attrs, int numb) {
        super(context, attrs, numb);
    }

    int maxTime = 60;
    int currentTime = maxTime;
    boolean runTimer = false;
    boolean timerIsRunning = false;
    GameActivity gameActivity;
    TimerRunnable timerRunnable;
    Thread thread;
    Handler mHandler = new Handler();

    public void Init(GameActivity gameActivity) {
        this.setTextSize(70);
        this.setTextColor(getResources().getColor(R.color.White));
        this.setText("00");
        this.gameActivity = gameActivity;
    }

    private void ResetTimer(int maxTime) {
        this.setText(Integer.toString(maxTime));
        currentTime = maxTime;
    }

    private void timesUp() {
        if(gameActivity != null)
            gameActivity.timesUp();
        runTimer = false;
        timerRunnable.toggleTimer(false);
    }

    public void UpdateTimer() {
        if(!runTimer) return;

        currentTime--;
        if(currentTime < 0) currentTime = 0;
        String writtenTime = Integer.toString(currentTime);
        if(currentTime < 10) writtenTime = "0" + writtenTime;
        final String w = writtenTime;
        mHandler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        setText(w);
                    }
                });
        //this.setText(writtenTime);

        if(currentTime <=0) {
            //kirim kembali ke ui thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    timesUp();
                }
            });
        }
    }



    public void SetTimer(int maxTime) {
        this.maxTime = maxTime;
        this.setText(Integer.toString(maxTime));
    }

    public void BeginTimer() {
        runTimer = true;
        currentTime = maxTime;

        //jangan sampai ada 2 thread jalan di saat bersamaan
        if(thread == null || !thread.isAlive()) {
            timerRunnable = new TimerRunnable(this);
            thread = new Thread(timerRunnable);
            timerRunnable.toggleTimer(true);
            thread.start();
            timerIsRunning = true;
        }
        else {
            Log.d("Timer", "continue from existing thread ");
            timerRunnable.toggleTimer(true);
        }
    }

    public void ContinueTimer() {
        runTimer = true;

        timerRunnable.toggleTimer(true);
    }

    public void PauseTimer() {
        runTimer = false;
        timerRunnable.toggleTimer(false);
    }

    public void StopTimer() {
        runTimer = false;
        timerRunnable.toggleTimer(false);
        boolean success = true;
        try {
            thread.join();
        }
        catch (Exception e) {
            success = false;
            Log.d("Timer", "fail to stop thread");
        }

        if(success)
            timerIsRunning = false;
    }

}

class TimerRunnable implements Runnable {

    boolean run = false;
    timers_object timers;

    public TimerRunnable(timers_object timers) {
        this.timers = timers;
    }

    @Override
    public void run() {
        while(run) {
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                Log.d("Timer", "timer: cant sleep");
                run = false;
            }
            if(timers == null) run = false;

            timers.UpdateTimer();
        }
    }

    public void toggleTimer(boolean run) {
        this.run = run;
    }
}
