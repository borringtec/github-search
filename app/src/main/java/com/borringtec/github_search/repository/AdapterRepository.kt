package com.borringtec.github_search.repository

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.borringtec.github_search.R
import com.borringtec.github_search.serialize.UrlSerialize

class AdapterRepository(private val repositories: List<UrlSerialize>) :
    RecyclerView.Adapter<AdapterRepository.ViewHolder>() {

    var carItemLister: (UrlSerialize) -> Unit = {}
    var btnShareLister: (UrlSerialize) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.repository_item, parent, false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = repositories[position]
        holder.repoName.text = repository.name

        holder.itemView.setOnClickListener {
            carItemLister(repository)
        }

        holder.shareButton.setOnClickListener {
            btnShareLister(repository)
        }
    }

    override fun getItemCount(): Int = repositories.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val repoName: TextView =
            view.findViewById(R.id.item_repo_name)
        val shareButton: View =
            view.findViewById(R.id.item_share_button)
    }
}
