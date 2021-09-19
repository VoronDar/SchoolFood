package com.astery.ccabp.view.activities.MainFragments;

import android.annotation.SuppressLint;
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
import com.astery.ccabp.model.cloud_database.pogo.GotSchedule;
import com.astery.ccabp.model.cloud_database.pogo.PostMenu;
import com.astery.ccabp.model.local_database.MenuDatabase;
import com.astery.ccabp.model.local_database.ScheduleDatabase;
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.model.things.Menu;
import com.astery.ccabp.model.things.Scedule;
import com.astery.ccabp.view.activities.MainActivity;
import com.astery.ccabp.view.activities.utilities.ConnectionSupport;
import com.astery.ccabp.view.activities.utilities.DialogListShower;
import com.astery.ccabp.view.activities.utilities.MainFragment;
import com.astery.ccabp.view.adapters.SceduleAdapter;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.util.ArrayList;
import java.util.Objects;

import static android.view.View.GONE;
import static com.astery.ccabp.model.cloud_database.CloudUtils.hasConnection;
import static com.astery.ccabp.view.activities.MainActivity.childSelected;

public class SceduleFragment extends MainFragment {

    private TextView personView;
    private SceduleAdapter adapter;
    private RecyclerView recyclerView;

        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_scedult, container, false);


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
            if (!hasConnection(getContext()))
                setLocalSchedule(getNowChild(childSelected).getClassId());
            else {
                CloudDatabase db = new CloudDatabase(getContext());
                db.getScedule(getNowChild(childSelected).getClassId() + "", new CloudDatabase.SceduleGettable() {
                    @Override
                    public void onFailure() {
                        if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                                SceduleFragment.this)) return;
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Не удалось получить данные", Toast.LENGTH_SHORT).show();
                            setLocalSchedule(getNowChild(childSelected).getClassId());
                        }
                    }

                    @Override
                    public void onSuccess(ArrayList<Scedule> scedules) {
                        if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                                SceduleFragment.this)) return;
                        setScheduleToView(scedules);
                    }
                });
            }

    }

    private void setScheduleToView(ArrayList<Scedule> schedules){

        adapter = new SceduleAdapter(schedules);
        adapter.notifyDataSetChanged();

        recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setLocalSchedule(int classId){

        ScheduleDatabase db = ScheduleDatabase.getInstance(getContext());
        GotSchedule m = db.getSchedule(classId);
        if (m == null) {
            if (adapter != null)
                adapter.delete();
            Toast.makeText(getContext(), "Не удалось получить данные", Toast.LENGTH_SHORT).show();
            return;
        }
        setScheduleToView(GotSchedule.convertFromPost(m));
    }


    private Child getNowChild(int i){
        return ((MainActivity) requireActivity()).children.get(i);
    }
}

