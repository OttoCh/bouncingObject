package com.example.ottochristianto.hiddenobject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    int usedSpirte[] = new int[] {
            R.drawable.beer0001,
            R.drawable.box_gift,
            R.drawable.box_wooden,
            R.drawable.chalice0001,
            R.drawable.bottle_health_001,
            R.drawable.bottle_mana_001,
            R.drawable.bench0001
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomizeSpriteCount();
        ImageView title_icon = findViewById(R.id.title_icon);
        floating(title_icon);
    }

    public void openNewGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void quitGame(View view) {
        finish();
    }

    private void floating(View view){
        Animation animation =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.floating_title);
        animation.setRepeatCount(10);
        view.startAnimation(animation);
    }

    private void randomizeSpriteCount() {
        Random rand = new Random();
        int randomNumb = rand.nextInt(usedSpirte.length);

        if(randomNumb < usedSpirte.length && randomNumb >= 0) {
            ImageView title_icon = findViewById(R.id.title_icon);
            title_icon.setImageResource(usedSpirte[randomNumb]);
        }
    }
}
