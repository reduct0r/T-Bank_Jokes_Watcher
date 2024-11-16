package com.example.homework_project_1.main.data

import com.example.homework_project_1.R

object AvatarProvider {
    private val defaultAvatars = listOf(
        R.drawable.def_ava1,
        R.drawable.def_ava2,
        R.drawable.def_ava3
    )
    private val programmingAvatars = listOf(
        R.drawable.prog_ava1,
        R.drawable.prog_ava2,
        R.drawable.prog_ava3,
        R.drawable.prog_ava4,
        R.drawable.prog_ava5,
        R.drawable.prog_ava6,
        R.drawable.prog_ava7
    )
    private val mathAvatars = listOf(
        R.drawable.math_ava1,
        R.drawable.math_ava2,
        R.drawable.math_ava3,
        R.drawable.math_ava4,
        R.drawable.math_ava5
    )
    private val scienceAvatars = listOf(
        R.drawable.sci_ava1,
        R.drawable.sci_ava2,
        R.drawable.sci_ava3,
        R.drawable.sci_ava4,
        R.drawable.math_ava1,
        R.drawable.math_ava4,
        R.drawable.math_ava5
    )
    private val techAvatars = listOf(
        R.drawable.tech_ava1,
        R.drawable.tech_ava2,
        R.drawable.tech_ava3,
        R.drawable.sci_ava2,
        R.drawable.sci_ava3,
        R.drawable.sci_ava4
    )

    private val categoryAvatars: Map<String, List<Int>> = mapOf(
        "General" to defaultAvatars,
        "Programming" to programmingAvatars,
        "Math" to mathAvatars,
        "Science" to scienceAvatars,
        "Tech" to techAvatars
    )

    fun getCategoryAvatars(): Map<String, List<Int>> {
        return categoryAvatars
    }

    fun getDefaultAvatars(): List<Int> {
        return defaultAvatars
    }
}
