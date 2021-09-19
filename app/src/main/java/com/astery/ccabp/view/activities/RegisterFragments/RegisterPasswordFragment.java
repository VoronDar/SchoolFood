package com.astery.ccabp.view.activities.RegisterFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astery.ccabp.R;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.astery.ccabp.view.activities.utilities.RegisterFragment;

public class RegisterPasswordFragment extends RegisterFragment {

    private TextView passwordField;
    private TextView confirmPasswordField;

    public boolean isBlock = false;

    public RegisterPasswordFragment(){
    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_password, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        passwordField = view.findViewById(R.id.first_field);
        confirmPasswordField = view.findViewById(R.id.second_field);


        String password = TransportPreferences.getPassword(getContext());
        String ppPasword = TransportPreferences.getConfirmPassword(getContext());

        if (password != null)
            passwordField.setText(password);
        if (ppPasword != null)
            confirmPasswordField.setText(ppPasword);

        setOnMoveListener(confirmPasswordField);
    }

    @Override
    public boolean isRight(){
        save();
        return (passwordField.getText().toString().equals(confirmPasswordField.getText().toString()));
    }

    private void save(){
        if (passwordField.getText() != null && passwordField.getText().length() > 0)
            TransportPreferences.setPassword(getContext(), passwordField.getText().toString());
        if (confirmPasswordField.getText() != null && confirmPasswordField.getText().length() > 0)
            TransportPreferences.setConfirmPassword(getContext(), confirmPasswordField.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        save();
    }
}

