package com.astery.ccabp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.model.things.Menu;
import com.astery.ccabp.model.things.Scedule;

import java.util.ArrayList;

import static android.view.View.GONE;

public class SceduleAdapter extends RecyclerView.Adapter<SceduleAdapter.CourseLibViewHolder>{

    private final ArrayList<Scedule> units;

    public SceduleAdapter(ArrayList<Scedule> blocks) {
        this.units = blocks;
    }


    public void delete(){
        units.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CourseLibViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_unit, viewGroup, false);
        return new CourseLibViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseLibViewHolder holder, final int position) {

        Scedule unit = units.get(position);

        holder.name.setText(Menu.ThingIntToString(unit.getThing()));
        if (unit.getInfo() != null && unit.getInfo().length() > 1)
            holder.first_thing.setText(unit.getInfo());
        else
            holder.first_thing.setVisibility(GONE);



    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    static class CourseLibViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView first_thing;

        public CourseLibViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            first_thing = itemView.findViewById(R.id.first_place);

        }
    }
}
