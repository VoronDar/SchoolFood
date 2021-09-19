package com.astery.ccabp.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.astery.ccabp.R;
import com.astery.ccabp.model.cloud_database.CloudAuth;
import com.astery.ccabp.model.cloud_database.CloudDatabase;
import com.astery.ccabp.model.cloud_database.pogo.GotSchool;
import com.astery.ccabp.model.local_database.ChildDatabase;
import com.astery.ccabp.model.local_database.MenuDatabase;
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.astery.ccabp.view.activities.RegisterFragments.RegistenChildrenGradeFragment;
import com.astery.ccabp.view.activities.RegisterFragments.RegistenChildrenNameFragment;
import com.astery.ccabp.view.activities.RegisterFragments.RegistenChildrenPatronymicFragment;
import com.astery.ccabp.view.activities.RegisterFragments.RegisterAddChildrenFragment;
import com.astery.ccabp.view.activities.RegisterFragments.RegisterCodeFragment;
import com.astery.ccabp.view.activities.RegisterFragments.RegisterContactsFragment;
import com.astery.ccabp.view.activities.RegisterFragments.RegisterEnterFragment;
import com.astery.ccabp.view.activities.RegisterFragments.RegisterNameFragment;
import com.astery.ccabp.view.activities.RegisterFragments.RegisterPasswordFragment;
import com.astery.ccabp.view.activities.RegisterFragments.RegisterRegionFragment;
import com.astery.ccabp.view.activities.utilities.AndroidBug5497Workaround;
import com.astery.ccabp.view.activities.utilities.RegisterFragment;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Random;

import static com.astery.ccabp.model.cloud_database.CloudUtils.hasConnection;
import static com.astery.ccabp.model.cloud_database.CloudUtils.isConnection;

/**
 * активность, которая регистрирует пользователя или позволяет ему войти
 *
 */
public class RegisterActivity extends AppCompatActivity{


    private FragmentManager fragmentManager;

    /** текущий фрагмент */
    public Steps nowStep;
    /** текущий фрагмент */
    public RegisterFragment fragment;



    /** Фрагменты и взаимодействие между ними
     *  иногда фрагменты все-таки знают, как взаимодействовать между собой,
     *  но все, что касается кнопки далее и назад обрабатывается
     *  ! сами фрагменты обрабатывают переход на "удалить ребенка", "добавить ребенка" и "зарегистрироваться"
     * */
    public enum Steps{
        enter{
            @Override
            RegisterFragment getFragment() {
                return new RegisterEnterFragment();
            }

            @Override
            Steps goNext() {
                return null;
            }

            @Override
            Steps goBack() {
                return null;
            }

        },
        name{
            @Override
            RegisterFragment getFragment() {
                return new RegisterNameFragment();
            }

            @Override
            Steps goNext() {
                // пока сразу на данные
                //return Steps.region;
                return Steps.contacts;
            }

            @Override
            Steps goBack() {
                return Steps.enter;
            }
        },
        region{
            @Override
            RegisterFragment getFragment() {
                return new RegisterRegionFragment();
            }

            @Override
            Steps goNext() {
                return contacts;
            }

            @Override
            Steps goBack() {
                return name;
            }
        },
        contacts{
            @Override
            RegisterFragment getFragment() {
                return new RegisterContactsFragment();
            }

            @Override
            Steps goNext() {
                return password;
            }

            @Override
            Steps goBack() {

                // пока сразу на данные
                return Steps.name;
                //return region;
            }
        },
        password{
            @Override
            RegisterFragment getFragment() {
                return new RegisterPasswordFragment();
            }

            @Override
            Steps goNext() {
                return childrenList;
            }

            @Override
            Steps goBack() {
                return contacts;
            }
        },
        childrenList{
            @Override
            RegisterFragment getFragment() {
                return new RegisterAddChildrenFragment();
            }

            @Override
            Steps goNext() {
                return null;
            }

            @Override
            Steps goBack() {
                return password;
            }
        },
        childName{
            @Override
            RegisterFragment getFragment() {
                return new RegistenChildrenNameFragment();
            }

            @Override
            Steps goNext() {
                return childPatronymic;
            }

            @Override
            Steps goBack() {
                if (MainActivity.isStarted)
                    return null;
                return Steps.childrenList;
            }
        },
        childPatronymic{
            @Override
            RegisterFragment getFragment() {
                return new RegistenChildrenPatronymicFragment();
            }

            @Override
            Steps goNext() {
                return childClass;
            }

            @Override
            Steps goBack() {
                return childName;
            }
        }
        ,
        childClass{
            @Override
            RegisterFragment getFragment() {
                return new RegistenChildrenGradeFragment();
            }

            @Override
            Steps goNext() {
                return childCode;
            }

            @Override
            Steps goBack() {
                return childPatronymic;
            }
            }
        ,
            childCode{
                @Override
                RegisterFragment getFragment() {
                    return new RegisterCodeFragment();
                }

                @Override
                Steps goNext() {
                    if (MainActivity.isStarted)
                        return null;
                    return childrenList;
                }

                @Override
                Steps goBack() {
                    return childClass;
                }
            };

