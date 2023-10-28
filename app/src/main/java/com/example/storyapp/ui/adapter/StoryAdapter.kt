package com.example.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ItemsStoryBinding
import com.example.storyapp.ui.story.detail.DetailStoryActivity
import com.example.storyapp.ui.story.detail.DetailStoryActivity.Companion.EXTRA_USER

class StoryAdapter(private val listStory: List<ListStoryItem>): RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

     class ListViewHolder(private val itemsStoryBinding: ItemsStoryBinding): RecyclerView.ViewHolder(itemsStoryBinding.root) {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryAdapter.ListViewHolder {
        val itemsStoryBinding = ItemsStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemsStoryBinding)
    }

    override fun onBindViewHolder(holder: StoryAdapter.ListViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size
}