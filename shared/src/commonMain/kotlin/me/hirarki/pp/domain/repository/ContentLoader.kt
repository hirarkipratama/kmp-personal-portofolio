package me.hirarki.pp.domain.repository

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun fetchContentJson(filename: String): String

expect suspend fun loadProjectImage(imagePath: String): ImageBitmap
