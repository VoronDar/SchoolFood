package com.astery.ccabp.view.activities.utilities;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.astery.ccabp.R;

/**
 * Класс, в котором прописаны анимации раскрытия/закрытия иконки стрелки
 * (стрелка используется в блоках, которые могут открываться и закрываться)
 */
public class ArrowAnimate {

    public static int DURATION = 400;


    public static void open(View view, Context context){
        AnimationSet set = new AnimationSet(true);

        Animation alpha = new AlphaAnimation(0.3f, 1f);
        alpha.setDuration(DURATION);

        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotation_animation);
        rotation.setDuration(DURATION);

        set.addAnimation(alpha);
        set.addAnimation(rotation);

        view.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setRotation(0);
                view.setAlpha(1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public static void close(View view, Context context) {
        AnimationSet set = new AnimationSet(true);

        Animation alpha = new AlphaAnimation(1f, 0.3f);
        alpha.setDuration(DURATION);

        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotation_animation_back);
        alpha.setDuration(DURATION);

        set.addAnimation(alpha);
        set.addAnimation(rotation);

        view.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setRotation(90);
                view.setAlpha(0.3f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void setClose(View view){
        view.setRotation(90);
        view.setAlpha(0.3f);
    }
    public static void setOpen(View view){
        view.setRotation(0);
        view.setAlpha(1f);
    }
}
