package com.astery.ccabp.view.activities.utilities;

import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.view.adapters.DialogSelectAdapter;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.util.ArrayList;

public class DialogListShower {
    public static void show(FragmentActivity context, final ArrayList<SimpleUnit> list, final OnSelectListener listener){

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        View my_custom_view = context.getLayoutInflater().inflate(R.layout.dialog_list, null);
        adb.setView(my_custom_view);

        DialogSelectAdapter adapter = new DialogSelectAdapter(context, list);
        RecyclerView recyclerView = my_custom_view.findViewById(R.id.recyclerView);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        final AlertDialog ad = adb.create();
        adapter.setBlockListener(new DialogSelectAdapter.BlockListener() {
            @Override
            public void onSelect(int position) {
                ad.cancel();
                listener.onSelect(position);
            }
        });
        ad.show();
    }

    public interface OnSelectListener{
        void onSelect(int position);
    }
}
