package com.astery.ccabp.view.activities.utilities;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.astery.ccabp.R;

public class DialogChangeShower {
    public static void show(FragmentActivity context, final String name, final String original, final OnFinishListener listener){

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        final View my_custom_view = context.getLayoutInflater().inflate(R.layout.dialog_change, null);
        adb.setView(my_custom_view);

        ((TextView)my_custom_view.findViewById(R.id.title_text)).setText("Изменить " + name);
        ((EditText)my_custom_view.findViewById(R.id.change_text)).setHint(original);

        final AlertDialog ad = adb.create();

        my_custom_view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.cancel();
            }
        });

        my_custom_view.findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.accept(((EditText)my_custom_view.findViewById(R.id.change_text)).getText().toString());
                ad.cancel();
            }
        });
        ad.show();
    }

    public interface OnFinishListener{
        void accept(String string);
    }

}
