package com.borringtec.github_search.ui

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.borringtec.github_search.R
import com.borringtec.github_search.repository.GitHubRepository
import com.borringtec.github_search.serialize.UrlSerialize
import com.borringtec.github_search.repository.AdapterRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var confirm: Button
    private lateinit var listRepository: RecyclerView
    private lateinit var githubApi: GitHubRepository
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        showUserName()
        setupRetrofit()
        setupListeners()
    }

    private fun setupView() {
        username = findViewById(R.id.editTextText)
        confirm = findViewById(R.id.button)
        listRepository = findViewById(R.id.recyclerView)
        listRepository.layoutManager = LinearLayoutManager(this)
        sharedPreferences = getSharedPreferences("github_search", MODE_PRIVATE)
    }


    private fun setupListeners() {
        confirm.setOnClickListener {
            val user = username.text.toString()
            saveUserLocal(user)
            getAllReposByUserName(user)
        }
    }

    private fun saveUserLocal(user: String) {
        sharedPreferences.edit().putString("username", user).apply()
    }

    private fun showUserName() {
        val user = sharedPreferences.getString("username", "")
        username.setText(user)
    }

    private fun setupRetrofit() {
        val gson: Gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        githubApi = retrofit.create(GitHubRepository::class.java)
    }

    private fun getAllReposByUserName(user: String) {
        githubApi.getAllRepositoriesByUser(user).enqueue(object : Callback<List<UrlSerialize>> {
            override fun onResponse(
                call: Call<List<UrlSerialize>>,
                response: Response<List<UrlSerialize>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { setupAdapter(it) }
                }
            }

            override fun onFailure(call: Call<List<UrlSerialize>>, t: Throwable) {}
        })
    }

    private fun setupAdapter(list: List<UrlSerialize>) {
        val adapter = AdapterRepository(list)
        adapter.carItemLister = { urlRepository ->
            openBrowser(urlRepository.htmlUrl)
        }
        adapter.btnShareLister = { urlRepository ->
            shareRepositoryLink(urlRepository.htmlUrl)
        }
        listRepository.adapter = adapter
    }

    private fun shareRepositoryLink(urlRepository: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, urlRepository)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun openBrowser(urlRepository: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlRepository)))
    }
}
