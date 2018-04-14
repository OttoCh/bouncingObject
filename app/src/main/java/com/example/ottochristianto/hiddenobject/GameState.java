package com.example.ottochristianto.hiddenobject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.view.View;

public class GameState {

    int screen_width;
    int screen_height;

    View target;
    float velocity_X = 10;
    float velocity_Y = 5;

    float target_X;
    float target_Y;

    public GameState(int height, int width, View target) {
        screen_height = height;
        screen_width = width;
        this.target = target;
    }

    public void update() {
        //kalkulasi posisi imageview harusnya ada dimana
        float target_X = target.getX();
        float target_Y = target.getY();

        target_X += velocity_X;
        target_Y += velocity_Y;

        //Pantulkan
        if(target_X > screen_width || target_X < 0)
            velocity_X *= -1;
        else if(target_Y > screen_height || target_Y < 0)
            velocity_Y *= -1;

    }

    public void draw(Canvas canvas, Paint paint) {
        //set X dan set Y posisi setiap image View
        target.setX(target_X);
        target.setY(target_Y);
    }
}
