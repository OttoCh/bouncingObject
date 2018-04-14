package com.example.ottochristianto.hiddenobject;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

public class floating_surface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread _thread;
    final int view_width = this.getWidth();
    final int view_height = this.getHeight();
    Context context;

    public floating_surface(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initSurface(View target) {
        //listen for events
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);

        //and instantiate thread
        _thread = new GameThread(holder, context, new Handler(), view_height, view_width, target);

        _thread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        try {
            //_thread.interrupt(); alternative dmn dia bisa lgsg mengakhiri thread, tetap harus di cek sendiri di threadnya .isInterrupt();
            _thread.stopThread();
            _thread.join(); //menggunakan flag
        }
        catch(InterruptedException ex) {
            Log.d("Thread", "surfaceDestroyed: fail to stop thread");
        }

    }
}
