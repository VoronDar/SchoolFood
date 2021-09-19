package com.astery.ccabp.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.astery.ccabp.R;
import com.astery.ccabp.model.local_database.ChildDatabase;
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.model.transport_preferences.TPStorage;
import com.astery.ccabp.view.activities.MainFragments.CalendarFragment;
import com.astery.ccabp.view.activities.MainFragments.HistoryFragment;
import com.astery.ccabp.view.activities.MainFragments.MenuFragment;
import com.astery.ccabp.view.activities.MainFragments.PlanFragment;
import com.astery.ccabp.view.activities.MainFragments.ProfileFragment;
import com.astery.ccabp.view.activities.MainFragments.SceduleFragment;
import com.astery.ccabp.view.activities.MainFragments.TaskFillerFragment;
import com.astery.ccabp.view.activities.utilities.ConnectionSupport;
import com.astery.ccabp.view.activities.utilities.InfoHoldable;
import com.astery.ccabp.view.activities.utilities.InfoPannel;
import com.astery.ccabp.view.activities.utilities.MainFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static com.astery.ccabp.view.activities.RegisterActivity.deleteData;

/**
 * основная активность
 *
 */
public class MainActivity extends AppCompatActivity implements InfoHoldable, View.OnTouchListener {

    /** нужна, чтобы register activity смог понять, если его вызвали из mainActivity*/
    public static boolean isStarted = false;

    private FragmentManager fragmentManager;
    public FragmentTransaction ft;

    public static Steps nowStep;
    private MainFragment fragment;

    public ArrayList<Child> children;
    /** true - если план не равен принятому*/
    public static ArrayList<Boolean> isPlanAccepted;
    public static boolean isOneChild;
    public static int childSelected = 0;

    private MaterialToolbar topAppBar;

    public static boolean isLastTask;

    /** Фрагменты и взаимодействие между ними */
    public enum Steps{
        dayPlan{
            @Override
            MainFragment getFragment() {
                isLastTask = true;
                return new TaskFillerFragment();
            }

            @Override
            Steps slideRight() {
                return menu;
            }

            @Override
            Steps slideLeft() {
                return null;
            }

            @Override
            Steps goBack() {
                return calendar;
            }
            @Override
            String getName() {
                return "Заявка";
            }

            @Override
            int getOrder() {
                return 2;
            }
        },
        calendar{
            @Override
            MainFragment getFragment() {
                return new CalendarFragment();
            }

            @Override
            Steps slideRight() {
                return scedule;
            }

            @Override
            Steps slideLeft() {
                return dayPlan;
            }

            @Override
            Steps goBack() {
                return dayPlan;
            }
            @Override
            String getName() {
                return "Календарь";
            }
            @Override
            int getOrder() {
                return 0;
            }
        },
        menu{
            @Override
            MainFragment getFragment() {
                return new MenuFragment(getWeekDay(), false);
            }

            @Override
            Steps slideRight() {
                return scedule;
            }

            @Override
            Steps slideLeft() {
                return dayPlan;
            }

            @Override
            Steps goBack() {
                if (isLastTask)
                    return dayPlan;
                return weekPlan;
            }
            @Override
            String getName() {
                return "Меню";
            }
            @Override
            int getOrder() {
                return 0;
            }
        },
        scedule{
            @Override
            MainFragment getFragment() {
                return new SceduleFragment();
            }

            @Override
            Steps slideRight() {
                return null;
            }

            @Override
            Steps slideLeft() {
                return null;
            }

            @Override
            Steps goBack() {
                if (isLastTask)
                    return dayPlan;
                return weekPlan;
            }
            @Override
            String getName() {
                return "Расписание";
            }
            @Override
            int getOrder() {
                return 1;
            }
        },
        weekPlan{
            @Override
            MainFragment getFragment() {
                isLastTask = false;
                return new PlanFragment();
            }

            @Override
            Steps slideRight() {
                return Steps.settings;
            }

            @Override
            Steps slideLeft() {
                return menu;
            }

            @Override
            Steps goBack() {
                return null;
            }
            @Override
            String getName() {
                return "План питания";
            }
            @Override
            int getOrder() {
                return 3;
            }
        },
        history{
            @Override
            MainFragment getFragment() {
                isLastTask = false;
                return new HistoryFragment();
            }

            @Override
            Steps slideRight() {
                return Steps.settings;
            }

            @Override
            Steps slideLeft() {
                return menu;
            }

            @Override
            Steps goBack() {
                return settings;
            }
            @Override
            String getName() {
                return "История";
            }
            @Override
            int getOrder() {
                return 4;
            }
        },
        settings{
            @Override
            MainFragment getFragment() {
                return new ProfileFragment();
            }

            @Override
            Steps slideRight() {
                return Steps.dayPlan;
            }

            @Override
            Steps slideLeft() {
                return Steps.scedule;
            }

            @Override
            Steps goBack() {
                return null;
            }
            @Override
            String getName() {
                return "Настройки";
            }
            @Override
            int getOrder() {
                return 4;
            }
        };
        abstract MainFragment getFragment();
        abstract Steps slideRight();
        abstract Steps slideLeft();
        abstract Steps goBack();
        abstract String getName();
        abstract int getOrder();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);


