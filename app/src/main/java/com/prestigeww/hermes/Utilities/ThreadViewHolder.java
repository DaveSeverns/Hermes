package com.prestigeww.hermes.Utilities;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.R;

import java.util.Random;

public class ThreadViewHolder extends RecyclerView.ViewHolder {
    private TextView chatNameText;
    private TextView chatMemberCount;
    private RelativeLayout circleLayout;
    static Random randomGenerator;
    public ThreadViewHolder(View itemView) {
        super(itemView);
        chatNameText = itemView.findViewById(R.id.chat_name_label);
        chatMemberCount = itemView.findViewById(R.id.member_count_text);
        circleLayout = itemView.findViewById(R.id.circle_shape_layout);
    }

    public void bindThread(ChatThread thread){
        chatNameText.setText(thread.getChatName());
        if(!thread.getUserIds().isEmpty()){
            chatMemberCount.setText(thread.getUserIds().size());
        }
        GradientDrawable sd = (GradientDrawable) circleLayout.getBackground().mutate();
        int color = (int) Long.parseLong(generateColor(), 16);
        sd.setColor(color);

        sd.invalidateSelf();
    }

    private static String generateColor() {
        randomGenerator = new Random();
        int newColor = randomGenerator.nextInt(0x1000000);
        return String.format("FF%06X", newColor);
    }
}
