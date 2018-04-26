package com.prestigeww.hermes.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.ThreadViewHolder;

import java.util.ArrayList;

public class ThreadListAdapter extends RecyclerView.Adapter<ThreadViewHolder> {
    ArrayList<ChatThread> collection = new ArrayList<>();

    public ThreadListAdapter(ArrayList<ChatThread> collection){

        this.collection = collection;
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.thread_holder_view, parent, false);
        return new ThreadViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {
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

}
