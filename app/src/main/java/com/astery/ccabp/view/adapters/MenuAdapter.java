package com.astery.ccabp.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.model.things.Menu;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.View.GONE;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.CourseLibViewHolder>{

    private final ArrayList<Menu> units;

    public MenuAdapter(Context context, ArrayList<Menu> blocks) {
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

        Menu unit = units.get(position);

        holder.name.setText(Menu.ThingIntToString(unit.getThing()));

        for (int i = 0; i < 7; i++){
            if (unit.getFood().size() <= i || unit.getFood().get(i).length() <= 2) {
                holder.things.get(i).setVisibility(GONE);
                continue;
            }
            holder.things.get(i).setText(unit.getFood().get(i));
        }


    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    static class CourseLibViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;

        private final ArrayList<TextView> things;

        public CourseLibViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);

            things = new ArrayList<>();
            things.add(itemView.findViewById(R.id.first_place));
            things.add(itemView.findViewById(R.id.second_place));
            things.add(itemView.findViewById(R.id.third_place));
            things.add(itemView.findViewById(R.id.fourth_place));
            things.add(itemView.findViewById(R.id.fifth_place));
            things.add(itemView.findViewById(R.id.six_place));
            things.add(itemView.findViewById(R.id.sevens_place));

            for (TextView t: things)
                t.setVisibility(View.VISIBLE);

        }
    }
}
