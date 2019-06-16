package com.example.taskees;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Model.Data;

public class RecyclerAdapter extends RecyclerView.Adapter<myViewHolder> {

    List<Data> list;
    Context context;

    public RecyclerAdapter(List<Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_data, parent, false);
        myViewHolder myHolder = new myViewHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int position) {
        Data myList = list.get(position);
        myViewHolder.setDate(myList.getDate());
        myViewHolder.setTitle(myList.getTitle());
        myViewHolder.setNote(myList.getNote());
    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try{
            if(list.size()==0){
                arr = 0;
            } else {
                arr = list.size();
            }
        } catch(Exception e){

        }
        return arr;
    }
}

class myViewHolder extends RecyclerView.ViewHolder{

    View myView;
    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        myView=itemView;
    }

    public void setTitle(String title){
        TextView mTitle=myView.findViewById(R.id.title);
        mTitle.setText(title);
    }

    public void setNote(String note){
        TextView mNote = myView.findViewById(R.id.note);
        mNote.setText(note);
    }

    public void setDate(String date){
        TextView mDate = myView.findViewById(R.id.date);
        mDate.setText(date);
    }
}
