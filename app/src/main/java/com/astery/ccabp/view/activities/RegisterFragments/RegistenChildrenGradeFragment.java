package com.astery.ccabp.view.activities.RegisterFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astery.ccabp.R;
import com.astery.ccabp.model.cloud_database.CloudDatabase;
import com.astery.ccabp.model.cloud_database.pogo.GotSchool;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.astery.ccabp.view.activities.RegisterActivity;
import com.astery.ccabp.view.activities.utilities.DialogListShower;
import com.astery.ccabp.view.activities.utilities.RegisterFragment;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.util.ArrayList;

import static com.astery.ccabp.view.activities.RegisterFragments.RegistenChildrenNameFragment.deleteChild;
import static com.astery.ccabp.view.activities.RegisterFragments.RegisterAddChildrenFragment.nowChild;
import static com.astery.ccabp.view.activities.utilities.DisplayUtils.sizeX;

public class RegistenChildrenGradeFragment extends RegisterFragment {

    private TextView schoolPlace;
    private TextView gradePlace;
    private TextView letterPlace;
    private CheckBox isFree;


    /** школы */
    public static ArrayList<GotSchool> schools;
    private int schoolPosition = -1;
    private boolean isLoaded = false;

    public RegistenChildrenGradeFragment(){
    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (schools == null || schools.size() == 0){
            CloudDatabase db = new CloudDatabase(getContext());
            db.getSchools(new CloudDatabase.SchoolsGettable() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onError() {

                }

                @Override
                public void onSuccess(ArrayList<GotSchool> schools) {
                    RegistenChildrenGradeFragment.schools = schools;
                    isLoaded = true;
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_grade, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setCancelChild(view);


        TextView description = view.findViewById(R.id.description);
        description.setText(description.getText() + " "
                + nowChild.getName() + " "
        + nowChild.getsName());


        schoolPlace = view.findViewById(R.id.first_field);
        gradePlace = view.findViewById(R.id.second_field);
        letterPlace = view.findViewById(R.id.third_field);
        isFree = view.findViewById(R.id.freeCheck);

        String school = nowChild.getSchoolName();
        String grade = null;
        String letter = null;
        if (nowChild.getGrade() != null && nowChild.getGrade().length() >=2) {
            grade = nowChild.getGrade().substring(0, nowChild.getGrade().length() - 1);
            letter = nowChild.getGrade().substring(nowChild.getGrade().length() - 1);
        }

        if (school != null)
            schoolPlace.setText(school);
        if (grade != null)
            gradePlace.setText(grade);
        if (letter != null)
            letterPlace.setText(letter);
        isFree.setChecked(nowChild.isFree());



        view.findViewById(R.id.delete_child).setOnClickListener(view1 -> {
            if (isBlock) return;
            save = false;
            ((RegisterActivity)requireActivity()).nowStep = RegisterActivity.Steps.childName;
            deleteChild(RegistenChildrenGradeFragment.this, getContext());
        });

        schoolPlace.setOnClickListener(view12 -> {
            if (isBlock || schools == null || schools.size() == 0) return;
            final ArrayList<SimpleUnit> list = new ArrayList<>();
            for (GotSchool sc: schools) {
                list.add(new SimpleUnit(sc.getName()));
            }
            DialogListShower.show(getActivity(), list, position -> {
                schoolPosition = position;
                ((TextView) view12).setText(list.get(position).name);
            });
        });


        gradePlace.setOnClickListener(view13 -> {
            if (isBlock) return;
            final ArrayList<SimpleUnit> list = new ArrayList<>();
            list.add(new SimpleUnit("1"));
            list.add(new SimpleUnit("2"));
            list.add(new SimpleUnit("3"));
            list.add(new SimpleUnit("4"));
            list.add(new SimpleUnit("5"));
            list.add(new SimpleUnit("6"));
            list.add(new SimpleUnit("7"));
            list.add(new SimpleUnit("8"));
            list.add(new SimpleUnit("9"));
            list.add(new SimpleUnit("10"));
            list.add(new SimpleUnit("11"));
            list.add(new SimpleUnit("12"));
            DialogListShower.show(getActivity(), list, position -> ((TextView) view13).setText(list.get(position).name));
        });


        letterPlace.setOnClickListener(view14 -> {
            final ArrayList<SimpleUnit> list = new ArrayList<>();
            list.add(new SimpleUnit("А"));
            list.add(new SimpleUnit("Б"));
            list.add(new SimpleUnit("В"));
            list.add(new SimpleUnit("Г"));
            list.add(new SimpleUnit("Д"));
            list.add(new SimpleUnit("Е"));
            list.add(new SimpleUnit("Ж"));
            list.add(new SimpleUnit("З"));
            list.add(new SimpleUnit("И"));
            list.add(new SimpleUnit("К"));
            DialogListShower.show(getActivity(), list, position -> ((TextView) view14).setText(list.get(position).name));
        });
            }

    private boolean save = true;

    private boolean loaded = false;
    private boolean isBlock = false;

    @Override
    public boolean isRight(){
        save();
        return (schoolPlace.getText().toString().length() > 3 && gradePlace.getText().toString().length() >=1
        &&  letterPlace.getText().length() > 0);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

        if (save) {
            save();
        }
    }

    private void save(){
        String grade = "";

        if (schoolPlace.getText() != null && schoolPlace.getText().length() > 0) {
            nowChild.setSchoolName(schoolPlace.getText().toString());
            if (schoolPosition != -1) {
                if (schools.size() > schoolPosition) {
                    nowChild.setSchool(schools.get(schoolPosition).getId());
                    nowChild.setHomeId(schools.get(schoolPosition).getHomeId());
                }
            }
        }
        if (gradePlace.getText() != null && gradePlace.getText().length() > 0)
            grade += gradePlace.getText().toString();
        if (letterPlace.getText() != null && letterPlace.getText().length() > 0)
            grade += letterPlace.getText().toString();

        nowChild.setGrade(grade);
        nowChild.setFree(isFree.isChecked());
    }

}

