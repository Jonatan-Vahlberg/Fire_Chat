package com.jonatan_vahlberg.firechat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChatsRecycleViewAdapter extends RecyclerView.Adapter<ChatsRecycleViewAdapter.ViewHolder> {
    private Context context;

    public ChatsRecycleViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_recyle_view_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Chat chat = User.current.chats.get(i);
        View view = viewHolder.itemView;

        viewHolder.chatId.setText("#"+chat.getChat_id());
        viewHolder.chatTitle.setText(chat.getChat_name());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("CHAT_ID",chat.getId());
                intent.putExtra("CHAT_PUBLIC_ID",chat.getChat_id());
                intent.putExtra("CHAT_NAME",chat.getChat_name());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return User.current.chats.size() ;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView chatTitle;
        TextView chatId;

        public ViewHolder(View itemview) {
            super(itemview);
            chatTitle =  itemview.findViewById(R.id.chat_item_title);
            chatId =  itemview.findViewById(R.id.chat_item_id);
        }
    }
}
