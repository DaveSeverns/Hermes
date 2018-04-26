package com.prestigeww.hermes.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.ThreadViewHolder;

import java.util.ArrayList;

public class ThreadListAdapter extends FirebaseRecyclerAdapter<ChatThread,ThreadViewHolder> {


    private String filterString;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     * @param isFiltarable
     */
    public ThreadListAdapter(FirebaseRecyclerOptions<ChatThread> options, boolean isFiltarable, String uId) {
        super(options, true);
        this.filterString = uId;
    }

    @Override
    protected void onBindViewHolder(ThreadViewHolder holder, int position, ChatThread model) {
        holder.bindThread(model);
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thread_holder_view,parent,false);
        return new ThreadViewHolder(view);
    }

    @Override
    protected boolean filterCondition(ChatThread model, String filterPattern) {
        return model.getUserIds().contains(filterString);
    }
}
