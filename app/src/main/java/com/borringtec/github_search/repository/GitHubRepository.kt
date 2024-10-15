package com.borringtec.github_search.repository

import com.borringtec.github_search.serialize.UrlSerialize
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubRepository {

    @GET("users/{user}/repos")
    fun getAllRepositoriesByUser(@Path("user") user: String): Call<List<UrlSerialize>>

}
