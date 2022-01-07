package com.hunseong.chatting.model

data class Chat(
    val users: Map<String, Boolean> = emptyMap(),
    val comments: Map<String, Comment> = emptyMap()
)