        abstract RegisterFragment getFragment();
        abstract Steps goNext();
        abstract Steps goBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!MainActivity.isStarted &&
                TransportPreferences.isSigned(getApplicationContext())){
            if (isConnection(getApplicationContext()))
                checkChildrenActivation();
            else
                close();
        }

        setContentView(R.layout.activity_main);


        MaterialToolbar topAppBar = super.findViewById(R.id.topAppBar);
        if (topAppBar != null) {
            topAppBar.setNavigationOnClickListener(view -> goBack());
            topAppBar.findViewById(R.id.settings).setVisibility(View.GONE);
            topAppBar.findViewById(R.id.info).setVisibility(View.GONE);
        }

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();
        if (!MainActivity.isStarted) {
            nowStep = Steps.enter;
        } else{
            Random random = new Random(Runtime.getRuntime().freeMemory());
            int id = random.nextInt();
            RegisterAddChildrenFragment.nowChild = new Child(id + "");
            nowStep = Steps.childName;
        }
        fragment = nowStep.getFragment();
        ft.add(R.id.fragment_place, fragment);
        ft.commit();

    }

    /** объявляет нажатие кнопки "далее" для фрагментов - вызывается в базовом классе фрагментов*/
    public void setFragmentNext(final RegisterFragment fr){
        fr.requireView().findViewById(R.id.buttonNext).setOnClickListener(view -> {
            closeFragment(fr);
        });
    }

    public void closeFragment(final RegisterFragment fr){
        if (fr.isRight()) goNext();
        else fr.showError();
    }


    /** удалить данные об аккаунте*/
    public static void deleteData(Context context){
        if (TransportPreferences.isSigned(context)) {
            ChildDatabase db = ChildDatabase.getInstance(context);
            ArrayList<Child> children = db.getEverything();
            for (Child child: children){
                db.delete(child.getId());
            }
            TransportPreferences.setName(context, "");
            TransportPreferences.setEmail(context, "");
            TransportPreferences.setParentId(context, 0);
            TransportPreferences.setSecondName(context, "");
            TransportPreferences.setPassword(context, "");
            TransportPreferences.setConfirmPassword(context, "");
            TransportPreferences.setPhone(context, "");

            TransportPreferences.setSigned(context, false);
        }
    }

    /** отправиться дальше */
    public void goNext(){
     if (fragment.isRight()){
         if (nowStep.goNext() != null){
             if (nowStep.equals(Steps.enter)){ deleteData(getApplicationContext());
                 slideFragment(nowStep.goNext());}
             if (nowStep.equals(Steps.password)) {
                 if (((RegisterPasswordFragment)fragment).isBlock) return;
                 ((RegisterPasswordFragment)fragment).isBlock = true;
                 CloudAuth cd = new CloudAuth(getApplicationContext(), new CloudAuth.Signable() {
                     @Override
                     public void onFailure() {
                         Toast.makeText(RegisterActivity.this, "не удалось зарегистрироваться", Toast.LENGTH_SHORT).show();
                         ((RegisterPasswordFragment)fragment).isBlock = false;
                     }

                     @Override
                     public void onSuccess() {
                         slideFragment(nowStep.goNext());
                     }

                     @Override
                     public void onError() {
                         ((RegisterPasswordFragment)fragment).isBlock = false;
                     }
                 });
                 if (hasConnection(getApplicationContext()))
                    cd.signUp(TransportPreferences.getEmail(getApplicationContext()),
                         TransportPreferences.getPassword(getApplicationContext()),
                         TransportPreferences.getName(getApplicationContext()),
                         TransportPreferences.getSecondName(getApplicationContext()));
                 else
                     ((RegisterPasswordFragment)fragment).isBlock = false;
             }
             else{
                 slideFragment(nowStep.goNext());
             }
         } else{
             if (nowStep.equals(Steps.childrenList) || nowStep.equals(Steps.childCode)){
                 close();
                 TransportPreferences.setSigned(getApplicationContext(), true);
             }
             else{
                 CloudAuth cd = new CloudAuth(getApplicationContext(), new CloudAuth.Signable() {
                     @Override
                     public void onFailure() {
                         Toast.makeText(getApplicationContext(), "Почта или пароль не совпадают", Toast.LENGTH_SHORT).show();
                     }

                     @Override
                     public void onSuccess() {
                         Log.i("main", "success");
                         getChildren();
                     }

                     @Override
                     public void onError() {
                     }
                 });

                 if (hasConnection(getApplicationContext())) {
                     cd.signIn((TransportPreferences.getEmail(getApplicationContext())),
                             TransportPreferences.getPassword(getApplicationContext()));
                 }

             }
         }
     } else{
         fragment.showError();
     }
    }

    private void getChildren(){
        CloudDatabase db = new CloudDatabase(getApplicationContext(), new CloudDatabase.Loadable() {
            @Override
            public void onFailure() {
                Log.i("flask", "fail");
                close();
                TransportPreferences.setSigned(getApplicationContext(), true);

            }

            @Override
            public void onSuccess() {
                Log.i("main", "setChildren");
                checkChildrenActivation();
                TransportPreferences.setSigned(getApplicationContext(), true);
            }

            @Override
            public void onError() {
                Log.i("flask", "error");
                ((RegisterPasswordFragment)fragment).isBlock = false;
            }
        });
        if (hasConnection(getApplicationContext())) {
            Log.i("flask", "setset");
            db.downloadChildren(TransportPreferences.getParentId(getApplicationContext()) + "");
        }
    }

    private void checkChildrenActivation(){
        ArrayList<Child> children = ChildDatabase.getInstance(getApplicationContext()).getEverything();
        CloudDatabase db = new CloudDatabase(getApplicationContext(), new CloudDatabase.Loadable() {
            @Override
            public void onFailure() {
                close();
            }

            @Override
            public void onSuccess() {
                close();
            }

            @Override
            public void onError() {
                //close();
            }
        });

        if (hasConnection(getApplicationContext()))
            db.getAvailables(children);

    }


    /** закрыть регистрацию и перейти на другую активность */
    public void close(){
        CloudDatabase db= new CloudDatabase(getApplicationContext(), new CloudDatabase.Loadable() {
            @Override
            public void onFailure() {
                ((RegisterPasswordFragment)fragment).isBlock = false;
            }

            @Override
            public void onSuccess() {

                if (RegistenChildrenGradeFragment.schools != null)
                    RegistenChildrenGradeFragment.schools.clear();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError() {
                ((RegisterPasswordFragment)fragment).isBlock = false;
            }
        });

        if (!hasConnection(getApplicationContext())){
            db.loadable.onSuccess();
            return;
        }

        ChildDatabase cdb = ChildDatabase.getInstance(getApplicationContext());
        ArrayList<Child> children = cdb.getEverything();

        if (children == null || children.size() == 0 || RegistenChildrenGradeFragment.schools == null) {
            db.loadable.onSuccess();
            return;
        }


        if (children == null || children.get(0) == null){
            db.loadable.onSuccess();
        }

        int homeId = findSchoolById(RegistenChildrenGradeFragment.schools, children.get(0).getSchool()).getHomeId();

        for (Child child: children){
            if (homeId != findSchoolById(RegistenChildrenGradeFragment.schools, child.getSchool()).getHomeId()) {
                homeId = -1;
                break;
            }
        }

        if (homeId == -1)
            db.loadable.onSuccess();
        else {
            db.getPayment(homeId);
            TransportPreferences.setlastPaymentHomeId(getApplicationContext(), homeId);
        }
    }

    private GotSchool findSchoolById(ArrayList<GotSchool> schools, int id){
        for (GotSchool sc: schools){
            if (sc.getId() == id) return sc;
        }
        return null;
    }

    /** отправиться назад */
    public void goBack(){
            if (nowStep.goBack() != null){
                slideFragment(nowStep.goBack());
            } else{
                if (nowStep.equals(Steps.childName))
                    close();
            }
    }

    /** переход между фрагментами */
    public void slideFragment(@NonNull Steps step){
        this.fragment = step.getFragment();
        this.nowStep = step;

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        ft.replace(R.id.fragment_place, this.fragment);
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        goBack();
    }


}
