package com.prestigeww.hermes.Utilities;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleImageView;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;
import com.squareup.picasso.Picasso;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView senderTextView;
    private TextView bodyTextView;

    String sender;
    String email;

    private ImageView bubbleImage;
    public MessageViewHolder(View itemView) {
        super(itemView);

        senderTextView = itemView.findViewById(R.id.sender_textview);
        bodyTextView = (BubbleTextView) itemView.findViewById(R.id.body_textview);
        bubbleImage =  itemView.findViewById(R.id.image_sent);

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    }

    public void bindView(Context context, MessageInChat message) {
        sender = message.getSender();
        //   senderTextView.setText(message.getSender());

        if (!message.getDocId().equals("no_doc")) {
                bubbleImage.setVisibility(View.VISIBLE);
                Picasso.with(context.getApplicationContext()).load(message.getDocId()).into(bubbleImage);
                bodyTextView.setVisibility(View.GONE);
        }else{
            bodyTextView.setText(message.getBody());
        }

        senderTextView.setText(message.getSender());


       // if (sender == email) {
        //    senderTextView.setText("Me");
        //    senderTextView.setBackgroundColor(Color.CYAN);
        //} else {
        //    senderTextView.setText(message.getSender());
        //}


    }


}

