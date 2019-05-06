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

public class UsersListRecyclerAdapter
        extends RecyclerView.Adapter<UsersListRecyclerAdapter.ViewHolder> {

    private ArrayList<User> mUsers;
    private UserRecyclerClickListener mUserRecyclerClickListener;

    public UsersListRecyclerAdapter(ArrayList<User> users, UserRecyclerClickListener userRecyclerClickListener) {
        this.mUsers = users;
        mUserRecyclerClickListener = userRecyclerClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_users_list_items,
                parent,
                false
        );
        final ViewHolder holder = new ViewHolder(view,
                mUserRecyclerClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((ViewHolder)holder).userFullName.setText(mUsers.get(position).getUserFullName());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        TextView userFullName;
        UserRecyclerClickListener clickListener;

        public ViewHolder(View itemView, UserRecyclerClickListener clickListener) {
            super(itemView);
            userFullName = itemView.findViewById(R.id.username);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onUserSelected(getAdapterPosition());
        }
    }

    public interface UserRecyclerClickListener {
        void onUserSelected(int position);
    }
}