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
import com.astery.ccabp.view.activities.RegisterActivity;
import com.astery.ccabp.view.activities.utilities.RegisterFragment;

import java.util.Objects;

import static com.astery.ccabp.view.activities.utilities.DisplayUtils.sizeY;

public class RegisterEnterFragment extends RegisterFragment {

    private TextView emailField;
    private TextView passwordField;

    public RegisterEnterFragment(){
    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (sizeY(getActivity()) < 1300)
            return inflater.inflate(R.layout.fragment_enter_little, container, false);
        return inflater.inflate(R.layout.fragment_enter, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ///// SET ANOTHER BUTTON ///////////////////////////////////////////////////////////////////
        view.findViewById(R.id.button_register).setOnClickListener(view1 ->{
                delete = true;
                RegisterActivity.deleteData(getContext());
                ((RegisterActivity)(Objects.requireNonNull(requireActivity()))).slideFragment(RegisterActivity.Steps.name);});
        ////////////////////////////////////////////////////////////////////////////////////////////


        emailField = view.findViewById(R.id.first_field);
        passwordField = view.findViewById(R.id.second_field);

        String email = TransportPreferences.getEmail(getContext());
        String password = TransportPreferences.getPassword(getContext());

        if (emailField != null)
            emailField.setText(email);
        if (password != null)
            passwordField.setText(password);
    }

    private boolean delete = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }

    public void saveData() {
        if (emailField == null) return;
        if (delete) return;
        if (emailField.getText() != null && emailField.getText().length() > 0)
            TransportPreferences.setEmail(getContext(), emailField.getText().toString());
        if (passwordField.getText() != null && passwordField.getText().length() > 0)
            TransportPreferences.setPassword(getContext(), passwordField.getText().toString());
    }

    @Override
    public boolean isRight(){
        saveData();
        if (emailField.getText() == null || emailField.getText().length() < 3) return false;
        return passwordField.getText() != null && passwordField.getText().length() >= 2;
    }

}

