package com.astery.ccabp.view.activities.MainFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.model.things.Scedule;
import com.astery.ccabp.view.activities.MainActivity;
import com.astery.ccabp.view.activities.utilities.ConnectionSupport;
import com.astery.ccabp.view.activities.utilities.DialogListShower;
import com.astery.ccabp.view.activities.utilities.MainFragment;
import com.astery.ccabp.view.adapters.SceduleAdapter;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static android.view.View.GONE;
import static com.astery.ccabp.view.activities.MainActivity.childSelected;

public class HistoryFragment extends MainFragment {

    private TextView personView;

        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_history, container, false);


    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        personView = view.findViewById(R.id.name);

        if (!MainActivity.isOneChild){

            personView.setText(getNowChild(childSelected).getName()
                    +  " " + getNowChild(childSelected).getsName());


            personView.setOnClickListener(view12 ->
                    requireView().findViewById(R.id.place).performClick());
            view.findViewById(R.id.place).setOnClickListener(view1 -> {
                final ArrayList<SimpleUnit> list = new ArrayList<>();

                for (int i = 0; i < ((MainActivity) requireActivity()).children.size(); i++){
                    list.add(new SimpleUnit(((MainActivity)getActivity()).children.get(i).getName()
                            + " " + ((MainActivity)getActivity()).children.get(i).getsName()));
                }

                DialogListShower.show(getActivity(), list, position -> {
                    personView.setText(list.get(position).name);
                    childSelected = position;
                    setScedule();
                });
            });
        } else{
            requireView().findViewById(R.id.place).setVisibility(GONE);
        }


        setScedule();

    }


    private void setScedule(){

        CloudDatabase db = new CloudDatabase(getContext());
        db.getLastTask(getToday(), getNowChild(childSelected).getId() +"", new CloudDatabase.LastGettable(){
            @Override
            public void onFailure() {
                if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                        HistoryFragment.this))return;
                if (getContext() != null)
                    Toast.makeText(getContext(), "Не удалось получить данные", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ArrayList<String> things) {
                if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                        HistoryFragment.this))return;
                ((TextView)requireView().findViewById(R.id.date)).setText(getToday());
                requireView().findViewById(R.id.unit).setVisibility(GONE);
                ((TextView)requireView().findViewById(R.id.first_place)).setText("Завтрак: " + things.get(0));
                ((TextView)requireView().findViewById(R.id.second_place)).setText("Обед: " + things.get(1));
                ((TextView)requireView().findViewById(R.id.third_place)).setText("Полдник 15%: " + things.get(2));
                ((TextView)requireView().findViewById(R.id.fourth_place)).setText("Полдник 25%: " + things.get(3));
            }
        });

    }


    private Child getNowChild(int i){
        return ((MainActivity) requireActivity()).children.get(i);
    }

    private String getToday() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH)+1) + "." + (calendar.get(Calendar.DAY_OF_MONTH));
    }
}

