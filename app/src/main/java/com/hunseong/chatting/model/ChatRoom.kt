package com.hunseong.chatting.model

data class ChatRoom(
    val otherUser: User,
    val comments: Map<String, Comment> = emptyMap()
)