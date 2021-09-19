package com.astery.ccabp.view.activities.MainFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.model.local_database.TaskDatabase;
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.astery.ccabp.view.activities.MainActivity;
import com.astery.ccabp.view.activities.utilities.DialogChangeShower;
import com.astery.ccabp.view.activities.utilities.InfoPannel;
import com.astery.ccabp.view.activities.utilities.MainFragment;
import com.astery.ccabp.view.adapters.ProfileAdapter;

import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.astery.ccabp.view.activities.MainActivity.childSelected;

public class ProfileFragment extends MainFragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_profile, container, false);


    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.school_everything).setOnTouchListener((View.OnTouchListener) requireActivity());
        //TransportPreferences.setInfoState(getContext(), false, InfoPannel.PLAN_FILLER_HELP);
        //TransportPreferences.setInfoState(getContext(), false, InfoPannel.TASK_FILL_HELP);
        final TextView name = view.findViewById(R.id.name_info);


        //TaskDatabase db = TaskDatabase.getInstance(requireContext());
        //db.open();
        //db.delete();
        //db.close();

        name.setText(TransportPreferences.getName(getContext()) + " " + TransportPreferences.getSecondName(getContext()));


        view.findViewById(R.id.more_parent).setOnClickListener(view1 -> DialogChangeShower.show(getActivity(), "имя и фамилию", name.getText().toString(), string -> {
            String[] in = string.split(" ");
            if (in.length == 2){
                TransportPreferences.setName(getContext(), in[0]);
                TransportPreferences.setSecondName(getContext(), in[1]);


                name.setText(TransportPreferences.getName(getContext()) + " "
                        + TransportPreferences.getSecondName(getContext()));
            } else{
                Toast.makeText(getContext(), "Неверный формат имени и фамилии", Toast.LENGTH_SHORT).show();
            }
        }));

        ProfileAdapter adapter = new ProfileAdapter(getContext(), ((MainActivity) requireActivity()).children, -1, (MainActivity) getActivity());
        adapter.notifyDataSetChanged();

        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnDragListener((v, event) -> {
            ((MainActivity)requireActivity()).onTouch(null, null);
            return false;
        });
        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                ((MainActivity)requireActivity()).onTouch(null, null);
                return false;
            }
        });

        adapter.notifyDataSetChanged();

        adapter.setBlockListener(position -> {
            ((MainActivity) requireActivity()).onTouch(null, null);
            MainActivity.childSelected = position;
            ((MainActivity)(requireActivity())).slideFragment(MainActivity.Steps.history, new HistoryFragment());
        }
        );
    }

}

