package com.prestigeww.hermes.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.prestigeww.hermes.Activities.ChatWindowActivity;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.ThreadViewHolder;

import java.util.ArrayList;

public class ThreadListAdapter extends RecyclerView.Adapter<ThreadViewHolder> {
    ArrayList<ChatThread> collection = new ArrayList<>();


    ThreadClickInterface threadClickInterface;

    public ThreadListAdapter(ArrayList<ChatThread> collection, ThreadClickInterface threadClickInterface){

        this.collection = collection;
        this.threadClickInterface = threadClickInterface;
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.thread_holder_view, parent, false);
        return new ThreadViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatId = holder.getIdOfThread();
                if(chatId != null){
                    threadClickInterface.windowIntent(chatId);
                }
            }
        });
        holder.bindThread(collection.get(position));

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }




    public interface ThreadClickInterface{
        public void windowIntent(String chatId);
    }

}
