package com.yourun_compose.data.model.user

data class UserInfo(
    val id: Long,
    val nickname: String,
    val tendency: String,
    val tags: List<String>,
    val crewReward: Long,
    val personalReward: Long,
    val mvp: Long
)

data class UpdateUserRequest(
    val nickname: String,
    val tag1: String,
    val tag2: String
)

data class UserMateInfo(
    val id: Long,
    val nickname: String,
    val tendency: String,
    val tags: List<String>,
    val totalDistance: Int,
    val countDay: Int,
    var rank: Int = 0,
    var change: Int = 0,
    val profileImageResId: Int = 0
)