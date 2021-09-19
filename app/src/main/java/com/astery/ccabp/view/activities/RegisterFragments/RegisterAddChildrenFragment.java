package com.astery.ccabp.view.activities.RegisterFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.model.local_database.ChildDatabase;
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.view.activities.RegisterActivity;
import com.astery.ccabp.view.activities.utilities.RegisterFragment;
import com.astery.ccabp.view.adapters.AddChildAdapter;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.util.ArrayList;
import java.util.Random;

public class RegisterAddChildrenFragment extends RegisterFragment {

    private ArrayList<Child> children;
    public static Child nowChild;

    public RegisterAddChildrenFragment(){
    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_add_child, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<SimpleUnit> units = new ArrayList<>();

        final ChildDatabase db = ChildDatabase.getInstance(getContext());
        children = db.getEverything();

        for (int i = 0; i < children.size(); i++){
            units.add(new SimpleUnit(children.get(i).getName()
                    + " " + children.get(i).getsName()));
        }

        units.add(new SimpleUnit(null));

        AddChildAdapter adapter = new AddChildAdapter(getContext(), units);
        adapter.setBlockListener(new AddChildAdapter.BlockListener() {
            @Override
            public void onMoreClick(int position) {
                nowChild = children.get(position);
                ((RegisterActivity)(requireActivity())).slideFragment(RegisterActivity.Steps.childName);
            }

            @Override
            public void onAddClick(int position) {
                Random random = new Random(Runtime.getRuntime().freeMemory());
                int id = random.nextInt();
                RegisterAddChildrenFragment.nowChild = new Child(id + "");
                ((RegisterActivity)(requireActivity())).slideFragment(RegisterActivity.Steps.childName);

            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        view.findViewById(R.id.button_cancel).setOnClickListener(v ->
                ((RegisterActivity)requireActivity()).close());
            }

    @Override
    public boolean isRight(){
        return true;
    }

}

