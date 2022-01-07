package com.hunseong.chatting.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hunseong.chatting.databinding.ItemMyMessageBinding
import com.hunseong.chatting.databinding.ItemOtherMessageBinding
import com.hunseong.chatting.model.Comment
import com.hunseong.chatting.model.User

class MessageAdapter(private val otherUser: User) :
    ListAdapter<Comment, RecyclerView.ViewHolder>(diffUtil) {

    inner class MyMessageViewHolder(private val binding: ItemMyMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.messageTv.text = comment.message
        }
    }

    inner class OtherMessageViewHolder(private val binding: ItemOtherMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.messageTv.text = comment.message
            binding.messageNameTv.text = otherUser.userName

            if (otherUser.profileUrl.isNotBlank()) {
                Glide.with(binding.messageProfileIv)
                    .load(otherUser.profileUrl)
                    .apply(RequestOptions().circleCrop())
                    .into(binding.messageProfileIv)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MY_MESSAGE_TYPE -> MyMessageViewHolder(ItemMyMessageBinding.inflate(LayoutInflater.from(
                parent.context), parent, false))
            else -> OtherMessageViewHolder(ItemOtherMessageBinding.inflate(LayoutInflater.from(
                parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyMessageViewHolder -> holder.bind(getItem(position))
            is OtherMessageViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).uid != otherUser.uid) {
            MY_MESSAGE_TYPE
        } else {
            OTHER_MESSAGE_TYPE
        }
    }

    companion object {
        const val MY_MESSAGE_TYPE = 0
        const val OTHER_MESSAGE_TYPE = 1
        private val diffUtil = object : DiffUtil.ItemCallback<Comment>() {

            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.message == newItem.message
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }

        }
    }
}