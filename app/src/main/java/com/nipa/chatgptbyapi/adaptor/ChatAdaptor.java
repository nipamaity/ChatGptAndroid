package com.nipa.chatgptbyapi.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nipa.chatgptbyapi.R;
import com.nipa.chatgptbyapi.databinding.AdaptorChatBinding;
import com.nipa.chatgptbyapi.model.ChatModel;

import java.util.List;

public class ChatAdaptor extends RecyclerView.Adapter<ChatAdaptor.ViewHolder> {
    Context context;

    public ChatAdaptor(Context context, List<ChatModel> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    List<ChatModel> chatList;
    @NonNull
    @Override
    public ChatAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdaptorChatBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdaptor.ViewHolder holder, int position) {
        if(chatList.get(position).isChatByMe()){
            holder.chatBinding.tvMessageMe.setVisibility(View.VISIBLE);
            holder.chatBinding.tvMessageBot.setVisibility(View.GONE);
            holder.chatBinding.tvMessageMe.setText(chatList.get(position).getMessage());
        }else {
            holder.chatBinding.tvMessageMe.setVisibility(View.GONE);
            holder.chatBinding.tvMessageBot.setVisibility(View.VISIBLE);
            holder.chatBinding.tvMessageBot.setText(chatList.get(position).getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private AdaptorChatBinding chatBinding;
        public ViewHolder(AdaptorChatBinding chatBinding) {
            super(chatBinding.getRoot());
            this.chatBinding = chatBinding;
        }
    }

}
