package com.hunseong.chatting.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hunseong.chatting.databinding.ItemFriendBinding
import com.hunseong.chatting.databinding.ItemMessageBinding
import com.hunseong.chatting.model.Comment
import com.hunseong.chatting.model.User

class MessageAdapter() : ListAdapter<Comment, MessageAdapter.ViewHolder>(
    diffUtil) {

    inner class ViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.messageTv.text = comment.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
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