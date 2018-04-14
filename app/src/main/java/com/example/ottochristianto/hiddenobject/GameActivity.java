package com.example.ottochristianto.hiddenobject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GameActivity extends AppCompatActivity {

    int existingSprite[] = new int[] {
            R.drawable.arch_001,
            R.drawable.bed0007,
            R.drawable.bench0001,
            R.drawable.beer0001,
            R.drawable.bottle_health_001,
            R.drawable.bottle_mana_001,
            R.drawable.box_cardboard,
            R.drawable.box_gift,
            R.drawable.box_wooden,
            R.drawable.bridge_wooden,
            R.drawable.bridge_wooden_2,
            R.drawable.cage0001,
            R.drawable.chair0007,
            R.drawable.chalice0001
    };

    int getObject = 0;
    int totalMistake = 0;
    int maximumMistake = 5;
    int usedSprite[] = new int[5];

    final int totalBouncingObject = 6;

    ArrayList<ImageView> neededObject = new ArrayList<ImageView>();
    ArrayList<floating_object> floatingObject = new ArrayList<floating_object>();

    View exitInflated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        InitGame();
    }

    private void InitGame() {
        getObject = 0;
        totalMistake = 0;
        populateObjectList();
        randomizeNeededObject(neededObject, existingSprite);
        spawnObject(totalBouncingObject, existingSprite);
        //make sure object list is findable
        untransparentNeededObject();
    }

    private void untransparentNeededObject() {
        for(Iterator<ImageView> i = neededObject.iterator(); i.hasNext();) {
            ImageView icon = i.next();
            icon.setAlpha(1f);
        }
    }

    private void randomizeNeededObject(ArrayList<ImageView> neededObject, int existingSprite[]) {
        //shuffle the sprite array
        Random rand = new Random();
        int low = 0;
        int high = existingSprite.length;
        for(int i = 0; i<existingSprite.length; i++) {
            int r = rand.nextInt(high - low) + low;
            int temp = existingSprite[i];
            existingSprite[i] = existingSprite[r];
            existingSprite[r] = temp;
        }

        //assign the sprite consecutively
        int index = 0;
        for(Iterator<ImageView> i = neededObject.iterator(); i.hasNext();) {
            ImageView icon = i.next();
            icon.setImageResource(existingSprite[index]);
            icon.setTag(existingSprite[index], new Object());
            index++;
        }
    }

    private void populateObjectList() {
        neededObject.add((ImageView) findViewById(R.id.find_icon_1));
        neededObject.add((ImageView) findViewById(R.id.find_icon_2));
        neededObject.add((ImageView) findViewById(R.id.find_icon_3));
        neededObject.add((ImageView) findViewById(R.id.find_icon_4));
        neededObject.add((ImageView) findViewById(R.id.find_icon_5));
    }

    public boolean objectPickedUp(int pickedSprite) {
        for(Iterator<ImageView> i = neededObject.iterator(); i.hasNext();) {
            ImageView icon = i.next();
            Drawable.ConstantState constantState;
            Context context = this;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                constantState = context.getResources().getDrawable(pickedSprite, context.getTheme()).getConstantState();
            }
            else {
                constantState = context.getResources().getDrawable(pickedSprite).getConstantState();
            }

            if(icon.getAlpha() != 0.5f && icon.getDrawable().getConstantState() == constantState) {
                //buat agar si icon jadi transparan
                icon.setAlpha(0.5f);
                getObject++;
                if(getObject >= neededObject.toArray().length) {
                    Thread thread = new Thread();
                    thread.start();
                    gameDone();
                }
                return true;
            }

//            if(icon.getResources().getDrawable(icon)== pickedSprite) {
//                return true;
//            }
        }
        totalMistake++;
        if(totalMistake>maximumMistake)
            gameLose();

        return false;
    }

    private void gameLose() {
        neededObject.clear();
        TextView textView = findViewById(R.id.game_finish);
        textView.setText(R.string.you_lose);

        //lakukan pembersihan semua floating_object yg ada di scene
        deleteAllFloatingObject((ViewGroup) findViewById(R.id.relativeLayout));

        showExitBox(false);
    }

    private void deleteAllFloatingObject(ViewGroup parentLayout) {
        //catat dulu semua yang mau dihapus
        final int childCount = parentLayout.getChildCount();
        View[] deleteViewList = new View[childCount];
        int index = 0;
        for(int i=0; i<childCount;i++) {
            View v = parentLayout.getChildAt(i);
            if(v.getTag() == "floating_object") {
                //parentLayout.removeView(v);
                deleteViewList[index] = v;
                index++;
            }
        }

        //hapus semuanya
        for(int i=0; i<index; i++) {
            parentLayout.removeView(deleteViewList[i]);
        }
    }

    private void gameDone() {
        neededObject.clear();
        TextView textView = findViewById(R.id.game_finish);
        textView.setText(R.string.you_win);
        showExitBox(true);
    }

    private void showExitBox(boolean win) {
        if(exitInflated == null) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.exit_box_stub);
            viewStub.setLayoutResource(R.layout.exit_box);
            exitInflated = viewStub.inflate();
        }
        else {
            exitInflated.setVisibility(View.VISIBLE);
        }

        TextView messageText = exitInflated.findViewById(R.id.Message);
        Button retryButton = exitInflated.findViewById(R.id.RetryButton);
        if(win) {
            messageText.setText(R.string.you_win);
            retryButton.setText(R.string.continue_button);
        }
        else {
            messageText.setText(R.string.you_lose);
            retryButton.setText(R.string.retry_button);
        }
    }

    private void spawnObject(int totalSpawn, int existingSprite[]) {
        for(int i=0; i<totalSpawn; i++) {
            floating_object floatingObject = new floating_object(getApplicationContext());
            floatingObject.Init(existingSprite[i], i, this);
            RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);

            Random rand = new Random();

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.leftMargin = (int)(rand.nextFloat() * 900f);
            layoutParams.topMargin = (int)(rand.nextFloat() * 900f);
            layoutParams.width = 200;
            layoutParams.height = 200;

            //floatingObject.setLayoutParams(layoutParams);
            this.floatingObject.add(floatingObject);
            relativeLayout.addView(floatingObject, layoutParams);
        }
    }


    public void RetryGame(View view) {
        exitInflated.setVisibility(View.INVISIBLE);
        InitGame();
    }

}
