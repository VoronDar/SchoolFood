package com.astery.ccabp.view.adapters;

import android.content.Context;
import android.icu.text.IDNA;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astery.ccabp.R;
import com.astery.ccabp.model.cloud_database.CloudDatabase;
import com.astery.ccabp.model.local_database.ChildDatabase;
import com.astery.ccabp.model.things.Child;
import com.astery.ccabp.view.activities.MainActivity;
import com.astery.ccabp.view.activities.utilities.ArrowAnimate;
import com.astery.ccabp.view.activities.utilities.DialogChangeShower;
import com.astery.ccabp.view.activities.utilities.InfoHoldable;
import com.astery.ccabp.view.activities.utilities.InfoPannel;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.astery.ccabp.view.activities.MainActivity.childSelected;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.CourseLibViewHolder>{

    private final ArrayList<Child> units;
    private final Context context;
    private int openPos;
    private BlockListener blockListener;

    private final MainActivity activity;


    public ProfileAdapter(Context context, ArrayList<Child> blocks, int openPosition, MainActivity activity) {
        units = new ArrayList<>();
        units.addAll(blocks);
        units.add(null);

        this.context = context;
        this.openPos = openPosition;
        this.activity = activity;
    }


    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }
    public interface BlockListener {
        void onGoHistory(int position);
    }


    @NonNull
    @Override
    public CourseLibViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_unit, viewGroup, false);
        return new CourseLibViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseLibViewHolder holder, final int position) {

        Child unit = units.get(position);

        if (unit == null){
            holder.nameTitle.setText("Добавить ребенка");
            holder.plan.setBackground(activity.getResources().getDrawable(R.drawable.button_outlined_background));
            holder.plan.setText(activity.getResources().getString(R.string.bt_add_child));
            holder.plan.setTextColor(activity.getResources().getColor(R.color.textButtonOutlined));
            holder.plan.setOnClickListener(v -> {
                activity.onTouch(null, null);
                activity.addNewChild();
            });
            holder.name.setVisibility(GONE);
            holder.school.setVisibility(GONE);
            holder.group.setVisibility(GONE);
            holder.change.setVisibility(GONE);
            holder.schoolLayout.setVisibility(View.GONE);
            holder.groupLayout.setVisibility(View.GONE);
            holder.plan.setVisibility(View.VISIBLE);
            holder.story.setVisibility(View.GONE);
            return;
        } else{
            holder.plan.setVisibility(View.GONE);
        }

        holder.name.setText(unit.getName()
                + " " + unit.getsName());

        holder.school.setText(unit.getSchoolName());
        holder.group.setText(unit.getGrade());

        if (openPos == position){
            if (holder.school.getVisibility() == View.VISIBLE)
            //ArrowAnimate.open(holder.arrow, context);
                ArrowAnimate.setOpen(holder.arrow);
            holder.school.setVisibility(View.VISIBLE);
            holder.group.setVisibility(View.VISIBLE);
            holder.schoolLayout.setVisibility(View.VISIBLE);
            holder.groupLayout.setVisibility(View.VISIBLE);
        } else{
            if (holder.school.getVisibility() == GONE)
                //ArrowAnimate.close(holder.arrow, context);
                ArrowAnimate.setClose(holder.arrow);
            holder.school.setVisibility(GONE);
            holder.group.setVisibility(GONE);
            holder.story.setVisibility(View.GONE);
            holder.schoolLayout.setVisibility(GONE);
            holder.groupLayout.setVisibility(GONE);
        }


    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class CourseLibViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView nameTitle;
        private final TextView school;
        private final TextView group;
        private final Button plan;
        private final Button story;
        private final View schoolLayout;
        private final View groupLayout;
        private final View arrow;
        private final View change;

        public CourseLibViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_info);
            nameTitle = itemView.findViewById(R.id.name);
            school = itemView.findViewById(R.id.school_info);
            group = itemView.findViewById(R.id.group_info);
            plan = itemView.findViewById(R.id.scedule);
            story = itemView.findViewById(R.id.story);
            arrow = itemView.findViewById(R.id.arrow);
            change = itemView.findViewById(R.id.more_child);


            schoolLayout = itemView.findViewById(R.id.school);
            groupLayout = itemView.findViewById(R.id.group);

            ArrowAnimate.setClose(arrow);

            story.setOnClickListener(v -> {
                activity.onTouch(null, null);
                blockListener.onGoHistory(getAdapterPosition());

            });

            itemView.setOnClickListener(view -> {
                activity.onTouch(null, null);
                if (openPos == getAdapterPosition()) {
                    int oldPos = openPos;
                    openPos = -1;
                    notifyItemChanged(oldPos);
                }
                else {
                    int oldPos = openPos;
                    openPos = getAdapterPosition();
                    if (oldPos != -1)
                        notifyItemChanged(oldPos);
                    notifyItemChanged(openPos);
                }
            });


            change.setOnClickListener(view -> DialogChangeShower.show(activity, "имя и фамилию", name.getText().toString(), string -> {
                String[] in = string.split(" ");
                if (in.length == 2){

                    activity.children.get(childSelected).setName(in[0]);
                    activity.children.get(childSelected).setsName(in[1]);
                    update(activity.children.get(childSelected));


                } else{
                    Toast.makeText(context, "Неверный формат имени и фамилии", Toast.LENGTH_SHORT).show();
                }
            }));


        }

        private void update(Child child){
            CloudDatabase db = new CloudDatabase(context, new CloudDatabase.Loadable() {
                @Override
                public void onFailure() {
                    InfoPannel.openPanel(activity, "Не удалось изменить имя", "Ок", null, false);
                }

                @Override
                public void onSuccess() {
                    ChildDatabase db = ChildDatabase.getInstance(context);
                    db.update(child);
                    name.setText(child.getName() + " " + child.getsName());
                }

                @Override
                public void onError() {

                }
            });
            db.updateChildName(child);
        }
    }
}
