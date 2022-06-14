package com.rustamft.tasksft.presentation.navigation

data class NavItem(
    val painterResId: Int,
    val descriptionResId: Int,
    val onClick: () -> Unit
)
