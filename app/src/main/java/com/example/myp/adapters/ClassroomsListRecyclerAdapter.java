package com.example.myp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myp.R;
import com.example.myp.FireBase.models.Classroom;

import java.util.ArrayList;

public class ClassroomsListRecyclerAdapter
        extends RecyclerView.Adapter<ClassroomsListRecyclerAdapter.ViewHolder>{

    private ArrayList<Classroom> mClassrooms;
    private ClassroomRecyclerClickListener mClassroomRecyclerClickListener;

    public ClassroomsListRecyclerAdapter(ArrayList<Classroom> classrooms, ClassroomRecyclerClickListener classroomRecyclerClickListener) {
        this.mClassrooms = classrooms;
        mClassroomRecyclerClickListener = classroomRecyclerClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_classrooms_list_items,
                parent,
                false
        );
        final ViewHolder holder = new ViewHolder(view,
                mClassroomRecyclerClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        ((ViewHolder)holder).userName.setText(((Classroom)(mClassrooms.toArray()[position])).getGrade());
        ((ViewHolder)holder).classroomGrade.setText(mClassrooms.get(position).titleTag());
    }

    @Override
    public int getItemCount() {
        return mClassrooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        TextView classroomGrade;
        ClassroomRecyclerClickListener clickListener;

        public ViewHolder(View itemView, ClassroomRecyclerClickListener clickListener) {
            super(itemView);
            classroomGrade = itemView.findViewById(R.id.username);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClassroomSelected(getAdapterPosition());
        }
    }

    public interface ClassroomRecyclerClickListener {
        void onClassroomSelected(int position);
    }
}