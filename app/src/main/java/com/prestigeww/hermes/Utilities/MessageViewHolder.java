package com.prestigeww.hermes.Utilities;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView senderTextView;
    private TextView bodyTextView;
    public MessageViewHolder(View itemView) {
        super(itemView);
        senderTextView = itemView.findViewById(R.id.sender_textview);
        bodyTextView = itemView.findViewById(R.id.body_textview);

    }

    public void bindView(MessageInChat message){
        senderTextView.setText(message.getSender());
        bodyTextView.setText(message.getBody());
    }


}
