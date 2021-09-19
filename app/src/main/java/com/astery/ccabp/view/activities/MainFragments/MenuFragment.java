package com.astery.ccabp.view.activities.MainFragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.model.cloud_database.CloudDatabase;
import com.astery.ccabp.model.cloud_database.pogo.PostChild;
import com.astery.ccabp.model.cloud_database.pogo.PostMenu;
import com.astery.ccabp.model.local_database.MenuDatabase;
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.model.things.Menu;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.astery.ccabp.view.activities.MainActivity;
import com.astery.ccabp.view.activities.utilities.ConnectionSupport;
import com.astery.ccabp.view.activities.utilities.DialogListShower;
import com.astery.ccabp.view.activities.utilities.MainFragment;
import com.astery.ccabp.view.adapters.MenuAdapter;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static com.astery.ccabp.model.cloud_database.CloudUtils.hasConnection;
import static com.astery.ccabp.model.local_database.things.ChildContract.ChildEntry.TABLE_NAME;
import static com.astery.ccabp.view.activities.MainActivity.childSelected;

public class MenuFragment extends MainFragment {

    private TextView dayView;
    private int day;
    private int homeId = 0;
    private int daysInCycle = 0;
    public String today = null;
    public static int getWeekDay;
    public static int getWeek;

    public int weekDay = -1;

    public MenuFragment(int day, boolean isOpenWeekDay){
        if (isOpenWeekDay) {
            this.weekDay = day;
        } else{
            this.day = day;
        }
    }
    public MenuFragment(){
    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);


    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dayView = view.findViewById(R.id.name);

        homeId = getNowChild(childSelected).getHomeId();
        daysInCycle = TransportPreferences.getDaysInCycle(getContext(), getNowChild(childSelected).getHomeId());

        if (weekDay >= 0) {
            setMenuByWeek();
            dayView.setText(Menu.ComeDayToString(day));
        }
        else {
            dayView.setText(Menu.DayIntToString(day));
            setMenu();
        }

        dayView.setOnClickListener(view1 -> requireView().findViewById(R.id.place).performClick());
        view.findViewById(R.id.place).setOnClickListener(view12 -> {
            final ArrayList<SimpleUnit> list = new ArrayList<>();

            for (int j = 0; j <= (daysInCycle-1)/7; j++){
            for (int i = 2; i <= 7; i++){
                list.add(new SimpleUnit(Menu.DayIntToString(i) + " " + (j + 1)));
            }
            }

            DialogListShower.show(getActivity(), list, position -> {
                dayView.setText(list.get(position).name);
                MenuFragment.this.day = position;
                setMenuByWeek();
            });
        });

        TextView personView = view.findViewById(R.id.child_name);


