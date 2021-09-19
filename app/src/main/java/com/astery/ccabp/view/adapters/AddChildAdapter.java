package com.astery.ccabp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.view.adapters.units.SimpleUnit;

import java.util.ArrayList;

import static android.view.View.GONE;

public class AddChildAdapter extends RecyclerView.Adapter<AddChildAdapter.CourseLibViewHolder>{

    private final ArrayList<SimpleUnit> units;
    private BlockListener blockListener;

    public AddChildAdapter(Context context, ArrayList<SimpleUnit> blocks) {
        this.units = blocks;
    }

    public interface BlockListener {
        void onMoreClick(int position);
        void onAddClick(int position);
    }


    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }

    @NonNull
    @Override
    public CourseLibViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.unit_register_child, viewGroup, false);
        return new CourseLibViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseLibViewHolder holder, final int position) {

        SimpleUnit unit = units.get(position);
        if (unit.name != null){
            holder.addNew.setVisibility(GONE);
            holder.name.setText(unit.name);
            holder.itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onMoreClick(position);
            });
        } else{
            holder.moreDetails.setVisibility(GONE);
            holder.name.setText("Добавить ребенка");
            holder.itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onAddClick(position);
            });
        }

    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class CourseLibViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final ImageView moreDetails;
        private final ImageView addNew;

        public CourseLibViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            moreDetails = itemView.findViewById(R.id.more_details);
            addNew = itemView.findViewById(R.id.add_button);

        }
    }
}
