package com.example.myp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myp.FireBase.models.ClassRoomMember;
import com.example.myp.R;

import java.util.ArrayList;

public class ChatListRecyclerAdapter
        extends RecyclerView.Adapter<ChatListRecyclerAdapter.ViewHolder>{

    private ArrayList<ClassRoomMember> mClassRoomMember;
    private ChatListRecyclerClickListener mChatListRecyclerClickListener;

    public ChatListRecyclerAdapter(ArrayList<ClassRoomMember> classRoomMembers, ChatListRecyclerClickListener chatListRecyclerClickListener) {
        this.mClassRoomMember = classRoomMembers;
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

//      ((ViewHolder)holder).username.setText(((Classroom)(mUsers.toArray()[position])).getGrade());
        ((ViewHolder)holder).username.setText("WUUU");
        //((ViewHolder)holder).username.setText(mClassRoomMember.get(position).getUser_full_name());
    }

    @Override
    public int getItemCount() {
        return mClassRoomMember.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        TextView username;
        ChatListRecyclerClickListener clickListener;

        public ViewHolder(View itemView, ChatListRecyclerClickListener clickListener) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
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