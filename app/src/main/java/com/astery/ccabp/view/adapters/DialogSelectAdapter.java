package com.astery.ccabp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.util.ArrayList;

public class DialogSelectAdapter extends RecyclerView.Adapter<DialogSelectAdapter.CourseLibViewHolder>{

    private ArrayList<SimpleUnit> units;
    private BlockListener blockListener;
    private Context context;

    public DialogSelectAdapter(Context context, ArrayList<SimpleUnit> blocks) {
        this.units = blocks;
        this.context = context;
    }

    public interface BlockListener {
        public void onSelect(int position);
    }


    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }

    @NonNull
    @Override
    public CourseLibViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_unit, viewGroup, false);
        return new CourseLibViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseLibViewHolder holder, int position) {

        SimpleUnit unit = units.get(position);
        if (unit.name != null)
            holder.name.setText(unit.name);

    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class CourseLibViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public CourseLibViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (blockListener != null){
                        blockListener.onSelect(getAdapterPosition());
                    }
                }
            });
        }
    }
}
