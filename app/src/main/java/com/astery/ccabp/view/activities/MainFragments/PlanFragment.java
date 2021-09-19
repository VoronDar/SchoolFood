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
import com.astery.ccabp.model.local_database.PlanDatabase;
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
import com.astery.ccabp.view.adapters.TaskAdapter;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.astery.ccabp.model.cloud_database.CloudUtils.isConnection;
import static com.astery.ccabp.view.activities.MainActivity.childSelected;

public class PlanFragment extends MainFragment {
    private TaskAdapter adapter;
    private TextView child;
    private static final int openPosition = -1;
    private ArrayList<Task> units;

        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_week_plan, container, false);


    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.help).setOnClickListener(v -> {
            ((MainActivity)requireActivity()).onTouch(null, null);
            InfoPannel.openPanel((InfoHoldable) getActivity(), "Вы можете заполнить план питания для Вашего ребенка. План будет повторяться каждую неделю. Вы можете изменять план питания в любое время. Чтобы изменить питание по плану на конкретный день, перейдите на вкладку 'Заявка'",
                    "Ок", null, false);
        });


        view.findViewById(R.id.school_everything).setOnTouchListener((View.OnTouchListener) requireActivity());


        if (!TransportPreferences.getInfoState(getContext(), InfoPannel.PLAN_FILLER_HELP)) {
            InfoPannel.openPanel((InfoHoldable) requireActivity(),
                    "Вы можете заполнить план питания для Вашего ребенка. План будет повторяться каждую неделю. Вы можете изменять план питания в любое время. Чтобы изменить питание по плану на конкретный день, перейдите на вкладку 'Заявка'",
                    "Ок", () -> TransportPreferences.setInfoState(getContext(), true, InfoPannel.PLAN_FILLER_HELP), true);
        }

        child = view.findViewById(R.id.name);
        if (!MainActivity.isOneChild) {

            child.setText(getNowChild(childSelected).getName()
                    + " " + getNowChild(childSelected).getsName());

            child.setOnClickListener(view13 -> requireView().findViewById(R.id.child).callOnClick());
            view.findViewById(R.id.child).setOnClickListener(view12 -> {
                ((MainActivity) requireActivity()).onTouch(null, null);
                saveTemper();
                final ArrayList<SimpleUnit> list = new ArrayList<>();

                for (int i = 0; i < ((MainActivity) requireActivity()).children.size(); i++){
                    list.add(new SimpleUnit(((MainActivity) requireActivity()).children.get(i).getName()
                            + " " + ((MainActivity)getActivity()).children.get(i).getsName()));
                }

                DialogListShower.show(getActivity(), list, position -> {
                    child.setText(list.get(position).name);
                    childSelected = position;
                    setChecked();
                });
            });

        } else{
            view.findViewById(R.id.child).setVisibility(GONE);
        }

        setChecked();

        view.findViewById(R.id.buttonNext).setOnClickListener(view1 -> {
            ((MainActivity) requireActivity()).onTouch(null, null);

            view1.setClickable(false);

            CloudDatabase db = new CloudDatabase(getContext(), new CloudDatabase.Loadable() {
                @Override
                public void onFailure() {
                    if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep)) return;
                    Toast.makeText(getContext(), "не удалось отправить данные", Toast.LENGTH_SHORT).show();
                    view1.setClickable(true);
                }

                @Override
                public void onSuccess() {
                    if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep)) return;
                    Toast.makeText(getContext(), "данные отправлены", Toast.LENGTH_SHORT).show();
                    view1.setClickable(true);

                    resetPlan();
                }

                @Override
                public void onError() {
                    if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep)) return;
                    Toast.makeText(getContext(), "не удалось отправить данные", Toast.LENGTH_SHORT).show();
                    view1.setClickable(true);
                }
            });
            db.loadPlan(units, getNowChild(childSelected).getClassId());
        });



    }


    private Child getNowChild(int i){
        return ((MainActivity) requireActivity()).children.get(i);
    }

    private void setChecked(){

            if (!MainActivity.isPlanAccepted.get(childSelected)){
                requireView().findViewById(R.id.buttonNext).setVisibility(GONE);
            } else if (isConnection(requireContext()))
                requireView().findViewById(R.id.buttonNext).setVisibility(View.VISIBLE);

            if (isConnection(requireContext())) {
                CloudDatabase db = new CloudDatabase(getContext());
                db.getPlan(new CloudDatabase.PlanGettable() {
                    @Override
                    public void onFailure() {
                        if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                                PlanFragment.this)) return;
                        ArrayList<Task> tasks = new ArrayList<>();
                        for (int i = 0; i < 6; i++) {
                            tasks.add(new Task(getNowChild(childSelected).getId(), "" + i,
                                    false,
                                    false,
                                    false,
                                    false, 0
                            ));
                        }
                        onSuccess(tasks);
                    }

                    @Override
                    public void onError() {
                        if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                                PlanFragment.this)) return;

                    }

                    @Override
                    public void onSuccess(ArrayList<Task> plan) {
                        if (!ConnectionSupport.isNowFragmentAvailable(MainActivity.nowStep,
                                PlanFragment.this)) return;
                        units = plan;
                        adapter = new TaskAdapter(plan, openPosition, PlanFragment.this, (MainActivity) requireActivity());
                        adapter.notifyDataSetChanged();
                        setAccessible(getNowChild(childSelected).isActivated());
                        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
                        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                        adapter.setBlockListener(new TaskAdapter.BlockListener() {
                            @Override
                            public void onGoMenu(int position) {
                                if (adapter.isNotAvailable()) return;
                                ((MainActivity) (requireActivity())).slideFragment(MainActivity.Steps.menu, new MenuFragment(position, true));
                            }

                            @Override
                            public void onGoScedule() {
                                if (adapter.isNotAvailable()) return;
                                ((MainActivity) (requireActivity())).slideFragment(MainActivity.Steps.scedule,
                                        new SceduleFragment());
                            }
                        });
                    }
                }, getNowChild(childSelected).getId());
            } else{
                PlanDatabase db = PlanDatabase.getInstance(requireContext());
                db.open();
                ArrayList<Task> units = db.getEverything(Integer.parseInt(getNowChild(childSelected).getId()));
                adapter = new TaskAdapter(units, openPosition, PlanFragment.this, (MainActivity) requireActivity());
                adapter.notifyDataSetChanged();
                setAccessible(getNowChild(childSelected).isActivated());
                RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
                recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
    }

    private void saveTemper(){
        if (units == null) return;
            int day = 0;
        for (Task task : units)
                TPStorage.saveTemperPlan(getContext(), task, day++);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveTemper();
    }

    public void setPlanAccepted() {
        MainActivity.isPlanAccepted.set(childSelected, true);
        if (isConnection(requireContext()))
            requireView().findViewById(R.id.buttonNext).setVisibility(View.VISIBLE);
    }

    public void resetPlan() {
        MainActivity.isPlanAccepted.set(childSelected, false);
        requireView().findViewById(R.id.buttonNext).setVisibility(View.GONE);
    }

    /** делает кнопки недоступными для неактивированных пользователей*/
    private void setAccessible(boolean isActivated){
        if (!isActivated){
            requireView().findViewById(R.id.blocker).setVisibility(VISIBLE);
            requireView().findViewById(R.id.blockerText).setVisibility(VISIBLE);
        }else{
            requireView().findViewById(R.id.blocker).setVisibility(GONE);
            requireView().findViewById(R.id.blockerText).setVisibility(GONE);
        }
        if (isConnection(requireContext()))
            requireView().findViewById(R.id.buttonNext).setEnabled(isActivated);
        adapter.setAvailable(isActivated);
    }
}

