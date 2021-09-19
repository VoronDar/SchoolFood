package com.astery.ccabp.view.activities.MainFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astery.ccabp.R;
import com.astery.ccabp.model.cloud_database.CloudDatabase;
import com.astery.ccabp.model.local_database.TaskDatabase;
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.model.things.Task;
import com.astery.ccabp.model.transport_preferences.TPStorage;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.astery.ccabp.view.activities.MainActivity;
import com.astery.ccabp.view.activities.utilities.ConnectionSupport;
import com.astery.ccabp.view.activities.utilities.DialogListShower;
import com.astery.ccabp.view.activities.utilities.InfoHoldable;
import com.astery.ccabp.view.activities.utilities.InfoPannel;
import com.astery.ccabp.view.activities.utilities.MainFragment;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.astery.ccabp.model.cloud_database.CloudUtils.hasConnection;
import static com.astery.ccabp.model.cloud_database.CloudUtils.isConnection;
import static com.astery.ccabp.view.activities.MainActivity.childSelected;
import static com.astery.ccabp.view.activities.utilities.DisplayUtils.sizeY;

public class TaskFillerFragment extends MainFragment {

    private CheckBox firstCheck;
    private CheckBox secondCheck;
    private CheckBox thirdCheck;
    private CheckBox fourthCheck;
    private TextView child;
    private Task cachedTask = null;

    private int payment = 0;

    private Calendar date;


    public TaskFillerFragment(Calendar date) {
        this.date = date;
    }

