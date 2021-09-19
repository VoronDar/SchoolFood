package com.astery.ccabp.view.activities.utilities;

import androidx.fragment.app.Fragment;

import com.astery.ccabp.view.activities.MainActivity;

/**
 * базовый класс для фрагментов основного пути
 *
 */
public abstract class MainFragment extends Fragment {
    /** фрагмент, на который можно перейти, нажав кнопку "назад"
     * определяет будет ли показываться кнопка назад тоже */
    private MainActivity.Steps back;

    public MainFragment(MainActivity.Steps back) {
        this.back = back;
    }

    public MainFragment() {
    }
}