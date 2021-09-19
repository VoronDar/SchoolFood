package com.astery.ccabp.view.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.model.things.Menu;
import com.astery.ccabp.model.things.Task;
import com.astery.ccabp.view.activities.MainActivity;
import com.astery.ccabp.view.activities.MainFragments.PlanFragment;
import com.astery.ccabp.view.activities.utilities.ArrowAnimate;

import java.util.ArrayList;

import static com.astery.ccabp.model.cloud_database.CloudUtils.isConnection;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.CourseLibViewHolder>{

    private final ArrayList<Task> units;
    private final PlanFragment fr;
    private int openPos;
    private BlockListener blockListener;
    private final MainActivity activity;

    private boolean isAvailable = true;

    public TaskAdapter(ArrayList<Task> blocks, int openPosition, PlanFragment fr, MainActivity activity) {
        this.units = blocks;
        this.openPos = openPosition;
        this.fr = fr;
        this.activity = activity;
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
        void onGoMenu(int position);
        void onGoScedule();
    }

    @NonNull
    @Override
    public CourseLibViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plan_unit, viewGroup, false);
        return new CourseLibViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseLibViewHolder holder, final int position) {

        Task unit = units.get(position);

        holder.name.setText(Menu.DayIntToString(position+2));

        if (unit.isBreakfast()){
            holder.ind_0.setAlpha(1f);
            holder.check_0.setChecked(true);
        } else{
            holder.ind_0.setAlpha(0.15f);
            holder.check_0.setChecked(false);
        }

        if (unit.isLanch()){
            holder.ind_1.setAlpha(1f);
            holder.check_1.setChecked(true);
        } else{
            holder.ind_1.setAlpha(0.15f);
            holder.check_1.setChecked(false);
        }

        if (unit.isSnak10()){
            holder.ind_2.setAlpha(1f);
            holder.check_2.setChecked(true);
        } else{
            holder.ind_2.setAlpha(0.15f);
            holder.check_2.setChecked(false);
        }

        if (unit.isSnak15()){
            holder.ind_3.setAlpha(1f);
            holder.check_3.setChecked(true);
        } else{
            holder.ind_3.setAlpha(0.15f);
            holder.check_3.setChecked(false);
        }

        if (openPos == position){
            ArrowAnimate.setOpen(holder.arrow);
            holder.place_0.setVisibility(View.VISIBLE);
            holder.place_1.setVisibility(View.VISIBLE);
            holder.place_2.setVisibility(View.VISIBLE);
            holder.place_3.setVisibility(View.VISIBLE);
            holder.menu.setVisibility(View.VISIBLE);
            holder.scedule.setVisibility(View.VISIBLE);
        } else{
            ArrowAnimate.setClose(holder.arrow);
            holder.place_0.setVisibility(View.GONE);
            holder.place_1.setVisibility(View.GONE);
            holder.place_2.setVisibility(View.GONE);
            holder.place_3.setVisibility(View.GONE);
            holder.menu.setVisibility(View.GONE);
            holder.scedule.setVisibility(View.GONE);

        }

        if (isConnection(activity.getApplicationContext())){
            holder.check_0.setClickable(true);
            holder.check_1.setClickable(true);
            holder.check_2.setClickable(true);
            holder.check_3.setClickable(true);
            holder.ind_0.setClickable(true);
            holder.ind_1.setClickable(true);
            holder.ind_2.setClickable(true);
            holder.ind_3.setClickable(true);
        } else{
            holder.check_0.setClickable(false);
            holder.check_1.setClickable(false);
            holder.check_2.setClickable(false);
            holder.check_3.setClickable(false);
            holder.ind_0.setClickable(false);
            holder.ind_1.setClickable(false);
            holder.ind_2.setClickable(false);
            holder.ind_3.setClickable(false);
            Log.i("main", "clickABLE - false");

        }

    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class CourseLibViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final ImageView ind_0;
        private final ImageView ind_1;
        private final ImageView ind_2;
        private final ImageView ind_3;
        private final CheckBox check_0;
        private final CheckBox check_1;
        private final CheckBox check_2;
        private final CheckBox check_3;
        private final View place_0;
        private final View place_1;
        private final View place_2;
        private final View place_3;
        private final Button menu;
        private final Button scedule;
        private final View arrow;

        public CourseLibViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            ind_0 = itemView.findViewById(R.id.ind_0);
            ind_1 = itemView.findViewById(R.id.ind_1);
            ind_2 = itemView.findViewById(R.id.ind_2);
            ind_3 = itemView.findViewById(R.id.ind_3);
            check_0 = itemView.findViewById(R.id.first_accept);
            check_1 = itemView.findViewById(R.id.second_accept);
            check_2 = itemView.findViewById(R.id.third_accept);
            check_3 = itemView.findViewById(R.id.fourth_accept);
            place_0 = itemView.findViewById(R.id.first_place);
            place_1 = itemView.findViewById(R.id.second_place);
            place_2 = itemView.findViewById(R.id.third_place);
            place_3 = itemView.findViewById(R.id.fourth_place);
            menu = itemView.findViewById(R.id.bt_menu);
            scedule = itemView.findViewById(R.id.bt_scedule);
            arrow = itemView.findViewById(R.id.more_details);


            ArrowAnimate.setClose(arrow);

            menu.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if(!isAvailable) return;
                if (blockListener != null){
                    blockListener.onGoMenu(getAdapterPosition());
                }
            });
            scedule.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if(!isAvailable) return;
                if (blockListener != null){
                    blockListener.onGoScedule();
                }
            });


            check_0.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if(!isAvailable) return;
                fr.setPlanAccepted();
            });

            check_1.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if(!isAvailable) return;
                fr.setPlanAccepted();
            });

            check_2.setOnClickListener(view -> {
                activity.onTouch(null, null);

                if(!isAvailable) return;
                fr.setPlanAccepted();
            });

            check_3.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if(!isAvailable) return;
                fr.setPlanAccepted();
            });



            check_0.setOnCheckedChangeListener((compoundButton, b) -> {
                if(!isAvailable) return;
                units.get(getAdapterPosition()).setBreakfast(b);
                if (units.get(getAdapterPosition()).isBreakfast()){
                    ind_0.setAlpha(1f);
                } else{
                    ind_0.setAlpha(0.15f);
                }
            });


            ind_0.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if(!isAvailable) return;
                fr.setPlanAccepted();
                units.get(getAdapterPosition()).setBreakfast(!units.get(getAdapterPosition()).isBreakfast());
                if (units.get(getAdapterPosition()).isBreakfast()){
                    ind_0.setAlpha(1f);
                    check_0.setChecked(true);
                } else{
                    ind_0.setAlpha(0.15f);
                    check_0.setChecked(false);
                }
            });


            check_1.setOnCheckedChangeListener((compoundButton, b) -> {
                if(!isAvailable) return;
                units.get(getAdapterPosition()).setLanch(b);
                if (units.get(getAdapterPosition()).isLanch()){
                    ind_1.setAlpha(1f);
                } else{
                    ind_1.setAlpha(0.15f);
                }
            });

            ind_1.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if(!isAvailable) return;
                fr.setPlanAccepted();
                units.get(getAdapterPosition()).setLanch(!units.get(getAdapterPosition()).isLanch());
                if (units.get(getAdapterPosition()).isLanch()){
                    ind_1.setAlpha(1f);
                    check_1.setChecked(true);
                } else{
                    ind_1.setAlpha(0.15f);
                    check_1.setChecked(false);
                }
            });

            check_2.setOnCheckedChangeListener((compoundButton, b) -> {
                if(!isAvailable) return;
                units.get(getAdapterPosition()).setSnak10(b);
                if (units.get(getAdapterPosition()).isSnak10()){
                    ind_2.setAlpha(1f);
                } else{
                    ind_2.setAlpha(0.15f);
                }
            });

            ind_2.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if(!isAvailable) return;
                fr.setPlanAccepted();
                units.get(getAdapterPosition()).setSnak10(!units.get(getAdapterPosition()).isSnak10());
                if (units.get(getAdapterPosition()).isSnak10()){
                    ind_2.setAlpha(1f);
                    check_2.setChecked(true);
                } else{
                    ind_2.setAlpha(0.15f);
                    check_2.setChecked(false);
                }
            });

            check_3.setOnCheckedChangeListener((compoundButton, b) -> {
                if(!isAvailable) return;
                units.get(getAdapterPosition()).setSnak15(b);
                if (units.get(getAdapterPosition()).isSnak15()){
                    ind_3.setAlpha(1f);
                } else{
                    ind_3.setAlpha(0.15f);
                }
            });

            ind_3.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if(!isAvailable) return;
                fr.setPlanAccepted();
                units.get(getAdapterPosition()).setSnak15(!units.get(getAdapterPosition()).isSnak15());
                if (units.get(getAdapterPosition()).isSnak15()){
                    ind_3.setAlpha(1f);
                    check_3.setChecked(true);
                } else{
                    ind_3.setAlpha(0.15f);
                    check_3.setChecked(false);
                }
            });

            itemView.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if(!isAvailable) return;
                int oldPos = openPos;
                openPos = getAdapterPosition();
                if (oldPos == openPos)
                    openPos = -1;
                if (oldPos >= 0)
                    notifyItemChanged(oldPos);
                if (openPos >= 0)
                    notifyItemChanged(openPos);
            });

        }
    }
}
