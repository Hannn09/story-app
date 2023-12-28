package com.example.storyapp

import com.example.storyapp.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "2023-05-07T22:10:15.213Z",
                "almas",
                "Halo",
                37.422092,
                "story-$i",
                -122.08392
            )
            items.add(story)
        }
        return items
    }
}