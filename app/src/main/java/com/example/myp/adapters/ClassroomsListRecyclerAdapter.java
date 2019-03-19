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

    private ArrayList<Classroom> mClassrooms = new ArrayList<>();
    private ClassroomRecyclerClickListener mClassroomRecyclerClickListener;

    public ClassroomsListRecyclerAdapter(ArrayList<Classroom> classrooms, ClassroomRecyclerClickListener classroomRecyclerClickListener) {
        this.mClassrooms = classrooms;
        mClassroomRecyclerClickListener = classroomRecyclerClickListener;
    }

//    private Set<Classroom> mClassrooms = new HashSet<>();
//    private ClassroomRecyclerClickListener mClassroomRecyclerClickListener;
//
//    public ClassroomsListRecyclerAdapter(Set<Classroom> chatrooms, ClassroomRecyclerClickListener chatroomRecyclerClickListener) {
//        this.mClassrooms = chatrooms;
//        mClassroomRecyclerClickListener = chatroomRecyclerClickListener;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_classrooms_list_items, parent, false);
        final ViewHolder holder = new ViewHolder(view, mClassroomRecyclerClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        ((ViewHolder)holder).classroomGrade.setText(((Classroom)(mClassrooms.toArray()[position])).getGrade());
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
            classroomGrade = itemView.findViewById(R.id.classroom_title);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClassroomSelected(getAdapterPosition());
        }
    }

    public interface ClassroomRecyclerClickListener {
        public void onClassroomSelected(int position);
    }
}