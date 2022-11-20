package org.techtown.schedulerproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> implements  OnMemoItemClickListener{
    ArrayList<Memo> items = new ArrayList<Memo>();
    OnMemoItemClickListener listener;
    OnMemoItemLongClickListener longListener;

    public void setOnItemClickListener(OnMemoItemClickListener listener)
    {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnMemoItemLongClickListener listener)
    {
        this.longListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        TextView textView3;

        public ViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();

                    if(longListener != null){
                        longListener.onItemLongClick(ViewHolder.this, view, position);
                    }

                    return true;
                }
            });
        }

        public void setItem(Memo item){
            textView.setText(item.getSubject());
            textView2.setText(item.getContent());
            textView3.setText(item.getTime());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.schedule_board, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Memo item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Memo item){
        items.add(item);
    }

    public Memo getItem(int position) {
        return items.get(position);
    }

}

