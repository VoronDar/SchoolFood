package com.astery.ccabp.view.activities.utilities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.astery.ccabp.R;
import com.astery.ccabp.view.activities.RegisterActivity;

import static com.astery.ccabp.view.activities.utilities.DisplayUtils.sizeX;

/**
 * базовый класс для фрагментов регистраци
 */
public abstract class RegisterFragment extends Fragment {
        public abstract boolean isRight();

        public static boolean startWithError = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((RegisterActivity)(requireActivity())).setFragmentNext(this);
        if (startWithError){
            showError();
            startWithError = false;
        }
    }

    public void showError(){
        if (getView() == null) return;
        View warn = getView().findViewById(R.id.warning);
        if (warn != null) warn.setVisibility(View.VISIBLE);
    }

    protected void setOnMoveListener(View view){
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction()!=KeyEvent.ACTION_DOWN)
                return false;
            if(keyCode == KeyEvent.KEYCODE_ENTER ){
                ((RegisterActivity)requireActivity()).closeFragment(this);
                return true;
            }
            return false;
        });
    }

    protected void setCancelChild(View view){
        if (sizeX(getActivity()) < 800)
            ((Button)view.findViewById(R.id.delete_child)).setText(getResources().getText(R.string.bt_delete_child_little));
    }

}