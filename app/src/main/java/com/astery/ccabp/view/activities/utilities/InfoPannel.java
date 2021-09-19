package com.astery.ccabp.view.activities.utilities;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.astery.ccabp.R;

import static android.view.View.GONE;

public class InfoPannel {

    public static final int TASK_FILL_HELP = 0;
    public static final int PLAN_FILLER_HELP = 1;
    public static final int CALENDAR_HELP = 2;

    private static boolean isPanel = false;
    private static OnInfoClickListener listener = null;

    public static void openPanel(InfoHoldable activity, String title, String buttonText, OnInfoClickListener listener, boolean keepListener){
        if (isPanel){
            closePanel(activity, null);
            return;
        }
        isPanel = true;
        if (keepListener)
            InfoPannel.listener = listener;
        else
            InfoPannel.listener = null;
        View panel = activity.findViewById(R.id.panel);
        Button button = (Button)activity.findViewById(R.id.bt_panel);
        panel.setVisibility(View.VISIBLE);

        Animation a = new TranslateAnimation(0, 0,  activity.dp(400), 0);
        a.setDuration(400);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        panel.startAnimation(a);

        ((TextView)activity.findViewById(R.id.text)).setText(title);
        button.setText(buttonText);
        button.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick();
            closePanel(activity, null);
        });
    }
    public static void closePanel(InfoHoldable activity, OnPanelCloseListener l){
        if (!isPanel){
            if (l != null)
                l.onClose();
            return;
        }

        if (listener != null){
            listener.onClick();
        }

        View panel = activity.findViewById(R.id.panel);
        Button button = (Button)activity.findViewById(R.id.bt_panel);

        Animation a = new TranslateAnimation(0, 0,  0, activity.dp(500));
        a.setDuration(200);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                button.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                panel.setVisibility(GONE);
                if (l != null)
                    l.onClose();
                isPanel = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        panel.startAnimation(a);
    }


    /** вызывается, когда на панели прожимается кнопка*/
    public interface OnInfoClickListener{
        void onClick();
    }
    /** вызывается, когда разрешено управлять какими-либо событиями, после того как закроется панель*/
    public interface OnPanelCloseListener{
        void onClose();
    }

    public static void setClickable(InfoHoldable activity){
        View panel = activity.findViewById(R.id.panel);
        panel.setOnTouchListener((View.OnTouchListener) activity);
    }
}
