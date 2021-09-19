package com.astery.ccabp.view.activities.RegisterFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astery.ccabp.R;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.astery.ccabp.view.activities.RegisterActivity;
import com.astery.ccabp.view.activities.utilities.RegisterFragment;

import static com.astery.ccabp.view.activities.utilities.FormMake.make;

public class RegisterNameFragment extends RegisterFragment {

    private TextView nameField;
    private TextView sNameField;

    public RegisterNameFragment(){
    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_register, container, false);


    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameField = view.findViewById(R.id.first_field);
        sNameField = view.findViewById(R.id.second_field);


        String name = TransportPreferences.getName(getContext());
        String sName = TransportPreferences.getSecondName(getContext());

        if (name != null)
            nameField.setText(name);
        if (sName != null)
            sNameField.setText(sName);

        setOnMoveListener(sNameField);

    }

    @Override
    public boolean isRight(){
        return (nameField.getText().length() > 1 && sNameField.getText().length() > 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (nameField.getText() != null && nameField.getText().length() > 0) {
            TransportPreferences.setName(getContext(),make(nameField.getText().toString()));
        }
        if (sNameField.getText() != null && sNameField.getText().length() > 0)
            TransportPreferences.setSecondName(getContext(), make(sNameField.getText().toString()));
    }
}

