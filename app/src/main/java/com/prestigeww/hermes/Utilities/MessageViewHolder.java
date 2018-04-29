package com.prestigeww.hermes.Utilities;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView senderTextView;
    private TextView bodyTextView;

    String sender;
    String email;

    public MessageViewHolder(View itemView) {
        super(itemView);

        senderTextView = itemView.findViewById(R.id.sender_textview);
        bodyTextView = (BubbleTextView) itemView.findViewById(R.id.body_textview);

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    }

    public void bindView(MessageInChat message){
        sender = message.getSender();
     //   senderTextView.setText(message.getSender());
        bodyTextView.setText(message.getBody());

        if (sender == email){
            senderTextView.setText("Me");
            senderTextView.setBackgroundColor(Color.CYAN);
        }else {
            senderTextView.setText(message.getSender());
        }


    }

    public void setSenderBackground(){
        senderTextView.setBackgroundColor(Color.CYAN);
        senderTextView.setText("ME");
    }


}
