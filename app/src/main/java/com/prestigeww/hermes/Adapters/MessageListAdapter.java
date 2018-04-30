package com.prestigeww.hermes.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.MessageViewHolder;

import java.util.List;
public class MessageListAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    List<MessageInChat> messages;
    Context mContext;
    String sender;

    public MessageListAdapter(List<MessageInChat> messages, Context context, String sender){
        this.messages = messages;
        mContext = context;
        this.sender = sender;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_holder_layout,parent,false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bindView(mContext,messages.get(position), sender);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


}
