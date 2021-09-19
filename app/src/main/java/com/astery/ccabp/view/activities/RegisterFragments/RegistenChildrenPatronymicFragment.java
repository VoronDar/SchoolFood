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
import com.astery.ccabp.view.activities.RegisterActivity;
import com.astery.ccabp.view.activities.utilities.RegisterFragment;

import static com.astery.ccabp.view.activities.RegisterFragments.RegisterAddChildrenFragment.nowChild;
import static com.astery.ccabp.view.activities.utilities.DisplayUtils.sizeX;
import static com.astery.ccabp.view.activities.utilities.FormMake.make;

public class RegistenChildrenPatronymicFragment extends RegisterFragment{

    private TextView nameField;


    public RegistenChildrenPatronymicFragment(){

    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_their_patronymic, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameField = view.findViewById(R.id.first_field);


        String name = nowChild.getpName();

        if (name != null)
            nameField.setText(name);

        if (sizeX(getActivity()) < 800)
            ((Button)view.findViewById(R.id.delete_child)).setText(getResources().getText(R.string.bt_delete_child_little));

        view.findViewById(R.id.delete_child).setOnClickListener(view1 -> {
            save = false;
            deleteChild(RegistenChildrenPatronymicFragment.this, getContext());
        });
        setOnMoveListener(nameField);

        setCancelChild(view);
    }

    private boolean save = true;

    public static void deleteChild(Fragment fragment, Context context){
        nowChild = null;
        ((RegisterActivity)(fragment.requireActivity())).goBack();
    }

    @Override
    public boolean isRight(){
        save();
        return (nameField.getText().length() > 1);
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
            nowChild.setpName(make(nameField.getText().toString()));

    }
}