        if (!MainActivity.isOneChild) {

            personView.setText(getNowChild(childSelected).getName()
                    +  " " + getNowChild(childSelected).getsName());


            personView.setOnClickListener(v -> view.findViewById(R.id.child_place).performClick());
            view.findViewById(R.id.child_place).setOnClickListener(view12 -> {
                final ArrayList<SimpleUnit> list = new ArrayList<>();

                for (int i = 0; i < ((MainActivity) requireActivity()).children.size(); i++){
                    list.add(new SimpleUnit(((MainActivity)requireActivity()).children.get(i).getName()
                            + " " + ((MainActivity)requireActivity()).children.get(i).getsName()));
                }

                DialogListShower.show(getActivity(), list, position -> {
                    personView.setText(list.get(position).name);
                    childSelected = position;

                    homeId = getNowChild(childSelected).getHomeId();
                    daysInCycle = TransportPreferences.getDaysInCycle(getContext(), getNowChild(childSelected).getHomeId());

                    setMenuByWeek();
                });
            });
        } else{
            view.findViewById(R.id.child_place).setVisibility(View.GONE);
        }
    }


    private String getToday() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH)+1) + "." + (calendar.get(Calendar.DAY_OF_MONTH));
    }


    private void setMenu(){
        // три вида получения меню - с даты заявки, на сегодня и на дату через выбор
        String day;
        if (today == null)  day = getToday();
        else day = today;

        if (!hasConnection(getContext())){
            setLocalMenuByDay(day, homeId);
            return;
        }

        CloudDatabase db = new CloudDatabase(getContext());
        db.getMenu(homeId, day, new CloudDatabase.MenuGettable() {
            @Override
            public void onFailure() {
                if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                        MenuFragment.this))return;
                if (getContext() != null)
                    setLocalMenuByDay(day, homeId);
            }

            @Override
            public void onSuccess(ArrayList<Menu> menus) {
                if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                        MenuFragment.this))return;
                MenuAdapter adapter = new MenuAdapter(getContext(), menus);
                adapter.notifyDataSetChanged();

                putMenu(menus);

                dayView.setText(Menu.ComeDayToString(getWeekDay) + " " + (getWeek + 1));
            }
        });
    }

    private void setMenuByWeek(){
        // три вида получения меню - с даты заявки, на сегодня и на дату через выбор

        if (!hasConnection(getContext())){
            setLocalMenuByWeek(day/7, day%6, homeId);
            return;
        }

        CloudDatabase db = new CloudDatabase(getContext());
        db.getMenu(homeId, day%6, day/7, new CloudDatabase.MenuGettable() {
            @Override
            public void onFailure() {
                if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                        MenuFragment.this))return;
                if (getContext() != null)
                    setLocalMenuByWeek(day/7, day%6, homeId);
            }

            @Override
            public void onSuccess(ArrayList<Menu> menus) {
                if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                        MenuFragment.this))return;

                putMenu(menus);
                dayView.setText(Menu.ComeDayToString(day%6) + " " + (day/7 + 1));
            }
        });
    }

    private Child getNowChild(int i){
        return ((MainActivity) requireActivity()).children.get(i);
    }

    private void setLocalMenuByWeek(int week, int day, int homeId){

        MenuDatabase db = MenuDatabase.getInstance(getContext());

        PostMenu m = db.getMenuToday(week, day, homeId, false);
        if (m == null){
            if (adapter != null)
                adapter.delete();
            Toast.makeText(getContext(), "Не удалось получить данные", Toast.LENGTH_SHORT).show();
            //dayView.setText(Menu.ComeDayToString(getWeekDay) + " " + (getWeek + 1));
            dayView.setText(Menu.ComeDayToString(day) + " " + (week + 1));
            return;
        }
        ArrayList<Menu> menu = Menu.convertFromPost(m);
        putMenu(menu);
        dayView.setText(Menu.ComeDayToString(day) + " " + (week + 1));
    }


    private void setLocalMenuByDay(String sday, int homeId){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {

            Log.i("menu", sday);

            String startDate = TransportPreferences.getStartCycle(getContext(), homeId);
            Date date1 = dateFormat.parse(sday);
            Date date2 = dateFormat.parse(startDate);
            long milliseconds = date1.getTime() - date2.getTime();
            int days = (int) (milliseconds / (24 * 60 * 60 * 1000));

            int cycle = TransportPreferences.getDaysInCycle(getContext(), homeId);
            int cycleDay = days%cycle;

            int week = cycleDay/7;
            int day = cycleDay%6;

            Calendar calendar = new GregorianCalendar(Integer.parseInt(startDate.split("\\.")[0]),
                    Integer.parseInt(startDate.split("\\.")[1]) ,
                            Integer.parseInt(startDate.split("\\.")[2]));
            //int nowDay = calendar.get(Calendar.DAY_OF_WEEK)-2 + day;

            /*
            if (nowDay >=6) {
                week += 1;
                nowDay -= 7;
            }
            *
             */

            if (day >= 6){
                week += 1;
                day -= 7;
            }

            Log.i("menu", startDate);
            Log.i("menu", cycle + " " + days);
            //Log.i("menu", week + " " + nowDay + " " + day);
            Log.i("menu", cycleDay + "");

            ArrayList<PostMenu> menus = MenuDatabase.getInstance(getContext()).getEverything();
            for (PostMenu m: menus){
                Log.i("menu", m.getWeek() + "" + m.getWeekday() + "" + m.isYoung() + "" + m.getHomeId());
            }

            PostMenu m = MenuDatabase.getInstance(getContext()).getMenuToday(week, day, homeId, true);
            if (m == null){
                if (adapter != null)
                    adapter.delete();
                Toast.makeText(getContext(), "Не удалось получить данные", Toast.LENGTH_SHORT).show();
                dayView.setText(Menu.ComeDayToString(day) + " " + (week + 1));
                return;
            }
            ArrayList<Menu> menu = Menu.convertFromPost(m);
            putMenu(menu);
            dayView.setText(Menu.ComeDayToString(day) + " " + (week + 1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private MenuAdapter adapter;
    private void putMenu(ArrayList<Menu> menus){

        adapter = new MenuAdapter(getContext(), menus);

        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}

