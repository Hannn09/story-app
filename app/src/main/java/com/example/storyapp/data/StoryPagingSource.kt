package com.example.storyapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.retrofit.ApiService
import com.example.storyapp.utils.SettingsPreferences
import kotlinx.coroutines.flow.first

class StoryPagingSource(private val pref: SettingsPreferences, private val apiService: ApiService) :
    PagingSource<Int, ListStoryItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = pref.getSession().first().token

            if (token.isNotEmpty()) {
                val responseData = apiService.getPagingStories("Bearer $token", position, params.loadSize)
                Log.d("StoryPagingSource", "Load Result: ${responseData.body()}")
                if (responseData.isSuccessful) {
                    Log.d("StoryPagingSource", "Load Result: ${responseData.body()}")
                    LoadResult.Page(
                        data = responseData.body()?.listStory ?: emptyList(),
                        prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                        nextKey = if (responseData.body()?.listStory.isNullOrEmpty()) null else position + 1
                    )
                } else {
                    Log.d("StoryPagingSource", "Load: ${responseData.message()}")
                    LoadResult.Error(Exception("Something went wrong"))
                }

            } else {
                Log.d("StoryPagingSource", "Load Error: $token")
                LoadResult.Error(Exception("Token is Empty"))
            }

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}