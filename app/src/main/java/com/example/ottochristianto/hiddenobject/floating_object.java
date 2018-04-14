package com.example.ottochristianto.hiddenobject;

import android.content.Context;
import android.graphics.Canvas;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.v7.widget.AppCompatImageView;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ottochristianto.hiddenobject.GameActivity;

import org.w3c.dom.Attr;

import java.util.Random;

/**
 * Created by Otto Christianto on 3/31/2018.
 */

public class floating_object extends AppCompatImageView {

    public int objectID;
    public int objectSprite;

    private float speedX = 10f;
    private float speedY = 10f;
    private float parentWidth;
    private float parentHeight;

    private GameActivity currentActivity;

    public floating_object(Context context) {
        super(context);
    }

    public floating_object(Context context, AttributeSet attrs) {
        super(context, null);
    }

    public floating_object(Context context, AttributeSet attrs, int c) {
        super(context, null, c);
    }

    public void Init(int objectSprite, int objectID, GameActivity gameActivity) {
        this.objectSprite = objectSprite;
        this.setTag(R.style.AppTheme, (Integer)objectSprite);
        this.setImageResource(objectSprite);
        this.objectID = objectID;
        this.currentActivity = gameActivity;
        randomizeDirection();
    }

    private void randomizeDirection() {
        Random rand = new Random();
        int val = rand.nextInt(1);
        if(val == 1) speedY *= -1;
        val = rand.nextInt(1);
        if(val == 1) speedX *= -1;
    }

    private void pickObject() {
        //panggil fungsi di activity utama
        if(currentActivity != null)
        if(currentActivity.objectPickedUp(objectSprite)) {
            //jika dicari maka jalankan suatu animasi
            this.setVisibility(GONE);
        }
        return;
    }

    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();
        this.setTag(R.string.tag_key, (Integer)objectSprite);
        if(this.getTag(R.string.tag_key)==null) return true;

        String a = this.toString();

        // you may need the x/y location
        int x = (int)event.getX();
        int y = (int)event.getY();

        // put your code in here to handle the event
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                pickObject();
                break;
        }

        // tell the View to redraw the Canvas
        invalidate();

        // tell the View that we handled the event
        return true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        parentHeight = ((ViewGroup)this.getParent()).getHeight();
        parentWidth = ((ViewGroup)this.getParent()).getWidth();
        canvas.save();

        float currentX = this.getX();
        float currentY = this.getY();

        currentX += speedX;
        currentY += speedY;

        if(currentX - getWidth() < 0 || currentX + getWidth() > parentWidth) {
            speedX *= -1;
        }
        else if(currentY - getHeight() < 0 || currentY + getHeight() > parentHeight) {
            speedY *= -1;
        }

        this.setY(currentY);
        this.setX(currentX);

        invalidate();

        canvas.restore();
    }

}
