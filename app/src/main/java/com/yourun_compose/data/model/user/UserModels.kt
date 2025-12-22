package com.yourun_compose.data.model.user

data class UserInfo(
    val id: Long,
    val nickname: String,
    val tendency: String,
    val tags: List<String>,
    val crewReward: Long = 0,
    val personalReward: Long = 0,
    val mvp: Long = 0
)

data class UpdateUserRequest(
    val nickname: String,
    val tags: List<String>
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