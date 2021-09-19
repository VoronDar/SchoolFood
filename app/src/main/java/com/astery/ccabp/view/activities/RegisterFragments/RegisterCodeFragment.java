package com.astery.ccabp.view.activities.RegisterFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astery.ccabp.R;
import com.astery.ccabp.model.cloud_database.CloudDatabase;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.astery.ccabp.view.activities.RegisterActivity;
import com.astery.ccabp.view.activities.utilities.RegisterFragment;

import static com.astery.ccabp.view.activities.RegisterFragments.RegistenChildrenNameFragment.deleteChild;
import static com.astery.ccabp.view.activities.RegisterFragments.RegisterAddChildrenFragment.nowChild;

public class RegisterCodeFragment extends RegisterFragment {

    private TextView codeField;
    private TextView sNameField;

    public RegisterCodeFragment(){
    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_code, container, false);


    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        codeField = view.findViewById(R.id.first_field);

        if (nowChild.getClassCode() != null)
            codeField.setText(nowChild.getClassCode());

        view.findViewById(R.id.delete_child).setOnClickListener(view1 -> {
            if (isBlock) return;
            save = false;
            ((RegisterActivity)requireActivity()).nowStep = RegisterActivity.Steps.childName;
            deleteChild(RegisterCodeFragment.this, getContext());
        });

        setOnMoveListener(codeField);

        setCancelChild(view);
    }


    private boolean save = true;
    private boolean loaded = false;
    private boolean isBlock = false;

    @Override
    public boolean isRight(){
        if (loaded) return true;
        CloudDatabase database = new CloudDatabase(getContext(), null);

        if (isBlock) return false;
        save();
        database.addChild(nowChild, TransportPreferences.getParentId(getContext()), new CloudDatabase.ALoadable() {
            @Override
            public void onFailure(String message) {
                RegisterFragment.startWithError = true;
                switch (message) {
                    case "e":
                        Toast.makeText(getContext(), "Ребенок уже зарегистрирован", Toast.LENGTH_SHORT).show();
                    case "f":
                    case "n":
                        ((RegisterActivity) (requireActivity())).slideFragment(RegisterActivity.Steps.childName);
                        break;
                    case "p":
                        ((RegisterActivity) (requireActivity())).slideFragment(RegisterActivity.Steps.childPatronymic);
                        break;
                    case "ic":
                        ((RegisterActivity) (requireActivity())).slideFragment(RegisterActivity.Steps.childCode);
                        break;
                }

            }

            @Override
            public void onSuccess() {

                Toast.makeText(getContext(), "Ребенок добавлен!", Toast.LENGTH_SHORT).show();
                loaded = true;
                ((RegisterActivity)(requireActivity())).goNext();
            }

            @Override
            public void onError() {
                isBlock = true;
            }
        });
        return false;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (save) {
            save();
        }
    }

    private void save(){

        if (codeField.getText() != null && codeField.getText().length() > 5) {
            String str = codeField.getText().toString();
            nowChild.setClassCode(str.toUpperCase());
        }
    }

}

