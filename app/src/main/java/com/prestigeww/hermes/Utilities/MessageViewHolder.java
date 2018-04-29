package com.prestigeww.hermes.Utilities;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;
import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.R;
import com.squareup.picasso.Picasso;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView senderTextView;
    private TextView bodyTextView;

    String sender;
    String email;

    private BubbleImageView bubbleImage;
    public MessageViewHolder(View itemView) {
        super(itemView);

        senderTextView = itemView.findViewById(R.id.sender_textview);
        bodyTextView = (BubbleTextView) itemView.findViewById(R.id.body_textview);
        bubbleImage = (BubbleImageView) itemView.findViewById(R.id.image_bubble);

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    }

    public void bindView(MessageInChat message){
        sender = message.getSender();
     //   senderTextView.setText(message.getSender());
    public void bindView(Context context, MessageInChat message){
        if(!message.getDocId().equals("no_doc")){
            bubbleImage.setVisibility(View.VISIBLE);
            Picasso.with(context.getApplicationContext()).load(message.getDocId()).into(bubbleImage);

        }
        senderTextView.setText(message.getSender());
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
