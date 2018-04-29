package com.prestigeww.hermes.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleImageView;
import com.github.library.bubbleview.BubbleTextView;
import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;
import com.squareup.picasso.Picasso;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView senderTextView;
    private TextView bodyTextView;
    private BubbleImageView bubbleImage;
    public MessageViewHolder(View itemView) {
        super(itemView);
        senderTextView = itemView.findViewById(R.id.sender_textview);
        bodyTextView = (BubbleTextView) itemView.findViewById(R.id.body_textview);
        bubbleImage = (BubbleImageView) itemView.findViewById(R.id.image_bubble);

    }

    public void bindView(Context context, MessageInChat message){
        if(!message.getDocId().equals("no_doc")){
            bubbleImage.setVisibility(View.VISIBLE);
            Picasso.with(context.getApplicationContext()).load(message.getDocId()).into(bubbleImage);

        }
        senderTextView.setText(message.getSender());
        bodyTextView.setText(message.getBody());
    }


}
