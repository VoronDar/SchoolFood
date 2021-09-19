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

public class RegisterContactsFragment extends RegisterFragment {

    private TextView emailField;
    private TextView phoneField;
    public RegisterContactsFragment(){
    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_contacts, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        emailField = view.findViewById(R.id.first_field);
        phoneField = view.findViewById(R.id.second_field);
        view.findViewById(R.id.second_place).setVisibility(View.INVISIBLE);


        String email = TransportPreferences.getEmail(getContext());
        String phone = TransportPreferences.getPhone(getContext());

        if (email != null)
            emailField.setText(email);
        if (phone != null)
            phoneField.setText(phone);

        setOnMoveListener(emailField);
            }



    @Override
    public boolean isRight(){
        return emailField.getText().toString().length() > 3;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (emailField.getText() != null && emailField.getText().length() > 0)
            TransportPreferences.setEmail(getContext(), emailField.getText().toString());
        if (phoneField.getText() != null && phoneField.getText().length() > 0)
            TransportPreferences.setPhone(getContext(), phoneField.getText().toString());
    }
}

