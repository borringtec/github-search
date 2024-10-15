package com.borringtec.github_search.serialize

import com.google.gson.annotations.SerializedName

data class UrlSerialize(
    val name: String,
    @SerializedName("html_url")
    val htmlUrl: String
)
