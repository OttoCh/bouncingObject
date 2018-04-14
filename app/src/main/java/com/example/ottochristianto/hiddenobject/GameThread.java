package com.example.ottochristianto.hiddenobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.View;

public class GameThread extends Thread {

    private SurfaceHolder _surfaceHolder;
    private Paint _paint;
    private GameState _state;
    boolean run = true;

    public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler, int height, int width, View target)
    {
        _surfaceHolder = surfaceHolder;
        _paint = new Paint();
        _state = new GameState(height, width, target);
    }


    @Override
    public void run() {
        while(run)
        {
            Canvas canvas = _surfaceHolder.lockCanvas();
            _state.update();
            _state.draw(canvas,_paint);
            _surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void stopThread() {
        run = false;
    }

    public GameState getGameState()
    {
        return _state;
    }
}
