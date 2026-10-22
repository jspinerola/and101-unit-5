package com.example.unit_5

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotoAdapter
    private val photoList = mutableListOf<PhotoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView)
        adapter = PhotoAdapter(photoList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 2. Fetch data from API
        fetchPicture()
    }

    private fun fetchPicture() {
        val client = AsyncHttpClient()
        val endpoint = "https://picsum.photos/v2/list?page=1&limit=20"

        client.get(endpoint, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                try {
                    val jsonArray = json.jsonArray
                    photoList.clear()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val imageUrl = item.getString("download_url")
                        val author = item.getString("author")
                        val url = item.getString("url")

                        photoList.add(PhotoItem(imageUrl, author, url))
                    }

                    adapter.notifyDataSetChanged()

                } catch (e: Exception) {
                    Log.e("PicsumAPI", "Error parsing JSON: ${e.message}")
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String?, throwable: Throwable?) {
                Log.e("PicsumAPI", "Request failed with code $statusCode: $errorResponse")
            }
        })
    }
}