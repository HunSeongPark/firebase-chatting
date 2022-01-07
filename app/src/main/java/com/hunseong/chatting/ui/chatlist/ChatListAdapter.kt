package com.hunseong.chatting.ui.chatlist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hunseong.chatting.databinding.ItemChatBinding
import com.hunseong.chatting.model.Chat
import com.hunseong.chatting.model.ChatRoom
import com.hunseong.chatting.model.Comment
import com.hunseong.chatting.model.User
import java.util.*
import kotlin.collections.LinkedHashMap

class ChatListAdapter : ListAdapter<ChatRoom, ChatListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatRoom: ChatRoom) {
            binding.chatTitleTv.text = chatRoom.otherUser.userName

            if (chatRoom.otherUser.profileUrl.isNotBlank()) {
                Glide.with(binding.chatIv)
                    .load(chatRoom.otherUser.profileUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.chatIv)
            }

            val commentMap = TreeMap<String, Comment>(Collections.reverseOrder())
            commentMap.putAll(chatRoom.comments)
            val lastKey = commentMap.keys.toTypedArray()[0]
            binding.lastCommentTv.text = chatRoom.comments[lastKey]?.message ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ChatRoom>() {

            override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
                return oldItem.comments == newItem.comments
            }

            override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
                return oldItem == newItem
            }

        }
    }
}