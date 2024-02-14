package com.example.noteappkmm.domain.note

import com.example.noteappkmm.presentation.BabyBlueHex
import com.example.noteappkmm.presentation.LightGreenHex
import com.example.noteappkmm.presentation.RedOrangeHex
import com.example.noteappkmm.presentation.RedPinkHex
import com.example.noteappkmm.presentation.VioletHex
import kotlinx.datetime.LocalDateTime


data class Note(
    val id: Long?,
    val title: String,
    val content: String,
    val colorHex: Long,
    val created: LocalDateTime
) {
    companion object {
        private val colors = listOf(RedOrangeHex, RedPinkHex, LightGreenHex, BabyBlueHex, VioletHex)

        fun generateRandomColor() = colors.random()
    }
}
