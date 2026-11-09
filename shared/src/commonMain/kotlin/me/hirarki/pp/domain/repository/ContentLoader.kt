package me.hirarki.pp.domain.repository

expect suspend fun fetchContentJson(filename: String): String
