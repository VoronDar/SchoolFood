package com.astery.ccabp.view.activities.MainFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.model.cloud_database.CloudDatabase;
import com.astery.ccabp.model.cloud_database.pogo.GotHolyday;
import com.astery.ccabp.model.local_database.HolydayDatabase;
import com.astery.ccabp.model.local_database.TaskDatabase;
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.model.things.Task;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.astery.ccabp.view.activities.MainActivity;
import com.astery.ccabp.view.activities.utilities.DialogListShower;
import com.astery.ccabp.view.activities.utilities.InfoHoldable;
import com.astery.ccabp.view.activities.utilities.InfoPannel;
import com.astery.ccabp.view.activities.utilities.MainFragment;
import com.astery.ccabp.view.adapters.CalendarAdapter;
import com.astery.ccabp.view.adapters.units.DayUnit;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.view.View.GONE;
import static com.astery.ccabp.model.cloud_database.CloudUtils.hasConnection;
import static com.astery.ccabp.model.cloud_database.CloudUtils.isConnection;
import static com.astery.ccabp.view.activities.MainActivity.childSelected;

public class CalendarFragment extends MainFragment {

    private TextView personView;
    public static ArrayList<ArrayList<Task>> tasks;
    public static ArrayList<ArrayList<GotHolyday>> holydays;

        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_calendar, container, false);


    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        personView = view.findViewById(R.id.name);


        if (!TransportPreferences.getInfoState(getContext(), InfoPannel.CALENDAR_HELP)) {
            InfoPannel.openPanel((InfoHoldable) requireActivity(),
                    "Вы можете изменять заявки на несколько дней вперед и посмотреть уже принятые заявки",
                    "Ок", () -> TransportPreferences.setInfoState(getContext(), true, InfoPannel.CALENDAR_HELP), true);
        }

        if (!MainActivity.isOneChild){

            personView.setText(getNowChild(childSelected).getName()
                    +  " " + getNowChild(childSelected).getsName());


            personView.setOnClickListener(view12 ->
                    requireView().findViewById(R.id.place).performClick());
            view.findViewById(R.id.place).setOnClickListener(view1 -> {
                final ArrayList<SimpleUnit> list = new ArrayList<>();

                for (int i = 0; i < ((MainActivity) requireActivity()).children.size(); i++){
                    list.add(new SimpleUnit(((MainActivity)requireActivity()).children.get(i).getName()
                            + " " + ((MainActivity)requireActivity()).children.get(i).getsName()));
                }

                DialogListShower.show(getActivity(), list, position -> {
                    personView.setText(list.get(position).name);
                    childSelected = position;
                    setCalendar();
                });
            });
        } else{
            requireView().findViewById(R.id.place).setVisibility(GONE);
        }


        setCalendar();

        view.findViewById(R.id.help).setOnClickListener(v -> {
            ((MainActivity)requireActivity()).onTouch(null, null);
            InfoPannel.openPanel((InfoHoldable) getActivity(), "Вы можете изменять заявки на несколько дней вперед и посмотреть уже принятые заявки",
                    "Ок", null, false);
        });

    }


    private void setCalendar(){
        final Calendar calendar = Calendar.getInstance();
        ((TextView)requireView().findViewById(R.id.month)).setText(getCalendarMonth(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1));
            CloudDatabase db1 = new CloudDatabase(requireContext(), new CloudDatabase.Loadable() {
                @Override
                public void onFailure() {
                    Log.i("main", "F");
                }
                @Override
                public void onSuccess() {
                    CloudDatabase db = new CloudDatabase(requireContext(), new CloudDatabase.Loadable() {
                        @Override
                        public void onFailure() {
                            Log.i("main", "F");
                        }
                        @Override
                        public void onSuccess() {
                            ArrayList<DayUnit> units = new ArrayList<>();
                            for (Task task: tasks.get(childSelected)){
                                if (task.getTimeStamp() == null || task.getTimeStamp().length() == 0){
                                    continue;
                                }
                                Log.i("unit", "last:" + task);
                                DayUnit.DayState state = (!task.isEnable())? DayUnit.DayState.no :(task.isTask())? DayUnit.DayState.task : DayUnit.DayState.plan;
                                Log.i("unit", "now:" + state);
                                units.add(new DayUnit(task.getDate().get(Calendar.DAY_OF_MONTH), !isHolyday(task.getDate().get(Calendar.DAY_OF_MONTH)), state, task.isAccepted()));
                            }
                            RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
                            CalendarAdapter adapter = new CalendarAdapter(units, (MainActivity) requireActivity(), requireContext());
                            adapter.notifyDataSetChanged();

                            adapter.setBlockListener(position -> {
                                Calendar cal = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                                        units.get(position).day);
                                ((MainActivity) requireActivity()).slideFragment(MainActivity.Steps.dayPlan, new TaskFillerFragment(cal));
                            });
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 6, RecyclerView.HORIZONTAL, false));
                        }
                        @Override
                        public void onError() {
                        }
                    });
                    if (isConnection(requireContext())) {
                        if (tasks.get(childSelected) == null || tasks.get(childSelected).size() < 2) {
                            db.getMonthTask(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), getNowChild(childSelected).getId(), childSelected);
                        } else
                            db.loadable.onSuccess();
                    } else
                        db.loadable.onSuccess();
                }
                @Override
                public void onError() {
                }
            });


        if (tasks == null){
            tasks = new ArrayList<>();
            for (Child ignored : ((MainActivity)requireActivity()).children){
                tasks.add(null);
            }
        }
        if (holydays == null){
            holydays = new ArrayList<>();
            for (Child ignored : ((MainActivity)requireActivity()).children){
                holydays.add(null);
            }
        }

        if (!hasConnection(requireContext())){

            Log.i("main", "no connect");

            HolydayDatabase dbh = HolydayDatabase.getInstance(requireContext());
            dbh.open();
            CalendarFragment.holydays.set(childSelected, dbh.getHolyday(Integer.parseInt(getNowChild(childSelected).getId()),
                    GotHolyday.generateMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))));
            dbh.close();

            TaskDatabase db = TaskDatabase.getInstance(requireContext());
            db.open();
            int end = 31;
            ArrayList<Task> tasks =
                    db.getTasksByPeriod(Integer.parseInt(getNowChild(childSelected).getId()),
                            new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 0),
                            new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), end));

            db.close();

            CalendarFragment.tasks.set(childSelected, tasks);

            db1.loadable.onSuccess();
        }

        else {
            Log.i("main", "is connect");

            if (holydays.get(childSelected) == null) {
                db1.getMonthHolyday(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), getNowChild(childSelected).getId(), childSelected);
            } else
                db1.loadable.onSuccess();
        }

    }

    private boolean isHolyday(int day){
            if (holydays == null || holydays.get(childSelected) == null)
                return false;
            for (GotHolyday holyday: holydays.get(childSelected)){
                if (holyday.getStart() <= day && holyday.getEnd() >= day){
                    return true;
                }
            }
            return false;
    }

    private Child getNowChild(int i){
        return ((MainActivity) requireActivity()).children.get(i);
    }

    private String getCalendarMonth(int year, int month){
            String m = year + ": ";
            switch (month){
                case 1:
                    return m + "Январь";
                case 2:
                    return m + "Февраль";
                case 3:
                    return m + "Март";
                case 4:
                    return m + "Апрель";
                case 5:
                    return m + "Май";
                case 6:
                    return m + "Июнь";
                case 7:
                    return m + "Июль";
                case 8:
                    return m + "Август";
                case 9:
                    return m + "Сентябрь";
                case 10:
                    return m + "Октябрь";
                case 11:
                    return m + "Ноябрь";
                case 12:
                    return m + "Декабрь";
            }
            throw new RuntimeException();
    }
}

