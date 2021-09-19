package com.astery.ccabp.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.view.activities.MainActivity;
import com.astery.ccabp.view.adapters.units.DayUnit;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{

    private final ArrayList<DayUnit> units;
    private BlockListener blockListener;
    private final MainActivity activity;
    private final Context context;
    private final Calendar now;

    private boolean isAvailable = true;

    public CalendarAdapter(ArrayList<DayUnit> blocks, MainActivity activity, Context context) {
        this.units = blocks;
        this.activity = activity;
        this.context = context;
        now = Calendar.getInstance();
    }

    public boolean isNotAvailable() {
        return !isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }
    public interface BlockListener {
        void onClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.unit_calendar, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        DayUnit unit = units.get(position);
        if (unit == null){
            holder.card.setVisibility(View.INVISIBLE);
            holder.indicator.setVisibility(View.INVISIBLE);
            return;
        } else
            holder.card.setVisibility(View.VISIBLE);

        if (unit.day == now.get(Calendar.DAY_OF_MONTH)){
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.todayCalendar));
            holder.card.setStrokeColor(context.getResources().getColor(R.color.todayCalendarStroke));
        } else{
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.card.setStrokeColor(context.getResources().getColor(R.color.otherCalendarStroke));
        }


        holder.day.setText(Integer.toString(unit.day));
        if (unit.enabled){
            holder.day.setAlpha(1f);
            holder.indicator.setVisibility(View.VISIBLE);
            if (!unit.accepted)
                holder.indicator.setBackground(context.getResources().getDrawable(R.drawable.warning_indicator, context.getTheme()));
            else if  (unit.dayState == DayUnit.DayState.no)
                holder.indicator.setVisibility(View.INVISIBLE);
            else if (unit.dayState == DayUnit.DayState.plan)
                holder.indicator.setBackground(context.getResources().getDrawable(R.drawable.on_indicator, context.getTheme()));
            else
                holder.indicator.setBackground(context.getResources().getDrawable(R.drawable.task_indicator, context.getTheme()));

        } else{
            holder.day.setAlpha(0.3f);
            holder.indicator.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView day;
        private final View indicator;
        private final MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day);
            indicator = itemView.findViewById(R.id.indicator);
            card = itemView.findViewById(R.id.card);

            itemView.setOnClickListener(v -> {
                activity.onTouch(null, null);
                if (isNotAvailable()) return;
                if (units.get(getAdapterPosition()).day <= now.get(Calendar.DAY_OF_MONTH)) return;
                if (blockListener != null && units.get(getAdapterPosition()).enabled) {
                    blockListener.onClick(getAdapterPosition());
                }
            });

        }
    }
}
