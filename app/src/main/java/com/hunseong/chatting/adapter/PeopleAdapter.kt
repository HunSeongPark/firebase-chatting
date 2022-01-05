package com.hunseong.chatting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hunseong.chatting.databinding.ItemFriendBinding
import com.hunseong.chatting.model.User

class PeopleAdapter(private val onClick: (User) -> Unit) : ListAdapter<User, PeopleAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition.takeIf { it != NO_POSITION } ?: return@setOnClickListener
                onClick(getItem(position))
            }
        }

        fun bind(user: User) {
            binding.nameTv.text = user.userName

            if (user.profileUrl.isNotBlank()) {
                Glide.with(binding.profileIv)
                    .load(user.profileUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileIv)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.profileUrl == newItem.profileUrl
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

        }
    }
}