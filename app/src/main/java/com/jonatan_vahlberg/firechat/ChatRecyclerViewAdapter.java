package com.jonatan_vahlberg.firechat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Message> messages;

    public ChatRecyclerViewAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_message_item,viewGroup,false);
        ChatRecyclerViewAdapter.ViewHolder vh = new ChatRecyclerViewAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message message = messages.get(i);
        if(message.getUID().equals(User.current.UID)){
            viewHolder.messageSender.setText("");
            viewHolder.itemView.findViewById(R.id.message_left_view).setVisibility(View.VISIBLE);
            viewHolder.itemView.findViewById(R.id.message_right_view).setVisibility(View.GONE);
        }
        else{
            viewHolder.messageSender.setText(message.getNick());
            viewHolder.itemView.findViewById(R.id.message_left_view).setVisibility(View.GONE);
            viewHolder.itemView.findViewById(R.id.message_right_view).setVisibility(View.VISIBLE);
        }
        viewHolder.message.setText(message.getMessage());
        viewHolder.timeStamp.setText(message.getStamp());
    }

    @Override
    public int getItemCount() {
        return messages.size() ;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView messageSender;
        TextView message;
        TextView timeStamp;

        public ViewHolder(View itemview) {
            super(itemview);
            messageSender =  itemview.findViewById(R.id.message_nick);
            message =  itemview.findViewById(R.id.message_text);
            timeStamp =  itemview.findViewById(R.id.message_time_stamp);
        }
    }
}
