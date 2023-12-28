package com.example.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ItemsStoryBinding
import com.example.storyapp.ui.story.detail.DetailStoryActivity
import com.example.storyapp.ui.story.detail.DetailStoryActivity.Companion.EXTRA_USER

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

     class MyViewHolder(private val itemsStoryBinding: ItemsStoryBinding): RecyclerView.ViewHolder(itemsStoryBinding.root) {
        fun bind(story: ListStoryItem) {
            itemsStoryBinding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(imgUser)
                username.text = story.name
                descriptionStory.text = story.description

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                    intent.putExtra(EXTRA_USER, story.id)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(imgUser, "image"),
                            Pair(username, "name"),
                            Pair(descriptionStory, "description")
                        )

                    itemView.context.startActivity(intent,
                       optionsCompat.toBundle()
                    )
                }
            }
        }

     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemsStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder((view))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = getItem(position)
        if (list != null) {
            holder.bind(list)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}