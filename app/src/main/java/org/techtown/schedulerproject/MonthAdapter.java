package org.techtown.schedulerproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ScheduleViewHolder>{
    ArrayList<org.techtown.schedulerproject.MonthSchedules> schedulesArrayList;

    public interface OnClickListener{
        void onClicked(MonthAdapter.ScheduleViewHolder viewHolder, View view, int position);
    }

    private OnClickListener clickListener;

    public void setClickListener(OnClickListener listener){
        this.clickListener = listener;
    }

    public MonthAdapter(ArrayList<org.techtown.schedulerproject.MonthSchedules> schedulesArrayAdapter){
        this.schedulesArrayList = schedulesArrayAdapter;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card,parent,false);
        MonthAdapter.ScheduleViewHolder viewHolder = new MonthAdapter.ScheduleViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = viewHolder.getAdapterPosition();
                if (clickListener != null){
                    clickListener.onClicked(viewHolder, view, position);
                }
            }
        });

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        holder.atb_maker.setImageResource(schedulesArrayList.get(position).getMarker());
        holder.atb_contents.setText(schedulesArrayList.get(position).getContents());
        holder.atb_date.setText(schedulesArrayList.get(position).getDate());
        holder.atb_id.setText(schedulesArrayList.get(position).getDataid());
    }
    @Override
    public int getItemCount() {
        return schedulesArrayList.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {

        TextView atb_date, atb_contents;
        ImageView atb_maker;
        Button atb_id;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            atb_maker= itemView.findViewById(R.id.iconImage);
            this.atb_contents = itemView.findViewById(R.id.textContent);
            this.atb_date = itemView.findViewById(R.id.dateTextView);
            this.atb_id = itemView.findViewById(R.id.cardIdButton);
        }
    }
    public org.techtown.schedulerproject.MonthSchedules getItem(int position){
        return schedulesArrayList.get(position);
    }

}
