package com.astery.ccabp.view.activities.RegisterFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.astery.ccabp.R;
import com.astery.ccabp.model.local_database.ChildDatabase;
import com.astery.ccabp.view.activities.RegisterActivity;
import com.astery.ccabp.view.activities.utilities.RegisterFragment;

import static com.astery.ccabp.view.activities.RegisterFragments.RegisterAddChildrenFragment.nowChild;
import static com.astery.ccabp.view.activities.utilities.DisplayUtils.sizeX;
import static com.astery.ccabp.view.activities.utilities.FormMake.make;

public class RegistenChildrenNameFragment extends RegisterFragment{

    private TextView nameField;
    private TextView sNameField;


    public RegistenChildrenNameFragment(){

    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_their_name, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameField = view.findViewById(R.id.first_field);
        sNameField = view.findViewById(R.id.second_field);


        String name = nowChild.getName();
        String sName = nowChild.getsName();

        if (name != null)
            nameField.setText(name);
        if (sName != null)
            sNameField.setText(sName);

        setCancelChild(view);

        view.findViewById(R.id.delete_child).setOnClickListener(view1 -> {
            save = false;
            deleteChild(RegistenChildrenNameFragment.this, getContext());
        });
        setOnMoveListener(sNameField);

    }

    private boolean save = true;

    public static void deleteChild(Fragment fragment, Context context){
        nowChild = null;
        ((RegisterActivity)(fragment.requireActivity())).goBack();
    }

    @Override
    public boolean isRight(){
        save();
        return (nameField.getText().length() > 1 && sNameField.getText().length() > 1);
    }

    @Override
    public void onDestroy() {
        if (save) {
            save();
        }
        super.onDestroy();
    }

    private void save(){
        if (nameField.getText() != null && nameField.getText().length() > 0)
            nowChild.setName(make(nameField.getText().toString()));
        if (sNameField.getText() != null && sNameField.getText().length() > 0)
            nowChild.setsName(make(sNameField.getText().toString()));

    }
}

