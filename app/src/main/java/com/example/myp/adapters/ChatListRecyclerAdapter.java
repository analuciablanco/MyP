package com.example.myp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myp.FireBase.models.User;
import com.example.myp.R;

import java.util.ArrayList;

public class ChatListRecyclerAdapter
        extends RecyclerView.Adapter<ChatListRecyclerAdapter.ViewHolder>{

    private ArrayList<User> mUsers = new ArrayList<>();
    private ChatListRecyclerClickListener mChatListRecyclerClickListener;

    public ChatListRecyclerAdapter(ArrayList<User> users, ChatListRecyclerClickListener chatListRecyclerClickListener) {
        this.mUsers = users;
        mChatListRecyclerClickListener = chatListRecyclerClickListener;
    }

//    private Set<Classroom> mUsers = new HashSet<>();
//    private ClassroomRecyclerClickListener mClassroomRecyclerClickListener;
//
//    public ClassroomsListRecyclerAdapter(Set<Classroom> chatrooms, ClassroomRecyclerClickListener chatroomRecyclerClickListener) {
//        this.mUsers = chatrooms;
//        mClassroomRecyclerClickListener = chatroomRecyclerClickListener;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_list_items, parent, false);
        final ViewHolder holder = new ViewHolder(view, mChatListRecyclerClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        ((ViewHolder)holder).userName.setText(((Classroom)(mUsers.toArray()[position])).getGrade());
        ((ViewHolder)holder).userName.setText(mUsers.get(position).getUserFullName());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        TextView userName;
        ChatListRecyclerClickListener clickListener;

        public ViewHolder(View itemView, ChatListRecyclerClickListener clickListener) {
            super(itemView);
            userName = itemView.findViewById(R.id.classroom_title);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClassroomSelected(getAdapterPosition());
        }
    }

    public interface ChatListRecyclerClickListener {
        public void onClassroomSelected(int position);
    }
}