        super.findViewById(R.id.everything).setOnTouchListener(this);
        InfoPannel.setClickable(this);

        children = ChildDatabase.getInstance(getApplicationContext()).getEverything();
        ChildDatabase.getInstance(getApplicationContext()).close();

        isOneChild = children.size()<=1;

        isPlanAccepted = new ArrayList<>();
        CalendarFragment.tasks = new ArrayList<>();
        for (int i = 0; i < children.size() ; i++){
            isPlanAccepted.add(false);
            CalendarFragment.tasks.add(null);
        }


        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();

        BottomNavigationView appBar = super.findViewById(R.id.bottom_navigation);
        appBar.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        if (children.size() == 0){
            appBar.findViewById(R.id.tasks).setAlpha(0.5f);
            appBar.findViewById(R.id.scedule).setAlpha(0.5f);
            appBar.findViewById(R.id.bt_menu).setAlpha(0.5f);
            appBar.findViewById(R.id.bt_plan).setAlpha(0.5f);
        }

        appBar.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorMenuLabel)));
        appBar.setOnNavigationItemSelectedListener(item -> {
            Log.i("main", "clicked " + item.getItemId());
            InfoPannel.closePanel(MainActivity.this, () -> {
                switch (item.getItemId()) {
                    case R.id.tasks:
                        if (children.size() > 0)
                            slideFragment(Steps.dayPlan);
                        break;
                    case R.id.bt_menu:
                        if (children.size() > 0)
                            slideFragment(Steps.menu);
                        break;
                    case R.id.scedule:
                        if (children.size() > 0)
                            slideFragment(Steps.scedule);
                        break;
                    case R.id.profile:
                        slideFragment(Steps.settings);
                        break;
                    case R.id.bt_plan:
                        slideFragment(Steps.weekPlan);
                        break;
                    default:
                        break;
                }
            });

            return true;
        });

        if (children.size() > 0)
            nowStep = Steps.dayPlan;
        else{
            nowStep = Steps.settings;
        }


        fragment = nowStep.getFragment();
        ft.add(R.id.fragment_place, fragment);
        ft.commit();
        ConnectionSupport.setFragment(nowStep);

        topAppBar = super.findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(view -> onBackPressed());

        topAppBar.setOnMenuItemClickListener(item -> {
            if (R.id.settings == item.getItemId()){

                // sign out
                InfoPannel.openPanel(MainActivity.this,
                        getResources().getString(R.string.info_sign_out),
                        getResources().getString(R.string.bt_info_sign_out),
                        () -> {
                            deleteData(getApplicationContext());
                            isStarted = false;
                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
                        }, false);

                return true;
            }
            return false;
        });

        setBackable(false);
        setSettingsVisible();
        setName();


        TPStorage.removeTemperPlan(getApplicationContext(), children);
        TPStorage.removeTemperTask(getApplicationContext(), children);

        isStarted = true;

    }

    @SuppressLint("RestrictedApi")
    public void setTaskStateForView(boolean isAccepted, boolean isTaskFr){

        if (isTaskFr)
            topAppBar.findViewById(R.id.info).setVisibility(View.VISIBLE);
        else {
            topAppBar.findViewById(R.id.info).setVisibility(View.GONE);
            return;
        }
        if (isAccepted){
            ((ActionMenuItemView)topAppBar.findViewById(R.id.info)).setTitle("Принята");
        } else
            ((ActionMenuItemView)topAppBar.findViewById(R.id.info)).setTitle("Закроется в 9:00");
    }


    private void setName(){
        topAppBar.setTitle(nowStep.getName());
    }

    private boolean isBackable = false;

    private void setBackable(boolean isBack) {
        if (isBack) {
            topAppBar.setNavigationIcon(R.drawable.button_back);
        } else{
            topAppBar.setNavigationIcon(null);
        }

        this.isBackable = isBack;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TPStorage.removeTemperPlan(getApplicationContext(), children);
        TPStorage.removeTemperTask(getApplicationContext(), children);
        ChildDatabase.getInstance(getApplicationContext()).close();
    }

    public void setSettingsVisible() {
        if (nowStep == Steps.settings)
            topAppBar.findViewById(R.id.settings).setVisibility(View.VISIBLE);
        else
            topAppBar.findViewById(R.id.settings).setVisibility(View.GONE);
    }
    public void slideFragment(@NonNull Steps step){

        if (step == nowStep) return;

        boolean isRight = (nowStep.getOrder() <= step.getOrder());

        this.fragment = step.getFragment();
        this.nowStep = step;
        ConnectionSupport.setFragment(nowStep);


        setSettingsVisible();
        setBackable(false);
        setName();

        ft = fragmentManager.beginTransaction();
        if (!isRight)
            ft.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
            else
            ft.setCustomAnimations(R.anim.slide_right_reverse, R.anim.slide_left_reverse);
        ft.replace(R.id.fragment_place, this.fragment);
        ft.commit();
    }

    public void slideFragment(@NonNull Steps step, MainFragment fragment){

        step.getFragment();

        boolean isRight = (nowStep.getOrder() <= step.getOrder());

        this.fragment = fragment;
        setBackable(true);
        this.nowStep = step;
        ConnectionSupport.setFragment(nowStep);

        setSettingsVisible();
        setName();

        ft = fragmentManager.beginTransaction();
        if (!isRight)
            ft.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        else
            ft.setCustomAnimations(R.anim.slide_right_reverse, R.anim.slide_left_reverse);
        ft.replace(R.id.fragment_place, this.fragment);
        ft.commit();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

        if (!isBackable) return;

        if (nowStep.goBack() != null)
        slideFragment(nowStep.goBack());
    }

    public int dp(int dp){
        return dp * (getApplicationContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public void addNewChild(){
        ConnectionSupport.setFragment(null);
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public View findViewById(int id) {
        return super.findViewById(id);
    }



    /** Если на layout возможно будет вызываться infoPanel,
     * то двужущиеся объекты должны вызвать onTouch(null, null); при движении
     * (recyclerview - onDragListener и onFlingListener)
     * все остальное - onClickListener
     *
     * На данный момент вызывается только из профиля
     **/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("menu", "touch");
        if (event == null){
            InfoPannel.closePanel(this, null);
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                        event.getAction() == MotionEvent.ACTION_BUTTON_PRESS ||
                                event.getAction() == MotionEvent.ACTION_SCROLL
                                ){
            if (v.getId() != R.id.panel) {
                InfoPannel.closePanel(this, null);
            }
        }
        return true;
    }


    private static int getWeekDay(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        // опасна - пока что оно просто задает завтрашее число
        calendar.add(Calendar.DATE, 1);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            calendar.add(Calendar.DATE, 1);

        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