    public TaskFillerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //if (sizeY(getActivity()) < 1300)
        //    return inflater.inflate(R.layout.fragment_main_day_plan_little, container, false);
        return inflater.inflate(R.layout.fragment_main_day_plan, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (!isConnection(requireContext()) && date == null){
            Calendar calendar = Calendar.getInstance();
            if (CalendarFragment.tasks != null && CalendarFragment.tasks.get(childSelected) != null){
                for (int i = 0; i < CalendarFragment.tasks.get(childSelected).size(); i++){
                    if (CalendarFragment.tasks.get(childSelected).get(i).getDate().get(Calendar.DAY_OF_MONTH) > calendar.get(Calendar.DAY_OF_MONTH)){
                        date = CalendarFragment.tasks.get(childSelected).get(i).getDate();
                        break;
                    }
                }
            }

            if (date == null){
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                TaskDatabase db = TaskDatabase.getInstance(requireContext());
                db.open();
                cachedTask = db.getTask(Integer.parseInt(getNowChild(childSelected).getId()), Task.timeToInt(calendar));
                date = calendar;
                db.close();
                if (date != null && cachedTask == null) date = null;
            }
        }


        Log.i("main", date + " " + cachedTask + " ");
        view.findViewById(R.id.school_everything).setOnTouchListener((View.OnTouchListener) requireActivity());
        Task.genPayment(requireContext());

        if (!TransportPreferences.getInfoState(requireContext(), InfoPannel.TASK_FILL_HELP)) {
            InfoPannel.openPanel((InfoHoldable) requireActivity(),
                    "Вы можете заполнить заявку на питание для Вашего ребенка и изменять её в любое время до 9 утра того дня, на который создается заявка",
                    "Ок", () -> TransportPreferences.setInfoState(requireContext(), true, InfoPannel.TASK_FILL_HELP), true);
        }
        firstCheck = view.findViewById(R.id.first_accept);
        secondCheck = view.findViewById(R.id.second_accept);
        thirdCheck = view.findViewById(R.id.third_accept);
        fourthCheck = view.findViewById(R.id.fourth_accept);

        checkConnection();

        child = view.findViewById(R.id.name);
        if (!MainActivity.isOneChild) {

            child.setText(getNowChild(childSelected).getName()
                    + " " + getNowChild(childSelected).getsName());

            child.setOnClickListener(view17 ->{
                    requireView().findViewById(R.id.child).callOnClick();
            });
            view.findViewById(R.id.child).setOnClickListener(view18 -> {
                ((MainActivity) requireActivity()).onTouch(null, null);
                saveTemper();
                final ArrayList<SimpleUnit> list = new ArrayList<>();

                for (int i = 0; i < ((MainActivity) requireActivity()).children.size(); i++){
                    list.add(new SimpleUnit(((MainActivity)getActivity()).children.get(i).getName()
                            + " " + ((MainActivity)getActivity()).children.get(i).getsName()));
                }

                DialogListShower.show(getActivity(), list, position -> {
                    child.setText(list.get(position).name);
                    childSelected = position;
                    // todo а правильно ли?
                    date = null;
                    setChecked();
                    checkConnection();
                });
            });

        } else{
            view.findViewById(R.id.child).setVisibility(GONE);
        }

        setChecked();

        view.findViewById(R.id.buttonNext).setOnClickListener(view16 -> {

            ((MainActivity) requireActivity()).onTouch(null, null);
            view16.setClickable(false);

            CloudDatabase db = new CloudDatabase(requireContext(), new CloudDatabase.Loadable() {
                @Override
                public void onFailure() {
                    if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep)) return;
                    if (requireContext() != null)
                    Toast.makeText(requireContext(), "не удалось отправить данные", Toast.LENGTH_SHORT).show();
                    view16.setClickable(true);
                }

                @Override
                public void onSuccess() {
                    if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep)) return;
                    if (requireContext() != null)
                    Toast.makeText(requireContext(), "данные отправлены\"", Toast.LENGTH_SHORT).show();
                    view16.setClickable(true);

                    //setTaskConfirmed(!MainActivity.isTaskAccepted.get(childSelected));
                    resetTask();
                }

                @Override
                public void onError() {
                    if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep)) return;
                    view16.setClickable(true);
                }
            });


            Task t = saveData(true);

            if (hasConnection(requireContext())) {
                t.setTask(true);
                db.loadTask(t);
            }
            else
                view16.setClickable(true);
        });



        view.findViewById(R.id.bt_menu).setOnClickListener(view1 ->{
            MenuFragment menu = new MenuFragment(getWeekDay(), false);
            menu.today = getDate();
                ((MainActivity)(requireActivity()))
                        .slideFragment(MainActivity.Steps.menu, menu);});

        view.findViewById(R.id.buttonChange).setOnClickListener(v -> setTaskConfirmed(false));

        view.findViewById(R.id.calendar).setOnClickListener(v -> ((MainActivity)(requireActivity()))
                .slideFragment(MainActivity.Steps.calendar, new CalendarFragment()));
    }

    private void genPayment(){
        int pay = 0;
        if (firstCheck.isChecked()) pay += Task.getBreakfastPrice();
        if (secondCheck.isChecked()) pay += Task.getLanchPrice();
        if (thirdCheck.isChecked()) pay += Task.getSnack10Price();
        if (fourthCheck.isChecked()) pay += Task.getSnack15Price();
        this.payment = pay;
        setPayment();
    }

    private Task saveData(boolean isAccepted){

        String day = (date == null) ?
                TransportPreferences.getTodayTaskId(requireContext(), getNowChild(childSelected).getId()).split("\\|")[0]:
                this.date.get(Calendar.YEAR)+ "." + (this.date.get(Calendar.MONTH)+1) + "." + this.date.get(Calendar.DAY_OF_MONTH);

        Task t = new Task(getNowChild(childSelected).getId(),
                day,
                firstCheck.isChecked(),
                secondCheck.isChecked(),
                thirdCheck.isChecked(),
                fourthCheck.isChecked(),
                // нужно посмотреть как правильно отправлять класс
                getNowChild(childSelected).getClassId());
        t.setAccepted(isAccepted);

        if (CalendarFragment.tasks == null){
            CalendarFragment.tasks = new ArrayList<>();
            for (Child ignored : ((MainActivity)requireActivity()).children){
                CalendarFragment.tasks.add(null);
            }
        }

        if (CalendarFragment.tasks.get(childSelected) == null){
            CalendarFragment.tasks.set(childSelected, new ArrayList<>());
        }
        if (CalendarFragment.tasks.get(childSelected).size() == 0){
            CalendarFragment.tasks.get(childSelected).add(t);
        } else{
            for (Task task: CalendarFragment.tasks.get(childSelected)){
                if (task.getTimeStamp() == null || task.getTimeStamp().length() == 0){
                    Log.i("main", "warn");
                    continue;
                }
                if (task.getDate().equals(t.getDate())){
                    task.setBreakfast(t.isBreakfast());
                    task.setLanch(t.isLanch());
                    task.setSnak10(t.isSnak10());
                    task.setSnak15(t.isSnak15());
                    task.setAccepted(t.isAccepted());
                }
            }
        }
        return t;
    }

    /** получить выбранного ребенка */
    private Child getNowChild(int i){
        return ((MainActivity) requireActivity()).children.get(i);
    }

    /** делает кнопки недоступными для неактивированных пользователей*/
    private void setAccessible(boolean isActivated){
        if (!isActivated){
            requireView().findViewById(R.id.blocker).setVisibility(VISIBLE);
            getView().findViewById(R.id.blockerText).setVisibility(VISIBLE);
        }else{
            requireView().findViewById(R.id.blocker).setVisibility(GONE);
            getView().findViewById(R.id.blockerText).setVisibility(GONE);
        }
        getView().findViewById(R.id.buttonChange).setEnabled(isActivated);
        getView().findViewById(R.id.bt_menu).setEnabled(isActivated);
        getView().findViewById(R.id.buttonNext).setEnabled(isActivated);
        getView().findViewById(R.id.first_accept).setEnabled(isActivated);
        getView().findViewById(R.id.second_accept).setEnabled(isActivated);
        getView().findViewById(R.id.third_accept).setEnabled(isActivated);
        getView().findViewById(R.id.fourth_accept).setEnabled(isActivated);
    }

    /** получить заявку текущего выбранного ребенка*/
    private void setChecked(){

        setAccessible(getNowChild(childSelected).isActivated());

        payment = 0;


        CloudDatabase db = new CloudDatabase(requireContext(), new CloudDatabase.Loadable() {
            @Override
            public void onFailure() {
            }

            @Override
            public void onSuccess() {
                if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep)) return;

                // пока что оно в кеше не сохраняет
                {
                    if (date != null){
                        if (cachedTask == null) {

                            Log.i("main", "from calendar");
                            for (Task task : CalendarFragment.tasks.get(childSelected)) {
                                if (task.getDate().equals(date)) {
                                    firstCheck.setChecked(task.isBreakfast());
                                    secondCheck.setChecked(task.isLanch());
                                    thirdCheck.setChecked(task.isSnak10());
                                    fourthCheck.setChecked(task.isSnak15());
                                    if (sizeY(getActivity()) >= 1300)
                                        ((TextView) requireView().findViewById(R.id.title)).setText("Заявка на " + getDateForView(false));
                                    else
                                        ((TextView) requireView().findViewById(R.id.title)).setText(getDateForView(false));
                                    setTaskConfirmed(task.isAccepted());
                                    genPayment();
                                    break;
                                }
                            }
                        } else{
                            firstCheck.setChecked(cachedTask.isBreakfast());
                            secondCheck.setChecked(cachedTask.isLanch());
                            thirdCheck.setChecked(cachedTask.isSnak10());
                            fourthCheck.setChecked(cachedTask.isSnak15());
                            if (sizeY(getActivity()) >= 1300)
                                ((TextView) requireView().findViewById(R.id.title)).setText("Заявка на " + getDateForView(false));
                            else
                                ((TextView) requireView().findViewById(R.id.title)).setText(getDateForView(false));
                            setTaskConfirmed(cachedTask.isAccepted());
                            genPayment();
                        }
                    }

                    else if (getNowTask(getDate())!= null && !getNowTask(getDate()).isAccepted()) {
                        Log.i("main", "from cache");
                        if (CalendarFragment.tasks != null && CalendarFragment.tasks.get(childSelected) != null){
                            for (Task task: CalendarFragment.tasks.get(childSelected)){
                                if (task.getTimeStamp().equals(getDate())){
                                    Log.i("main", task.isBreakfast() + ": bool");
                                    firstCheck.setChecked(task.isBreakfast());
                                    secondCheck.setChecked(task.isLanch());
                                    thirdCheck.setChecked(task.isSnak10());
                                    fourthCheck.setChecked(task.isSnak15());
                                    genPayment();
                                }
                            }
                            if (sizeY(getActivity()) >= 1300)
                                ((TextView) requireView().findViewById(R.id.title)).setText("Заявка на " + getDateForView(true));
                            else
                                ((TextView) requireView().findViewById(R.id.title)).setText(getDateForView(true));
                        }
                        return;
                    }
                    else {
                        Log.i("main", "load");
                        CloudDatabase db = new CloudDatabase(requireContext());
                        if (hasConnection(requireContext()))
                            db.getTask(new CloudDatabase.TaskGettable() {
                                @Override
                                public void onSuccess(Task task) {

                                    task.setTask(true);

                                    if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep))
                                        return;
                                    firstCheck.setChecked(task.isBreakfast());
                                    secondCheck.setChecked(task.isLanch());
                                    thirdCheck.setChecked(task.isSnak10());
                                    fourthCheck.setChecked(task.isSnak15());
                                    saveData(true);

                                    //setTaskAccepted();
                                    setTaskConfirmed(true);
                                    genPayment();

                                    if (sizeY(getActivity()) >= 1300)
                                        ((TextView) requireView().findViewById(R.id.title)).setText("Заявка на " + getDateForView(true));
                                    else
                                        ((TextView) requireView().findViewById(R.id.title)).setText(getDateForView(true));

                                }

                                @Override
                                public void onError() {

                                    if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep))
                                        return;
                                    firstCheck.setChecked(false);
                                    secondCheck.setChecked(false);
                                    thirdCheck.setChecked(false);
                                    fourthCheck.setChecked(false);
                                    genPayment();

                                }

                                @Override
                                public void onFailure() {
                                    if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep))
                                        return;
                                    firstCheck.setChecked(false);
                                    secondCheck.setChecked(false);
                                    thirdCheck.setChecked(false);
                                    fourthCheck.setChecked(false);genPayment();


                                }
                            }, getToday(), getNowChild(childSelected).getId(), getNowChild(childSelected).getClassId(), getWeekDay());
                    }

                    // пока так - потом поменяю
                    TransportPreferences.setLastDayPlan(requireContext(), getAbsoluteDay(), getNowChild(childSelected).getId());
                }
            }

            @Override
            public void onError() {

            }
        });


        if (TransportPreferences.getlastPaymentHomeId(requireContext()) == 0 || getNowChild(childSelected).getHomeId() != TransportPreferences.getlastPaymentHomeId(requireContext())){
            if (hasConnection(requireContext())) {
                db.getPayment(getNowChild(childSelected).getHomeId());
                TransportPreferences.setlastPaymentHomeId(requireContext(), getNowChild(childSelected).getHomeId());
            }
        } else{
            db.loadable.onSuccess();
        }

    }

    private Task getNowTask(String date){
        if (CalendarFragment.tasks.get(childSelected) == null) return null;
        for (Task task: CalendarFragment.tasks.get(childSelected)) {
            if (task.getTimeStamp().equals(date)) {
                return task;
            }
        } return null;
    }


    private String getDate(){
        return TransportPreferences.getTodayTaskId(requireContext(), getNowChild(childSelected).getId()).split("\\|")[0];
    }
    private String getDateForView(boolean isToday){

        String when;
        Date date;
        String[] ar = null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
        if (isToday) {
            ar = TransportPreferences.getTodayTaskId(requireContext(), getNowChild(childSelected).getId())
                    .split("\\|")[0].split("\\.");

                date = dateFormat.parse(TransportPreferences.getTodayTaskId(requireContext(), getNowChild(childSelected).getId())
                        .split("\\|")[0]);
        } else {
            ar = new String[]{Integer.toString(this.date.get(Calendar.YEAR)),
                Integer.toString(this.date.get(Calendar.MONTH) + 1),
                Integer.toString(this.date.get(Calendar.DAY_OF_MONTH))};
            date = dateFormat.parse(this.date.get(Calendar.YEAR)+ "." + (this.date.get(Calendar.MONTH)+1) + "." + this.date.get(Calendar.DAY_OF_MONTH));

        }
        }
        catch (ParseException e) {
            return ar[2] + "." + ar[1] + "." + ar[0];
        }
        if (ar[1].length() == 1)
            ar[1] = "0" + ar[1];
        if (ar[2].length() == 1)
            ar[2] = "0" + ar[2];

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date today = cal.getTime();

            long milliseconds = date.getTime() - today.getTime();
            Log.i("main", milliseconds + "");
            int days = (int) (milliseconds / (24 * 60 * 60 * 1000));
            if (days == 0){
                when = "сегодня";
            } else if (days == 1){
                when = "завтра";
            } else{
                when = "позднее";
            }


        return ar[2] + "." + ar[1]  + "."+ ar[0] + " (" + when + ")";
    }


    private Task getNowTaskByDate(){
        return getNowTask((date == null)?getDate():this.date.get(Calendar.YEAR)+ "." + (this.date.get(Calendar.MONTH)+1) + "." + this.date.get(Calendar.DAY_OF_MONTH));
    }

    private void saveTemper(){
        if (getNowTaskByDate() == null) return;
        saveData(getNowTaskByDate().isAccepted());
            TPStorage.saveTemperTask(requireContext(), new Task(getNowChild(childSelected).getId(),
                    TransportPreferences.getTodayTaskId(requireContext(), getNowChild(childSelected).getId()).split("\\|")[0],
                    firstCheck.isChecked(),
                    secondCheck.isChecked(),
                    thirdCheck.isChecked(),
                    fourthCheck.isChecked(), getNowChild(childSelected).getClassId()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveTemper();
    }

    private int getToday(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        // опасна - пока что оно просто задает завтрашее число
        calendar.add(Calendar.DATE, 1);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            calendar.add(Calendar.DATE, 1);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private int getWeekDay(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        // опасна - пока что оно просто задает завтрашее число
        calendar.add(Calendar.DATE, 1);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            calendar.add(Calendar.DATE, 1);

        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /** возвращает текущий календарный день*/
    private int getAbsoluteDay() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        // опасна - пока что оно просто задает завтрашее число
        calendar.add(Calendar.DATE, 1);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            calendar.add(Calendar.DATE, 1);

        //return calendar.get(Calendar.DAY_OF_YEAR) + (calendar.get(Calendar.YEAR) * 365) + isAcceptClose();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return 0 - если заявка на завтра еще открыта, 1 - если уже закрыта
     * */
    private int isAcceptClose(){
        return 0;
    }

    private void setTaskAccepted() {
        ((MainActivity)requireActivity()).setTaskStateForView(false, true);
        getNowTaskByDate().setAccepted(false);
        setTaskConfirmed(false);
    }

    private void resetTask() {
        getNowTaskByDate().setAccepted(true);
        setTaskConfirmed(true);
    }

    private void setPayment(){
        TextView payment = requireView().findViewById(R.id.payment);
        payment.setText("Стоимость питания: " + this.payment + "р");
    }

    private void checkConnection(){
        if (isConnection(requireContext())){
            setCheckViews();
            return;
        };
        requireView().findViewById(R.id.buttonNext).setVisibility(GONE);
        requireView().findViewById(R.id.buttonChange).setVisibility(GONE);
        blockCheckViews();

    }

    private void setTaskConfirmed(boolean isConfirmed){
        Log.i("main", "taskConfirmed " + isConfirmed);
        saveData(isConfirmed);

        if (getView() == null) return;

        float alpha;
        if (isConfirmed){
            alpha = 0.5f;
            requireView().findViewById(R.id.payment).setVisibility(GONE);
            getView().findViewById(R.id.buttonNext).setVisibility(GONE);
            if (isConnection(requireContext()))
                getView().findViewById(R.id.buttonChange).setVisibility(VISIBLE);
            ((MainActivity)requireActivity()).setTaskStateForView(true, true);
        } else{
            alpha = 1;
            requireView().findViewById(R.id.payment).setVisibility(VISIBLE);
            requireView().findViewById(R.id.buttonNext).setVisibility(VISIBLE);
            requireView().findViewById(R.id.buttonChange).setVisibility(GONE);
            ((MainActivity)requireActivity()).setTaskStateForView(false, true);
        }
        firstCheck.setAlpha(alpha);
        secondCheck.setAlpha(alpha);
        thirdCheck.setAlpha(alpha);
        fourthCheck.setAlpha(alpha);


    }

    private void setCheckViews(){
        firstCheck.setOnClickListener(view12 -> {
            ((MainActivity) requireActivity()).onTouch(null, null);
            setTaskAccepted();
            if (firstCheck.isChecked()) payment += Task.getPayment(0);
            else payment -= Task.getPayment(0);
            setPayment();
        });

        secondCheck.setOnClickListener(view13 -> {
            ((MainActivity) requireActivity()).onTouch(null, null);
            setTaskAccepted();
            if (secondCheck.isChecked()) payment += Task.getPayment(1);
            else payment -= Task.getPayment(1);
            setPayment();
        });

        thirdCheck.setOnClickListener(view14 -> {
            ((MainActivity) requireActivity()).onTouch(null, null);
            setTaskAccepted();
            if (thirdCheck.isChecked()) payment += Task.getPayment(2);
            else payment -= Task.getPayment(2);
            setPayment();
        });

        fourthCheck.setOnClickListener(view15 -> {
            ((MainActivity) requireActivity()).onTouch(null, null);
            setTaskAccepted();
            if (fourthCheck.isChecked()) payment += Task.getPayment(3);
            else payment -= Task.getPayment(3);
            setPayment();
        });

        firstCheck.setClickable(true);
        secondCheck.setClickable(true);
        thirdCheck.setClickable(true);
        fourthCheck.setClickable(true);
        setAccessible(getNowChild(childSelected).isActivated());
    }
    private void blockCheckViews(){
        firstCheck.setClickable(false);
        secondCheck.setClickable(false);
        thirdCheck.setClickable(false);
        fourthCheck.setClickable(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity)requireActivity()).setTaskStateForView(true, false);
    }
}

